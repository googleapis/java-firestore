/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.firestore.v1.stub;

import static com.google.cloud.firestore.v1.FirestoreAdminClient.ListFieldsPagedResponse;
import static com.google.cloud.firestore.v1.FirestoreAdminClient.ListIndexesPagedResponse;

import com.google.api.core.ApiFunction;
import com.google.api.core.ApiFuture;
import com.google.api.core.BetaApi;
import com.google.api.gax.core.GaxProperties;
import com.google.api.gax.core.GoogleCredentialsProvider;
import com.google.api.gax.core.InstantiatingExecutorProvider;
import com.google.api.gax.grpc.GaxGrpcProperties;
import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.grpc.InstantiatingGrpcChannelProvider;
import com.google.api.gax.grpc.ProtoOperationTransformers;
import com.google.api.gax.longrunning.OperationSnapshot;
import com.google.api.gax.longrunning.OperationTimedPollAlgorithm;
import com.google.api.gax.retrying.RetrySettings;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ApiClientHeaderProvider;
import com.google.api.gax.rpc.ClientContext;
import com.google.api.gax.rpc.OperationCallSettings;
import com.google.api.gax.rpc.PageContext;
import com.google.api.gax.rpc.PagedCallSettings;
import com.google.api.gax.rpc.PagedListDescriptor;
import com.google.api.gax.rpc.PagedListResponseFactory;
import com.google.api.gax.rpc.StatusCode;
import com.google.api.gax.rpc.StubSettings;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.api.gax.rpc.UnaryCallSettings;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.firestore.admin.v1.CreateIndexRequest;
import com.google.firestore.admin.v1.DeleteIndexRequest;
import com.google.firestore.admin.v1.ExportDocumentsMetadata;
import com.google.firestore.admin.v1.ExportDocumentsRequest;
import com.google.firestore.admin.v1.ExportDocumentsResponse;
import com.google.firestore.admin.v1.Field;
import com.google.firestore.admin.v1.FieldOperationMetadata;
import com.google.firestore.admin.v1.GetFieldRequest;
import com.google.firestore.admin.v1.GetIndexRequest;
import com.google.firestore.admin.v1.ImportDocumentsMetadata;
import com.google.firestore.admin.v1.ImportDocumentsRequest;
import com.google.firestore.admin.v1.Index;
import com.google.firestore.admin.v1.IndexOperationMetadata;
import com.google.firestore.admin.v1.ListFieldsRequest;
import com.google.firestore.admin.v1.ListFieldsResponse;
import com.google.firestore.admin.v1.ListIndexesRequest;
import com.google.firestore.admin.v1.ListIndexesResponse;
import com.google.firestore.admin.v1.UpdateFieldRequest;
import com.google.longrunning.Operation;
import com.google.protobuf.Empty;
import java.io.IOException;
import java.util.List;
import javax.annotation.Generated;
import org.threeten.bp.Duration;

// AUTO-GENERATED DOCUMENTATION AND CLASS.
/**
 * Settings class to configure an instance of {@link FirestoreAdminStub}.
 *
 * <p>The default instance has everything set to sensible defaults:
 *
 * <ul>
 *   <li>The default service address (firestore.googleapis.com) and default port (443) are used.
 *   <li>Credentials are acquired automatically through Application Default Credentials.
 *   <li>Retries are configured for idempotent methods but not for non-idempotent methods.
 * </ul>
 *
 * <p>The builder of this class is recursive, so contained classes are themselves builders. When
 * build() is called, the tree of builders is called to create the complete settings object.
 *
 * <p>For example, to set the total timeout of getIndex to 30 seconds:
 *
 * <pre>{@code
 * FirestoreAdminStubSettings.Builder firestoreAdminSettingsBuilder =
 *     FirestoreAdminStubSettings.newBuilder();
 * firestoreAdminSettingsBuilder
 *     .getIndexSettings()
 *     .setRetrySettings(
 *         firestoreAdminSettingsBuilder
 *             .getIndexSettings()
 *             .getRetrySettings()
 *             .toBuilder()
 *             .setTotalTimeout(Duration.ofSeconds(30))
 *             .build());
 * FirestoreAdminStubSettings firestoreAdminSettings = firestoreAdminSettingsBuilder.build();
 * }</pre>
 */
@Generated("by gapic-generator-java")
public class FirestoreAdminStubSettings extends StubSettings<FirestoreAdminStubSettings> {
  /** The default scopes of the service. */
  private static final ImmutableList<String> DEFAULT_SERVICE_SCOPES =
      ImmutableList.<String>builder()
          .add("https://www.googleapis.com/auth/cloud-platform")
          .add("https://www.googleapis.com/auth/datastore")
          .build();

