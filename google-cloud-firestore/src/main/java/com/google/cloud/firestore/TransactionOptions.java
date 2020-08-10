/*
 * Copyright 2017 Google LLC
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

import com.google.api.core.InternalApi;
import com.google.common.base.Preconditions;
import com.google.firestore.v1.TransactionOptions.ReadOnly;
import com.google.firestore.v1.TransactionOptions.ReadWrite;
import com.google.protobuf.Timestamp;
import com.google.protobuf.TimestampOrBuilder;
import java.util.concurrent.Executor;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/** Options specifying the behavior of Firestore Transactions. */
public final class TransactionOptions {

  private static final TransactionOptions DEFAULT_READ_WRITE_TRANSACTION_OPTIONS =
      createReadWriteOptionsBuilder().build();

  private static final int DEFAULT_NUM_ATTEMPTS = 5;

  private final Executor executor;
  private final TransactionOptionsType type;
  private final ReadOnlyOptions readOnly;
  private final ReadWriteOptions readWrite;

  TransactionOptions(
      Executor executor,
      TransactionOptionsType type,
      ReadOnlyOptions readOnly,
      ReadWriteOptions readWrite) {
    this.executor = executor;
    this.type = type;
    this.readOnly = readOnly;
    this.readWrite = readWrite;
  }

  /**
   * @return the initial number of attempts a read-write transaction will be attempted
   * @deprecated as of v2.0.0, only applicable to Read-Write transactions. Use {@link
   *     ReadWriteOptions#getNumberOfAttempts()} instead
   */
  @Deprecated
  @InternalApi
  public int getNumberOfAttempts() {
    if (type == TransactionOptionsType.READ_WRITE) {
      return readWrite.numberOfAttempts;
    } else {
      return 1;
    }
  }

  /** @return Executor to be used to run user callbacks on */
  @Nullable
  public Executor getExecutor() {
    return executor;
  }

  @Nonnull
  public TransactionOptionsType getType() {
    return type;
  }

  @Nonnull
  public ReadOnlyOptions getReadOnly() {
    Preconditions.checkState(
        readOnly != null && readWrite == null, "Unable to call getReadOnly for ReadWriteOptions");
    return readOnly;
  }

  @Nonnull
  public ReadWriteOptions getReadWrite() {
    Preconditions.checkState(
        readWrite != null && readOnly == null, "Unable to call getReadWrite for ReadOnlyOptions");
    return readWrite;
  }

  /**
   * Create a default set of options suitable for most use cases. Transactions will be opened as
   * ReadWrite transactions and attempted up to 5 times.
   *
   * @return The TransactionOptions object.
   * @see #createReadWriteOptionsBuilder()
   */
  @Nonnull
  public static TransactionOptions create() {
    return DEFAULT_READ_WRITE_TRANSACTION_OPTIONS;
  }

  /**
   * Create a default set of options with a custom number of retry attempts.
   *
   * @param numberOfAttempts The number of execution attempts.
   * @return The TransactionOptions object.
   * @deprecated as of 2.0.0, replaced by {@link ReadWriteOptionsBuilder#setNumberOfAttempts(int)}
   * @see #createReadWriteOptionsBuilder()
   */
  @Nonnull
  @Deprecated
  public static TransactionOptions create(int numberOfAttempts) {
    return createReadWriteOptionsBuilder().setNumberOfAttempts(numberOfAttempts).build();
  }

  /**
   * Create a default set of options with a custom executor.
   *
   * @param executor The executor to run the user callback code on.
   * @return The TransactionOptions object.
   * @deprecated as of 2.0.0, replaced by {@link ReadWriteOptionsBuilder#setExecutor(Executor)}
   * @see #createReadWriteOptionsBuilder()
   */
  @Nonnull
  @Deprecated
  public static TransactionOptions create(@Nullable Executor executor) {
    return createReadWriteOptionsBuilder().setExecutor(executor).build();
  }

  /**
   * Create a default set of options with a custom executor and a custom number of retry attempts.
   *
   * @param executor The executor to run the user callback code on.
   * @param numberOfAttempts The number of execution attempts.
   * @return The TransactionOptions object.
   * @deprecated as of 2.0.0, replaced by {@link ReadWriteOptionsBuilder#setExecutor(Executor)} and
   *     {@link ReadWriteOptionsBuilder#setNumberOfAttempts(int)}
   * @see #createReadWriteOptionsBuilder()
   */
  @Nonnull
  @Deprecated
  public static TransactionOptions create(@Nullable Executor executor, int numberOfAttempts) {
    return createReadWriteOptionsBuilder()
        .setExecutor(executor)
        .setNumberOfAttempts(numberOfAttempts)
        .build();
  }

