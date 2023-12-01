/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.firestore;

import com.google.api.core.ApiAsyncFunction;
import com.google.api.core.ApiFunction;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.core.CurrentMillisClock;
import com.google.api.core.SettableApiFuture;
import com.google.api.gax.retrying.ExponentialRetryAlgorithm;
import com.google.api.gax.retrying.TimedAttemptSettings;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.firestore.telemetry.TraceUtil;
import com.google.common.util.concurrent.MoreExecutors;
import io.grpc.Context;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Implements backoff and retry semantics for Firestore transactions.
 *
 * <p>A TransactionRunner is instantiated with a `userCallback`, a `userCallbackExecutor` and
 * `numberOfAttempts`. Upon invoking {@link #run()}, the class invokes the provided callback on the
 * specified executor at most `numberOfAttempts` times. {@link #run()} returns an ApiFuture that
 * resolves when all retries complete.
 *
 * <p>TransactionRunner uses exponential backoff to increase the chance that retries succeed. To
 * customize the backoff settings, you can specify custom settings via {@link FirestoreOptions}.
 */
class TransactionRunner<T> {
  private final Transaction.AsyncFunction<T> userCallback;
  private final FirestoreImpl firestore;
  private final ScheduledExecutorService firestoreExecutor;
  private final Executor userCallbackExecutor;
  private final ExponentialRetryAlgorithm backoffAlgorithm;
  private final TransactionOptions transactionOptions;
  private TimedAttemptSettings nextBackoffAttempt;
  private Transaction transaction;
  private int attemptsRemaining;

  /**
   * @param firestore The active Firestore instance
   * @param userCallback The user provided transaction callback
   * @param transactionOptions The options determining which executor the {@code userCallback} is
   *     run on and whether the transaction is read-write or read-only
   */
  TransactionRunner(
      FirestoreImpl firestore,
      Transaction.AsyncFunction<T> userCallback,
      TransactionOptions transactionOptions) {
    this.transactionOptions = transactionOptions;
    this.firestore = firestore;
    this.firestoreExecutor = firestore.getClient().getExecutor();
    this.userCallback = userCallback;
    this.attemptsRemaining = transactionOptions.getNumberOfAttempts();
    this.userCallbackExecutor =
        Context.currentContextExecutor(
            transactionOptions.getExecutor() != null
                ? transactionOptions.getExecutor()
                : this.firestore.getClient().getExecutor());

    this.backoffAlgorithm =
        new ExponentialRetryAlgorithm(
            firestore.getOptions().getRetrySettings(), CurrentMillisClock.getDefaultClock());
    this.nextBackoffAttempt = backoffAlgorithm.createFirstAttempt();
  }

  ApiFuture<T> run() {
    TraceUtil.Span span = firestore.getTraceUtil().startSpan(TraceUtil.SPAN_NAME_TRANSACTION_RUN);
    span.setAttribute("transactionType", transactionOptions.getType().name());
    span.setAttribute("numAttemptsAllowed", transactionOptions.getNumberOfAttempts());
    span.setAttribute("attemptsRemaining", attemptsRemaining);
    try (io.opentelemetry.context.Scope ignored = span.makeCurrent()) {
      this.transaction = new Transaction(firestore, transactionOptions, this.transaction);

      --attemptsRemaining;

      ApiFuture<T> result =
          ApiFutures.catchingAsync(
              ApiFutures.transformAsync(
                  maybeRollback(), new RollbackCallback(), MoreExecutors.directExecutor()),
              Throwable.class,
              new RestartTransactionCallback(),
              MoreExecutors.directExecutor());
      span.endAtFuture(result);
      return result;
    } catch (Exception error) {
      span.end(error);
      throw error;
    }
  }

  private ApiFuture<Void> maybeRollback() {
    return transaction.hasTransactionId()
        ? transaction.rollback()
        : ApiFutures.immediateFuture(null);
  }

  /** A callback that invokes the BeginTransaction callback. */
  private class RollbackCallback implements ApiAsyncFunction<Void, T> {
    @Override
    public ApiFuture<T> apply(Void input) {
      final SettableApiFuture<Void> backoff = SettableApiFuture.create();
      // Add a backoff delay. At first, this is 0.
      firestoreExecutor.schedule(
          () -> backoff.set(null),
          nextBackoffAttempt.getRandomizedRetryDelay().toMillis(),
          TimeUnit.MILLISECONDS);

      nextBackoffAttempt = backoffAlgorithm.createNextAttempt(nextBackoffAttempt);
      return ApiFutures.transformAsync(
          backoff, new BackoffCallback(), MoreExecutors.directExecutor());
    }
  }

  /**
   * Invokes the user callback on the user callback executor and returns the user-provided result.
   */
  private SettableApiFuture<T> invokeUserCallback() {
    final SettableApiFuture<T> returnedResult = SettableApiFuture.create();

    userCallbackExecutor.execute(
        () -> {
          ApiFuture<T> userCallbackResult;
          try {
            userCallbackResult = userCallback.updateCallback(transaction);
          } catch (Exception e) {
            userCallbackResult = ApiFutures.immediateFailedFuture(e);
          }
          ApiFutures.addCallback(
              userCallbackResult,
              new ApiFutureCallback<T>() {
                @Override
                public void onFailure(Throwable t) {
                  returnedResult.setException(t);
                }

                @Override
                public void onSuccess(T result) {
                  returnedResult.set(result);
                }
              },
              firestoreExecutor);
        });
    return returnedResult;
  }

  /** A callback that invokes the BeginTransaction callback. */
  private class BackoffCallback implements ApiAsyncFunction<Void, T> {
    @Override
    public ApiFuture<T> apply(Void input) {
      return ApiFutures.transformAsync(
          transaction.begin(), new BeginTransactionCallback(), MoreExecutors.directExecutor());
    }
  }

  /**
   * The callback for the BeginTransaction RPC, which invokes the user callback and handles all
   * errors thereafter.
   */
  private class BeginTransactionCallback implements ApiAsyncFunction<Void, T> {
    public ApiFuture<T> apply(Void ignored) {
      return ApiFutures.transformAsync(
          invokeUserCallback(), new UserFunctionCallback(), MoreExecutors.directExecutor());
    }
  }

  /**
   * The callback that is invoked after the user function finishes execution. It invokes the Commit
   * RPC.
   */
  private class UserFunctionCallback implements ApiAsyncFunction<T, T> {
    @Override
    public ApiFuture<T> apply(T userFunctionResult) {
      return ApiFutures.transform(
          transaction.commit(),
          new CommitTransactionCallback(userFunctionResult),
          MoreExecutors.directExecutor());
    }
  }

  /** The callback that is invoked after the Commit RPC returns. It returns the user result. */
  private class CommitTransactionCallback implements ApiFunction<List<WriteResult>, T> {
    private final T userFunctionResult;

    CommitTransactionCallback(T userFunctionResult) {
      this.userFunctionResult = userFunctionResult;
    }

    @Override
    public T apply(List<WriteResult> input) {
      return userFunctionResult;
    }
  }

  /** A callback that restarts a transaction after an ApiException. It invokes the Rollback RPC. */
  private class RestartTransactionCallback implements ApiAsyncFunction<Throwable, T> {
    public ApiFuture<T> apply(Throwable throwable) {
      if (!(throwable instanceof ApiException)) {
        // This is likely a failure in the user callback.
        return rollbackAndReject(throwable);
      }

      ApiException apiException = (ApiException) throwable;
      if (transaction.hasTransactionId() && isRetryableTransactionError(apiException)) {
        if (attemptsRemaining > 0) {
          firestore.getTraceUtil().currentSpan().addEvent("Initiate transaction retry");
          return run();
        } else {
          final FirestoreException firestoreException =
              FirestoreException.forApiException(
                  apiException, "Transaction was cancelled because of too many retries.");
          return rollbackAndReject(firestoreException);
        }
      } else {
        final FirestoreException firestoreException =
            FirestoreException.forApiException(
                apiException, "Transaction failed with non-retryable error");
        return rollbackAndReject(firestoreException);
      }
    }

    /** Determines whether the provided error is considered retryable. */
    private boolean isRetryableTransactionError(ApiException exception) {
      switch (exception.getStatusCode().getCode()) {
          // This list is based on
          // https://github.com/firebase/firebase-js-sdk/blob/c822e78b00dd3420dcc749beb2f09a947aa4a344/packages/firestore/src/core/transaction_runner.ts#L112
        case ABORTED:
        case CANCELLED:
        case UNKNOWN:
        case DEADLINE_EXCEEDED:
        case INTERNAL:
        case UNAVAILABLE:
        case UNAUTHENTICATED:
        case RESOURCE_EXHAUSTED:
          return true;
        case INVALID_ARGUMENT:
          // The Firestore backend uses "INVALID_ARGUMENT" for transactions IDs that have expired.
          // While INVALID_ARGUMENT is generally not retryable, we retry this specific case.
          return exception.getMessage().contains("transaction has expired");
        default:
          return false;
      }
    }

    /** Rolls the transaction back and returns the error. */
    private ApiFuture<T> rollbackAndReject(final Throwable throwable) {
      final SettableApiFuture<T> failedTransaction = SettableApiFuture.create();

      if (transaction.hasTransactionId()) {
        // We use `addListener()` since we want to return the original exception regardless of
        // whether rollback() succeeds.
        transaction
            .rollback()
            .addListener(
                () -> failedTransaction.setException(throwable), MoreExecutors.directExecutor());
      } else {
        failedTransaction.setException(throwable);
      }

      return failedTransaction;
    }
  }
}