  private final UnaryCallSettings<CreateIndexRequest, Operation> createIndexSettings;
  private final OperationCallSettings<CreateIndexRequest, Index, IndexOperationMetadata>
      createIndexOperationSettings;
  private final PagedCallSettings<ListIndexesRequest, ListIndexesResponse, ListIndexesPagedResponse>
      listIndexesSettings;
  private final UnaryCallSettings<GetIndexRequest, Index> getIndexSettings;
  private final UnaryCallSettings<DeleteIndexRequest, Empty> deleteIndexSettings;
  private final UnaryCallSettings<GetFieldRequest, Field> getFieldSettings;
  private final UnaryCallSettings<UpdateFieldRequest, Operation> updateFieldSettings;
  private final OperationCallSettings<UpdateFieldRequest, Field, FieldOperationMetadata>
      updateFieldOperationSettings;
  private final PagedCallSettings<ListFieldsRequest, ListFieldsResponse, ListFieldsPagedResponse>
      listFieldsSettings;
  private final UnaryCallSettings<ExportDocumentsRequest, Operation> exportDocumentsSettings;
  private final OperationCallSettings<
          ExportDocumentsRequest, ExportDocumentsResponse, ExportDocumentsMetadata>
      exportDocumentsOperationSettings;
  private final UnaryCallSettings<ImportDocumentsRequest, Operation> importDocumentsSettings;
  private final OperationCallSettings<ImportDocumentsRequest, Empty, ImportDocumentsMetadata>
      importDocumentsOperationSettings;

  private static final PagedListDescriptor<ListIndexesRequest, ListIndexesResponse, Index>
      LIST_INDEXES_PAGE_STR_DESC =
          new PagedListDescriptor<ListIndexesRequest, ListIndexesResponse, Index>() {
            @Override
            public String emptyToken() {
              return "";
            }

            @Override
            public ListIndexesRequest injectToken(ListIndexesRequest payload, String token) {
              return ListIndexesRequest.newBuilder(payload).setPageToken(token).build();
            }

            @Override
            public ListIndexesRequest injectPageSize(ListIndexesRequest payload, int pageSize) {
              return ListIndexesRequest.newBuilder(payload).setPageSize(pageSize).build();
            }

            @Override
            public Integer extractPageSize(ListIndexesRequest payload) {
              return payload.getPageSize();
            }

            @Override
            public String extractNextToken(ListIndexesResponse payload) {
              return payload.getNextPageToken();
            }

            @Override
            public Iterable<Index> extractResources(ListIndexesResponse payload) {
              return payload.getIndexesList() == null
                  ? ImmutableList.<Index>of()
                  : payload.getIndexesList();
            }
          };

  private static final PagedListDescriptor<ListFieldsRequest, ListFieldsResponse, Field>
      LIST_FIELDS_PAGE_STR_DESC =
          new PagedListDescriptor<ListFieldsRequest, ListFieldsResponse, Field>() {
            @Override
            public String emptyToken() {
              return "";
            }

            @Override
            public ListFieldsRequest injectToken(ListFieldsRequest payload, String token) {
              return ListFieldsRequest.newBuilder(payload).setPageToken(token).build();
            }

            @Override
            public ListFieldsRequest injectPageSize(ListFieldsRequest payload, int pageSize) {
              return ListFieldsRequest.newBuilder(payload).setPageSize(pageSize).build();
            }

            @Override
            public Integer extractPageSize(ListFieldsRequest payload) {
              return payload.getPageSize();
            }

            @Override
            public String extractNextToken(ListFieldsResponse payload) {
              return payload.getNextPageToken();
            }

            @Override
            public Iterable<Field> extractResources(ListFieldsResponse payload) {
              return payload.getFieldsList() == null
                  ? ImmutableList.<Field>of()
                  : payload.getFieldsList();
            }
          };

  private static final PagedListResponseFactory<
          ListIndexesRequest, ListIndexesResponse, ListIndexesPagedResponse>
      LIST_INDEXES_PAGE_STR_FACT =
          new PagedListResponseFactory<
              ListIndexesRequest, ListIndexesResponse, ListIndexesPagedResponse>() {
            @Override
            public ApiFuture<ListIndexesPagedResponse> getFuturePagedResponse(
                UnaryCallable<ListIndexesRequest, ListIndexesResponse> callable,
                ListIndexesRequest request,
                ApiCallContext context,
                ApiFuture<ListIndexesResponse> futureResponse) {
              PageContext<ListIndexesRequest, ListIndexesResponse, Index> pageContext =
                  PageContext.create(callable, LIST_INDEXES_PAGE_STR_DESC, request, context);
              return ListIndexesPagedResponse.createAsync(pageContext, futureResponse);
            }
          };