  @Nonnull
  public static ReadWriteOptionsBuilder createReadWriteOptionsBuilder() {
    return Builder.readWriteBuilder();
  }

  @Nonnull
  public static ReadOnlyOptionsBuilder createReadOnlyOptionsBuilder() {
    return Builder.readOnlyBuilder();
  }

  public abstract static class Builder<B extends Builder<B>> {
    @Nullable protected Executor executor;

    protected Builder(@Nullable Executor executor) {
      this.executor = executor;
    }

    @Nullable
    public Executor getExecutor() {
      return executor;
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setExecutor(@Nullable Executor executor) {
      this.executor = executor;
      return (B) this;
    }

    @Nonnull
    public abstract TransactionOptions build();

    static ReadOnlyOptionsBuilder readOnlyBuilder() {
      return new ReadOnlyOptionsBuilder(null, null);
    }

    static ReadWriteOptionsBuilder readWriteBuilder() {
      return new ReadWriteOptionsBuilder(null, DEFAULT_NUM_ATTEMPTS);
    }
  }

  public static final class ReadOnlyOptionsBuilder extends Builder<ReadOnlyOptionsBuilder> {
    @Nullable private TimestampOrBuilder readTime;

    private ReadOnlyOptionsBuilder(@Nullable Executor executor, @Nullable Timestamp readTime) {
      super(executor);
      this.readTime = readTime;
    }

    @Nullable
    public TimestampOrBuilder getReadTime() {
      return readTime;
    }

    public ReadOnlyOptionsBuilder setReadTime(@Nullable TimestampOrBuilder readTime) {
      this.readTime = readTime;
      return this;
    }

    @Nonnull
    @Override
    public TransactionOptions build() {
      final Timestamp timestamp;
      if (readTime != null && readTime instanceof Timestamp.Builder) {
        timestamp = ((Timestamp.Builder) readTime).build();
      } else {
        timestamp = (Timestamp) readTime;
      }
      return new TransactionOptions(
          executor, TransactionOptionsType.READ_ONLY, new ReadOnlyOptions(timestamp), null);
    }
  }

  public static final class ReadWriteOptionsBuilder extends Builder<ReadWriteOptionsBuilder> {
    private int numberOfAttempts;

    private ReadWriteOptionsBuilder(@Nullable Executor executor, int numberOfAttempts) {
      super(executor);
      this.numberOfAttempts = numberOfAttempts;
    }

    public int getNumberOfAttempts() {
      return numberOfAttempts;
    }

    @Nonnull
    public ReadWriteOptionsBuilder setNumberOfAttempts(int numberOfAttempts) {
      Preconditions.checkArgument(numberOfAttempts > 0, "You must allow at least one attempt");
      this.numberOfAttempts = numberOfAttempts;
      return this;
    }

    @Nonnull
    @Override
    public TransactionOptions build() {
      return new TransactionOptions(
          executor,
          TransactionOptionsType.READ_WRITE,
          null,
          new ReadWriteOptions(numberOfAttempts));
    }
  }

  public enum TransactionOptionsType {
    READ_ONLY,
    READ_WRITE
  }

  public static final class ReadOnlyOptions implements ToProtoBuilder<ReadOnly.Builder> {
    @Nullable private final Timestamp readTime;

    private ReadOnlyOptions(@Nullable Timestamp readTime) {
      this.readTime = readTime;
    }

    @Override
    public ReadOnly.Builder toProtoBuilder() {
      if (readTime != null) {
        return ReadOnly.getDefaultInstance().toBuilder().setReadTime(readTime);
      } else {
        return ReadOnly.getDefaultInstance().toBuilder();
      }
    }

    @Nullable
    public Timestamp getReadTime() {
      return readTime;
    }
  }

  public static final class ReadWriteOptions implements ToProtoBuilder<ReadWrite.Builder> {
    private final int numberOfAttempts;

    private ReadWriteOptions(int numberOfAttempts) {
      this.numberOfAttempts = numberOfAttempts;
    }

    @Override
    public ReadWrite.Builder toProtoBuilder() {
      return ReadWrite.getDefaultInstance().toBuilder();
    }

    public int getNumberOfAttempts() {
      return numberOfAttempts;
    }
  }

  interface ToProtoBuilder<ProtoBuilder> {
    ProtoBuilder toProtoBuilder();
  }
}