  private static final PagedListResponseFactory<
          ListFieldsRequest, ListFieldsResponse, ListFieldsPagedResponse>
      LIST_FIELDS_PAGE_STR_FACT =
          new PagedListResponseFactory<
              ListFieldsRequest, ListFieldsResponse, ListFieldsPagedResponse>() {
            @Override
            public ApiFuture<ListFieldsPagedResponse> getFuturePagedResponse(
                UnaryCallable<ListFieldsRequest, ListFieldsResponse> callable,
                ListFieldsRequest request,
                ApiCallContext context,
                ApiFuture<ListFieldsResponse> futureResponse) {
              PageContext<ListFieldsRequest, ListFieldsResponse, Field> pageContext =
                  PageContext.create(callable, LIST_FIELDS_PAGE_STR_DESC, request, context);
              return ListFieldsPagedResponse.createAsync(pageContext, futureResponse);
            }
          };

  /** Returns the object with the settings used for calls to createIndex. */
  public UnaryCallSettings<CreateIndexRequest, Operation> createIndexSettings() {
    return createIndexSettings;
  }

  /** Returns the object with the settings used for calls to createIndex. */
  public OperationCallSettings<CreateIndexRequest, Index, IndexOperationMetadata>
      createIndexOperationSettings() {
    return createIndexOperationSettings;
  }

  /** Returns the object with the settings used for calls to listIndexes. */
  public PagedCallSettings<ListIndexesRequest, ListIndexesResponse, ListIndexesPagedResponse>
      listIndexesSettings() {
    return listIndexesSettings;
  }

  /** Returns the object with the settings used for calls to getIndex. */
  public UnaryCallSettings<GetIndexRequest, Index> getIndexSettings() {
    return getIndexSettings;
  }

  /** Returns the object with the settings used for calls to deleteIndex. */
  public UnaryCallSettings<DeleteIndexRequest, Empty> deleteIndexSettings() {
    return deleteIndexSettings;
  }

  /** Returns the object with the settings used for calls to getField. */
  public UnaryCallSettings<GetFieldRequest, Field> getFieldSettings() {
    return getFieldSettings;
  }

  /** Returns the object with the settings used for calls to updateField. */
  public UnaryCallSettings<UpdateFieldRequest, Operation> updateFieldSettings() {
    return updateFieldSettings;
  }

  /** Returns the object with the settings used for calls to updateField. */
  public OperationCallSettings<UpdateFieldRequest, Field, FieldOperationMetadata>
      updateFieldOperationSettings() {
    return updateFieldOperationSettings;
  }

  /** Returns the object with the settings used for calls to listFields. */
  public PagedCallSettings<ListFieldsRequest, ListFieldsResponse, ListFieldsPagedResponse>
      listFieldsSettings() {
    return listFieldsSettings;
  }

  /** Returns the object with the settings used for calls to exportDocuments. */
  public UnaryCallSettings<ExportDocumentsRequest, Operation> exportDocumentsSettings() {
    return exportDocumentsSettings;
  }

  /** Returns the object with the settings used for calls to exportDocuments. */
  public OperationCallSettings<
          ExportDocumentsRequest, ExportDocumentsResponse, ExportDocumentsMetadata>
      exportDocumentsOperationSettings() {
    return exportDocumentsOperationSettings;
  }

  /** Returns the object with the settings used for calls to importDocuments. */
  public UnaryCallSettings<ImportDocumentsRequest, Operation> importDocumentsSettings() {
    return importDocumentsSettings;
  }

  /** Returns the object with the settings used for calls to importDocuments. */
  public OperationCallSettings<ImportDocumentsRequest, Empty, ImportDocumentsMetadata>
      importDocumentsOperationSettings() {
    return importDocumentsOperationSettings;
  }

  @BetaApi("A restructuring of stub classes is planned, so this may break in the future")
  public FirestoreAdminStub createStub() throws IOException {
    if (getTransportChannelProvider()
        .getTransportName()
        .equals(GrpcTransportChannel.getGrpcTransportName())) {
      return GrpcFirestoreAdminStub.create(this);
    }
    throw new UnsupportedOperationException(
        String.format(
            "Transport not supported: %s", getTransportChannelProvider().getTransportName()));
  }

  /** Returns a builder for the default ExecutorProvider for this service. */
  public static InstantiatingExecutorProvider.Builder defaultExecutorProviderBuilder() {
    return InstantiatingExecutorProvider.newBuilder();
  }

  /** Returns the default service endpoint. */
  public static String getDefaultEndpoint() {
    return "firestore.googleapis.com:443";
  }

  /** Returns the default service scopes. */
  public static List<String> getDefaultServiceScopes() {
    return DEFAULT_SERVICE_SCOPES;
  }

  /** Returns a builder for the default credentials for this service. */
  public static GoogleCredentialsProvider.Builder defaultCredentialsProviderBuilder() {
    return GoogleCredentialsProvider.newBuilder().setScopesToApply(DEFAULT_SERVICE_SCOPES);
  }

  /** Returns a builder for the default ChannelProvider for this service. */
  public static InstantiatingGrpcChannelProvider.Builder defaultGrpcTransportProviderBuilder() {
    return InstantiatingGrpcChannelProvider.newBuilder()
        .setMaxInboundMessageSize(Integer.MAX_VALUE);
  }

  public static TransportChannelProvider defaultTransportChannelProvider() {
    return defaultGrpcTransportProviderBuilder().build();
  }

  @BetaApi("The surface for customizing headers is not stable yet and may change in the future.")
  public static ApiClientHeaderProvider.Builder defaultApiClientHeaderProviderBuilder() {
    return ApiClientHeaderProvider.newBuilder()
        .setGeneratedLibToken(
            "gapic", GaxProperties.getLibraryVersion(FirestoreAdminStubSettings.class))
        .setTransportToken(
            GaxGrpcProperties.getGrpcTokenName(), GaxGrpcProperties.getGrpcVersion());
  }

  /** Returns a new builder for this class. */
  public static Builder newBuilder() {
    return Builder.createDefault();
  }

  /** Returns a new builder for this class. */
  public static Builder newBuilder(ClientContext clientContext) {
    return new Builder(clientContext);
  }

  /** Returns a builder containing all the values of this settings class. */
  public Builder toBuilder() {
    return new Builder(this);
  }

  protected FirestoreAdminStubSettings(Builder settingsBuilder) throws IOException {
    super(settingsBuilder);

    createIndexSettings = settingsBuilder.createIndexSettings().build();
    createIndexOperationSettings = settingsBuilder.createIndexOperationSettings().build();
    listIndexesSettings = settingsBuilder.listIndexesSettings().build();
    getIndexSettings = settingsBuilder.getIndexSettings().build();
    deleteIndexSettings = settingsBuilder.deleteIndexSettings().build();
    getFieldSettings = settingsBuilder.getFieldSettings().build();
    updateFieldSettings = settingsBuilder.updateFieldSettings().build();
    updateFieldOperationSettings = settingsBuilder.updateFieldOperationSettings().build();
    listFieldsSettings = settingsBuilder.listFieldsSettings().build();
    exportDocumentsSettings = settingsBuilder.exportDocumentsSettings().build();
    exportDocumentsOperationSettings = settingsBuilder.exportDocumentsOperationSettings().build();
    importDocumentsSettings = settingsBuilder.importDocumentsSettings().build();
    importDocumentsOperationSettings = settingsBuilder.importDocumentsOperationSettings().build();
  }

  /** Builder for FirestoreAdminStubSettings. */
  public static class Builder extends StubSettings.Builder<FirestoreAdminStubSettings, Builder> {
    private final ImmutableList<UnaryCallSettings.Builder<?, ?>> unaryMethodSettingsBuilders;
    private final UnaryCallSettings.Builder<CreateIndexRequest, Operation> createIndexSettings;
    private final OperationCallSettings.Builder<CreateIndexRequest, Index, IndexOperationMetadata>
        createIndexOperationSettings;
    private final PagedCallSettings.Builder<
            ListIndexesRequest, ListIndexesResponse, ListIndexesPagedResponse>
        listIndexesSettings;
    private final UnaryCallSettings.Builder<GetIndexRequest, Index> getIndexSettings;
    private final UnaryCallSettings.Builder<DeleteIndexRequest, Empty> deleteIndexSettings;
    private final UnaryCallSettings.Builder<GetFieldRequest, Field> getFieldSettings;
    private final UnaryCallSettings.Builder<UpdateFieldRequest, Operation> updateFieldSettings;
    private final OperationCallSettings.Builder<UpdateFieldRequest, Field, FieldOperationMetadata>
        updateFieldOperationSettings;
    private final PagedCallSettings.Builder<
            ListFieldsRequest, ListFieldsResponse, ListFieldsPagedResponse>
        listFieldsSettings;
    private final UnaryCallSettings.Builder<ExportDocumentsRequest, Operation>
        exportDocumentsSettings;
    private final OperationCallSettings.Builder<
            ExportDocumentsRequest, ExportDocumentsResponse, ExportDocumentsMetadata>
        exportDocumentsOperationSettings;
    private final UnaryCallSettings.Builder<ImportDocumentsRequest, Operation>
        importDocumentsSettings;
    private final OperationCallSettings.Builder<
            ImportDocumentsRequest, Empty, ImportDocumentsMetadata>
        importDocumentsOperationSettings;
    private static final ImmutableMap<String, ImmutableSet<StatusCode.Code>>
        RETRYABLE_CODE_DEFINITIONS;

    static {
      ImmutableMap.Builder<String, ImmutableSet<StatusCode.Code>> definitions =
          ImmutableMap.builder();
      definitions.put(
          "no_retry_1_codes", ImmutableSet.copyOf(Lists.<StatusCode.Code>newArrayList()));
      definitions.put(
          "retry_policy_0_codes",
          ImmutableSet.copyOf(
              Lists.<StatusCode.Code>newArrayList(
                  StatusCode.Code.UNAVAILABLE,
                  StatusCode.Code.INTERNAL,
                  StatusCode.Code.DEADLINE_EXCEEDED)));
      RETRYABLE_CODE_DEFINITIONS = definitions.build();
    }

    private static final ImmutableMap<String, RetrySettings> RETRY_PARAM_DEFINITIONS;

    static {
      ImmutableMap.Builder<String, RetrySettings> definitions = ImmutableMap.builder();
      RetrySettings settings = null;
      settings =
          RetrySettings.newBuilder()
              .setInitialRpcTimeout(Duration.ofMillis(60000L))
              .setRpcTimeoutMultiplier(1.0)
              .setMaxRpcTimeout(Duration.ofMillis(60000L))
              .setTotalTimeout(Duration.ofMillis(60000L))
              .build();
      definitions.put("no_retry_1_params", settings);
      settings =
          RetrySettings.newBuilder()
              .setInitialRetryDelay(Duration.ofMillis(100L))
              .setRetryDelayMultiplier(1.3)
              .setMaxRetryDelay(Duration.ofMillis(60000L))
              .setInitialRpcTimeout(Duration.ofMillis(60000L))
              .setRpcTimeoutMultiplier(1.0)
              .setMaxRpcTimeout(Duration.ofMillis(60000L))
              .setTotalTimeout(Duration.ofMillis(60000L))
              .build();
      definitions.put("retry_policy_0_params", settings);
      RETRY_PARAM_DEFINITIONS = definitions.build();
    }

    protected Builder() {
      this(((ClientContext) null));
    }

    protected Builder(ClientContext clientContext) {
      super(clientContext);

      createIndexSettings = UnaryCallSettings.newUnaryCallSettingsBuilder();
      createIndexOperationSettings = OperationCallSettings.newBuilder();
      listIndexesSettings = PagedCallSettings.newBuilder(LIST_INDEXES_PAGE_STR_FACT);
      getIndexSettings = UnaryCallSettings.newUnaryCallSettingsBuilder();
      deleteIndexSettings = UnaryCallSettings.newUnaryCallSettingsBuilder();
      getFieldSettings = UnaryCallSettings.newUnaryCallSettingsBuilder();
      updateFieldSettings = UnaryCallSettings.newUnaryCallSettingsBuilder();
      updateFieldOperationSettings = OperationCallSettings.newBuilder();
      listFieldsSettings = PagedCallSettings.newBuilder(LIST_FIELDS_PAGE_STR_FACT);
      exportDocumentsSettings = UnaryCallSettings.newUnaryCallSettingsBuilder();
      exportDocumentsOperationSettings = OperationCallSettings.newBuilder();
      importDocumentsSettings = UnaryCallSettings.newUnaryCallSettingsBuilder();
      importDocumentsOperationSettings = OperationCallSettings.newBuilder();

      unaryMethodSettingsBuilders =
          ImmutableList.<UnaryCallSettings.Builder<?, ?>>of(
              createIndexSettings,
              listIndexesSettings,
              getIndexSettings,
              deleteIndexSettings,
              getFieldSettings,
              updateFieldSettings,
              listFieldsSettings,
              exportDocumentsSettings,
              importDocumentsSettings);
      initDefaults(this);
    }

    protected Builder(FirestoreAdminStubSettings settings) {
      super(settings);

      createIndexSettings = settings.createIndexSettings.toBuilder();
      createIndexOperationSettings = settings.createIndexOperationSettings.toBuilder();
      listIndexesSettings = settings.listIndexesSettings.toBuilder();
      getIndexSettings = settings.getIndexSettings.toBuilder();
      deleteIndexSettings = settings.deleteIndexSettings.toBuilder();
      getFieldSettings = settings.getFieldSettings.toBuilder();
      updateFieldSettings = settings.updateFieldSettings.toBuilder();
      updateFieldOperationSettings = settings.updateFieldOperationSettings.toBuilder();
      listFieldsSettings = settings.listFieldsSettings.toBuilder();
      exportDocumentsSettings = settings.exportDocumentsSettings.toBuilder();
      exportDocumentsOperationSettings = settings.exportDocumentsOperationSettings.toBuilder();
      importDocumentsSettings = settings.importDocumentsSettings.toBuilder();
      importDocumentsOperationSettings = settings.importDocumentsOperationSettings.toBuilder();

      unaryMethodSettingsBuilders =
          ImmutableList.<UnaryCallSettings.Builder<?, ?>>of(
              createIndexSettings,
              listIndexesSettings,
              getIndexSettings,
              deleteIndexSettings,
              getFieldSettings,
              updateFieldSettings,
              listFieldsSettings,
              exportDocumentsSettings,
              importDocumentsSettings);
    }

    private static Builder createDefault() {
      Builder builder = new Builder(((ClientContext) null));

      builder.setTransportChannelProvider(defaultTransportChannelProvider());
      builder.setCredentialsProvider(defaultCredentialsProviderBuilder().build());
      builder.setInternalHeaderProvider(defaultApiClientHeaderProviderBuilder().build());
      builder.setEndpoint(getDefaultEndpoint());

      return initDefaults(builder);
    }

    private static Builder initDefaults(Builder builder) {
      builder
          .createIndexSettings()
          .setRetryableCodes(RETRYABLE_CODE_DEFINITIONS.get("no_retry_1_codes"))
          .setRetrySettings(RETRY_PARAM_DEFINITIONS.get("no_retry_1_params"));

      builder
          .listIndexesSettings()
          .setRetryableCodes(RETRYABLE_CODE_DEFINITIONS.get("retry_policy_0_codes"))
          .setRetrySettings(RETRY_PARAM_DEFINITIONS.get("retry_policy_0_params"));

      builder
          .getIndexSettings()
          .setRetryableCodes(RETRYABLE_CODE_DEFINITIONS.get("retry_policy_0_codes"))
          .setRetrySettings(RETRY_PARAM_DEFINITIONS.get("retry_policy_0_params"));

      builder
          .deleteIndexSettings()
          .setRetryableCodes(RETRYABLE_CODE_DEFINITIONS.get("retry_policy_0_codes"))
          .setRetrySettings(RETRY_PARAM_DEFINITIONS.get("retry_policy_0_params"));

      builder
          .getFieldSettings()
          .setRetryableCodes(RETRYABLE_CODE_DEFINITIONS.get("retry_policy_0_codes"))
          .setRetrySettings(RETRY_PARAM_DEFINITIONS.get("retry_policy_0_params"));

      builder
          .updateFieldSettings()
          .setRetryableCodes(RETRYABLE_CODE_DEFINITIONS.get("no_retry_1_codes"))
          .setRetrySettings(RETRY_PARAM_DEFINITIONS.get("no_retry_1_params"));

      builder
          .listFieldsSettings()
          .setRetryableCodes(RETRYABLE_CODE_DEFINITIONS.get("retry_policy_0_codes"))
          .setRetrySettings(RETRY_PARAM_DEFINITIONS.get("retry_policy_0_params"));

      builder
          .exportDocumentsSettings()
          .setRetryableCodes(RETRYABLE_CODE_DEFINITIONS.get("no_retry_1_codes"))
          .setRetrySettings(RETRY_PARAM_DEFINITIONS.get("no_retry_1_params"));

      builder
          .importDocumentsSettings()
          .setRetryableCodes(RETRYABLE_CODE_DEFINITIONS.get("no_retry_1_codes"))
          .setRetrySettings(RETRY_PARAM_DEFINITIONS.get("no_retry_1_params"));

      builder
          .createIndexOperationSettings()
          .setInitialCallSettings(
              UnaryCallSettings.<CreateIndexRequest, OperationSnapshot>newUnaryCallSettingsBuilder()
                  .setRetryableCodes(RETRYABLE_CODE_DEFINITIONS.get("no_retry_1_codes"))
                  .setRetrySettings(RETRY_PARAM_DEFINITIONS.get("no_retry_1_params"))
                  .build())
          .setResponseTransformer(
              ProtoOperationTransformers.ResponseTransformer.create(Index.class))
          .setMetadataTransformer(
              ProtoOperationTransformers.MetadataTransformer.create(IndexOperationMetadata.class))
          .setPollingAlgorithm(
              OperationTimedPollAlgorithm.create(
                  RetrySettings.newBuilder()
                      .setInitialRetryDelay(Duration.ofMillis(5000L))
                      .setRetryDelayMultiplier(1.5)
                      .setMaxRetryDelay(Duration.ofMillis(45000L))
                      .setInitialRpcTimeout(Duration.ZERO)
                      .setRpcTimeoutMultiplier(1.0)
                      .setMaxRpcTimeout(Duration.ZERO)
                      .setTotalTimeout(Duration.ofMillis(300000L))
                      .build()));

      builder
          .updateFieldOperationSettings()
          .setInitialCallSettings(
              UnaryCallSettings.<UpdateFieldRequest, OperationSnapshot>newUnaryCallSettingsBuilder()
                  .setRetryableCodes(RETRYABLE_CODE_DEFINITIONS.get("no_retry_1_codes"))
                  .setRetrySettings(RETRY_PARAM_DEFINITIONS.get("no_retry_1_params"))
                  .build())
          .setResponseTransformer(
              ProtoOperationTransformers.ResponseTransformer.create(Field.class))
          .setMetadataTransformer(
              ProtoOperationTransformers.MetadataTransformer.create(FieldOperationMetadata.class))
          .setPollingAlgorithm(
              OperationTimedPollAlgorithm.create(
                  RetrySettings.newBuilder()
                      .setInitialRetryDelay(Duration.ofMillis(5000L))
                      .setRetryDelayMultiplier(1.5)
                      .setMaxRetryDelay(Duration.ofMillis(45000L))
                      .setInitialRpcTimeout(Duration.ZERO)
                      .setRpcTimeoutMultiplier(1.0)
                      .setMaxRpcTimeout(Duration.ZERO)
                      .setTotalTimeout(Duration.ofMillis(300000L))
                      .build()));

      builder
          .exportDocumentsOperationSettings()
          .setInitialCallSettings(
              UnaryCallSettings
                  .<ExportDocumentsRequest, OperationSnapshot>newUnaryCallSettingsBuilder()
                  .setRetryableCodes(RETRYABLE_CODE_DEFINITIONS.get("no_retry_1_codes"))
                  .setRetrySettings(RETRY_PARAM_DEFINITIONS.get("no_retry_1_params"))
                  .build())
          .setResponseTransformer(
              ProtoOperationTransformers.ResponseTransformer.create(ExportDocumentsResponse.class))
          .setMetadataTransformer(
              ProtoOperationTransformers.MetadataTransformer.create(ExportDocumentsMetadata.class))
          .setPollingAlgorithm(
              OperationTimedPollAlgorithm.create(
                  RetrySettings.newBuilder()
                      .setInitialRetryDelay(Duration.ofMillis(5000L))
                      .setRetryDelayMultiplier(1.5)
                      .setMaxRetryDelay(Duration.ofMillis(45000L))
                      .setInitialRpcTimeout(Duration.ZERO)
                      .setRpcTimeoutMultiplier(1.0)
                      .setMaxRpcTimeout(Duration.ZERO)
                      .setTotalTimeout(Duration.ofMillis(300000L))
                      .build()));

      builder
          .importDocumentsOperationSettings()
          .setInitialCallSettings(
              UnaryCallSettings
                  .<ImportDocumentsRequest, OperationSnapshot>newUnaryCallSettingsBuilder()
                  .setRetryableCodes(RETRYABLE_CODE_DEFINITIONS.get("no_retry_1_codes"))
                  .setRetrySettings(RETRY_PARAM_DEFINITIONS.get("no_retry_1_params"))
                  .build())
          .setResponseTransformer(
              ProtoOperationTransformers.ResponseTransformer.create(Empty.class))
          .setMetadataTransformer(
              ProtoOperationTransformers.MetadataTransformer.create(ImportDocumentsMetadata.class))
          .setPollingAlgorithm(
              OperationTimedPollAlgorithm.create(
                  RetrySettings.newBuilder()
                      .setInitialRetryDelay(Duration.ofMillis(5000L))
                      .setRetryDelayMultiplier(1.5)
                      .setMaxRetryDelay(Duration.ofMillis(45000L))
                      .setInitialRpcTimeout(Duration.ZERO)
                      .setRpcTimeoutMultiplier(1.0)
                      .setMaxRpcTimeout(Duration.ZERO)
                      .setTotalTimeout(Duration.ofMillis(300000L))
                      .build()));

      return builder;
    }

    // NEXT_MAJOR_VER: remove 'throws Exception'.
    /**
     * Applies the given settings updater function to all of the unary API methods in this service.
     *
     * <p>Note: This method does not support applying settings to streaming methods.
     */
    public Builder applyToAllUnaryMethods(
        ApiFunction<UnaryCallSettings.Builder<?, ?>, Void> settingsUpdater) throws Exception {
      super.applyToAllUnaryMethods(unaryMethodSettingsBuilders, settingsUpdater);
      return this;
    }

    public ImmutableList<UnaryCallSettings.Builder<?, ?>> unaryMethodSettingsBuilders() {
      return unaryMethodSettingsBuilders;
    }

    /** Returns the builder for the settings used for calls to createIndex. */
    public UnaryCallSettings.Builder<CreateIndexRequest, Operation> createIndexSettings() {
      return createIndexSettings;
    }

    /** Returns the builder for the settings used for calls to createIndex. */
    @BetaApi(
        "The surface for use by generated code is not stable yet and may change in the future.")
    public OperationCallSettings.Builder<CreateIndexRequest, Index, IndexOperationMetadata>
        createIndexOperationSettings() {
      return createIndexOperationSettings;
    }

    /** Returns the builder for the settings used for calls to listIndexes. */
    public PagedCallSettings.Builder<
            ListIndexesRequest, ListIndexesResponse, ListIndexesPagedResponse>
        listIndexesSettings() {
      return listIndexesSettings;
    }

    /** Returns the builder for the settings used for calls to getIndex. */
    public UnaryCallSettings.Builder<GetIndexRequest, Index> getIndexSettings() {
      return getIndexSettings;
    }

    /** Returns the builder for the settings used for calls to deleteIndex. */
    public UnaryCallSettings.Builder<DeleteIndexRequest, Empty> deleteIndexSettings() {
      return deleteIndexSettings;
    }

    /** Returns the builder for the settings used for calls to getField. */
    public UnaryCallSettings.Builder<GetFieldRequest, Field> getFieldSettings() {
      return getFieldSettings;
    }

    /** Returns the builder for the settings used for calls to updateField. */
    public UnaryCallSettings.Builder<UpdateFieldRequest, Operation> updateFieldSettings() {
      return updateFieldSettings;
    }

    /** Returns the builder for the settings used for calls to updateField. */
    @BetaApi(
        "The surface for use by generated code is not stable yet and may change in the future.")
    public OperationCallSettings.Builder<UpdateFieldRequest, Field, FieldOperationMetadata>
        updateFieldOperationSettings() {
      return updateFieldOperationSettings;
    }

    /** Returns the builder for the settings used for calls to listFields. */
    public PagedCallSettings.Builder<ListFieldsRequest, ListFieldsResponse, ListFieldsPagedResponse>
        listFieldsSettings() {
      return listFieldsSettings;
    }

    /** Returns the builder for the settings used for calls to exportDocuments. */
    public UnaryCallSettings.Builder<ExportDocumentsRequest, Operation> exportDocumentsSettings() {
      return exportDocumentsSettings;
    }

    /** Returns the builder for the settings used for calls to exportDocuments. */
    @BetaApi(
        "The surface for use by generated code is not stable yet and may change in the future.")
    public OperationCallSettings.Builder<
            ExportDocumentsRequest, ExportDocumentsResponse, ExportDocumentsMetadata>
        exportDocumentsOperationSettings() {
      return exportDocumentsOperationSettings;
    }

    /** Returns the builder for the settings used for calls to importDocuments. */
    public UnaryCallSettings.Builder<ImportDocumentsRequest, Operation> importDocumentsSettings() {
      return importDocumentsSettings;
    }

    /** Returns the builder for the settings used for calls to importDocuments. */
    @BetaApi(
        "The surface for use by generated code is not stable yet and may change in the future.")
    public OperationCallSettings.Builder<ImportDocumentsRequest, Empty, ImportDocumentsMetadata>
        importDocumentsOperationSettings() {
      return importDocumentsOperationSettings;
    }

    @Override
    public FirestoreAdminStubSettings build() throws IOException {
      return new FirestoreAdminStubSettings(this);
    }
  }
}
