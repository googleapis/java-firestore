/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.cloud.firestore.v1;

import static com.google.cloud.firestore.v1.FirestoreAdminClient.ListFieldsPagedResponse;
import static com.google.cloud.firestore.v1.FirestoreAdminClient.ListIndexesPagedResponse;

import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.grpc.GaxGrpcProperties;
import com.google.api.gax.grpc.testing.LocalChannelProvider;
import com.google.api.gax.grpc.testing.MockGrpcService;
import com.google.api.gax.grpc.testing.MockServiceHelper;
import com.google.api.gax.rpc.ApiClientHeaderProvider;
import com.google.api.gax.rpc.InvalidArgumentException;
import com.google.api.gax.rpc.StatusCode;
import com.google.common.collect.Lists;
import com.google.firestore.admin.v1.CollectionGroupName;
import com.google.firestore.admin.v1.CreateIndexRequest;
import com.google.firestore.admin.v1.DatabaseName;
import com.google.firestore.admin.v1.DeleteIndexRequest;
import com.google.firestore.admin.v1.ExportDocumentsRequest;
import com.google.firestore.admin.v1.ExportDocumentsResponse;
import com.google.firestore.admin.v1.Field;
import com.google.firestore.admin.v1.FieldName;
import com.google.firestore.admin.v1.GetFieldRequest;
import com.google.firestore.admin.v1.GetIndexRequest;
import com.google.firestore.admin.v1.ImportDocumentsRequest;
import com.google.firestore.admin.v1.Index;
import com.google.firestore.admin.v1.IndexName;
import com.google.firestore.admin.v1.ListFieldsRequest;
import com.google.firestore.admin.v1.ListFieldsResponse;
import com.google.firestore.admin.v1.ListIndexesRequest;
import com.google.firestore.admin.v1.ListIndexesResponse;
import com.google.firestore.admin.v1.UpdateFieldRequest;
import com.google.longrunning.Operation;
import com.google.protobuf.AbstractMessage;
import com.google.protobuf.Any;
import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

@javax.annotation.Generated("by GAPIC")
public class FirestoreAdminClientTest {
  private static MockFirestoreAdmin mockFirestoreAdmin;
  private static MockServiceHelper serviceHelper;
  private FirestoreAdminClient client;
  private LocalChannelProvider channelProvider;

  @BeforeClass
  public static void startStaticServer() {
    mockFirestoreAdmin = new MockFirestoreAdmin();
    serviceHelper =
        new MockServiceHelper(
            UUID.randomUUID().toString(), Arrays.<MockGrpcService>asList(mockFirestoreAdmin));
    serviceHelper.start();
  }

  @AfterClass
  public static void stopServer() {
    serviceHelper.stop();
  }

  @Before
  public void setUp() throws IOException {
    serviceHelper.reset();
    channelProvider = serviceHelper.createChannelProvider();
    FirestoreAdminSettings settings =
        FirestoreAdminSettings.newBuilder()
            .setTransportChannelProvider(channelProvider)
            .setCredentialsProvider(NoCredentialsProvider.create())
            .build();
    client = FirestoreAdminClient.create(settings);
  }

  @After
  public void tearDown() throws Exception {
    client.close();
  }

  @Test
  @SuppressWarnings("all")
  public void createIndexTest() throws Exception {
    IndexName name = IndexName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]", "[INDEX]");
    Index expectedResponse = Index.newBuilder().setName(name.toString()).build();
    Operation resultOperation =
        Operation.newBuilder()
            .setName("createIndexTest")
            .setDone(true)
            .setResponse(Any.pack(expectedResponse))
            .build();
    mockFirestoreAdmin.addResponse(resultOperation);

    CollectionGroupName parent = CollectionGroupName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]");
    Index index = Index.newBuilder().build();

    Index actualResponse = client.createIndexAsync(parent, index).get();
    Assert.assertEquals(expectedResponse, actualResponse);

    List<AbstractMessage> actualRequests = mockFirestoreAdmin.getRequests();
    Assert.assertEquals(1, actualRequests.size());
    CreateIndexRequest actualRequest = (CreateIndexRequest) actualRequests.get(0);

    Assert.assertEquals(parent, CollectionGroupName.parse(actualRequest.getParent()));
    Assert.assertEquals(index, actualRequest.getIndex());
    Assert.assertTrue(
        channelProvider.isHeaderSent(
            ApiClientHeaderProvider.getDefaultApiClientHeaderKey(),
            GaxGrpcProperties.getDefaultApiClientHeaderPattern()));
  }

  @Test
  @SuppressWarnings("all")
  public void createIndexExceptionTest() throws Exception {
    StatusRuntimeException exception = new StatusRuntimeException(Status.INVALID_ARGUMENT);
    mockFirestoreAdmin.addException(exception);

    try {
      CollectionGroupName parent =
          CollectionGroupName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]");
      Index index = Index.newBuilder().build();

      client.createIndexAsync(parent, index).get();
      Assert.fail("No exception raised");
    } catch (ExecutionException e) {
      Assert.assertEquals(InvalidArgumentException.class, e.getCause().getClass());
      InvalidArgumentException apiException = (InvalidArgumentException) e.getCause();
      Assert.assertEquals(StatusCode.Code.INVALID_ARGUMENT, apiException.getStatusCode().getCode());
    }
  }

  @Test
  @SuppressWarnings("all")
  public void listIndexesTest() {
    String nextPageToken = "";
    Index indexesElement = Index.newBuilder().build();
    List<Index> indexes = Arrays.asList(indexesElement);
    ListIndexesResponse expectedResponse =
        ListIndexesResponse.newBuilder()
            .setNextPageToken(nextPageToken)
            .addAllIndexes(indexes)
            .build();
    mockFirestoreAdmin.addResponse(expectedResponse);

    CollectionGroupName parent = CollectionGroupName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]");

    ListIndexesPagedResponse pagedListResponse = client.listIndexes(parent);

    List<Index> resources = Lists.newArrayList(pagedListResponse.iterateAll());
    Assert.assertEquals(1, resources.size());
    Assert.assertEquals(expectedResponse.getIndexesList().get(0), resources.get(0));

    List<AbstractMessage> actualRequests = mockFirestoreAdmin.getRequests();
    Assert.assertEquals(1, actualRequests.size());
    ListIndexesRequest actualRequest = (ListIndexesRequest) actualRequests.get(0);

    Assert.assertEquals(parent, CollectionGroupName.parse(actualRequest.getParent()));
    Assert.assertTrue(
        channelProvider.isHeaderSent(
            ApiClientHeaderProvider.getDefaultApiClientHeaderKey(),
            GaxGrpcProperties.getDefaultApiClientHeaderPattern()));
  }

  @Test
  @SuppressWarnings("all")
  public void listIndexesExceptionTest() throws Exception {
    StatusRuntimeException exception = new StatusRuntimeException(Status.INVALID_ARGUMENT);
    mockFirestoreAdmin.addException(exception);

    try {
      CollectionGroupName parent =
          CollectionGroupName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]");

      client.listIndexes(parent);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception
    }
  }

  @Test
  @SuppressWarnings("all")
  public void getIndexTest() {
    IndexName name2 = IndexName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]", "[INDEX]");
    Index expectedResponse = Index.newBuilder().setName(name2.toString()).build();
    mockFirestoreAdmin.addResponse(expectedResponse);

    IndexName name = IndexName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]", "[INDEX]");

    Index actualResponse = client.getIndex(name);
    Assert.assertEquals(expectedResponse, actualResponse);

    List<AbstractMessage> actualRequests = mockFirestoreAdmin.getRequests();
    Assert.assertEquals(1, actualRequests.size());
    GetIndexRequest actualRequest = (GetIndexRequest) actualRequests.get(0);

    Assert.assertEquals(name, IndexName.parse(actualRequest.getName()));
    Assert.assertTrue(
        channelProvider.isHeaderSent(
            ApiClientHeaderProvider.getDefaultApiClientHeaderKey(),
            GaxGrpcProperties.getDefaultApiClientHeaderPattern()));
  }

  @Test
  @SuppressWarnings("all")
  public void getIndexExceptionTest() throws Exception {
    StatusRuntimeException exception = new StatusRuntimeException(Status.INVALID_ARGUMENT);
    mockFirestoreAdmin.addException(exception);

    try {
      IndexName name = IndexName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]", "[INDEX]");

      client.getIndex(name);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception
    }
  }

  @Test
  @SuppressWarnings("all")
  public void deleteIndexTest() {
    Empty expectedResponse = Empty.newBuilder().build();
    mockFirestoreAdmin.addResponse(expectedResponse);

    IndexName name = IndexName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]", "[INDEX]");

    client.deleteIndex(name);

    List<AbstractMessage> actualRequests = mockFirestoreAdmin.getRequests();
    Assert.assertEquals(1, actualRequests.size());
    DeleteIndexRequest actualRequest = (DeleteIndexRequest) actualRequests.get(0);

    Assert.assertEquals(name, IndexName.parse(actualRequest.getName()));
    Assert.assertTrue(
        channelProvider.isHeaderSent(
            ApiClientHeaderProvider.getDefaultApiClientHeaderKey(),
            GaxGrpcProperties.getDefaultApiClientHeaderPattern()));
  }

  @Test
  @SuppressWarnings("all")
  public void deleteIndexExceptionTest() throws Exception {
    StatusRuntimeException exception = new StatusRuntimeException(Status.INVALID_ARGUMENT);
    mockFirestoreAdmin.addException(exception);

    try {
      IndexName name = IndexName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]", "[INDEX]");

      client.deleteIndex(name);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception
    }
  }

  @Test
  @SuppressWarnings("all")
  public void getFieldTest() {
    FieldName name2 = FieldName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]", "[FIELD]");
    Field expectedResponse = Field.newBuilder().setName(name2.toString()).build();
    mockFirestoreAdmin.addResponse(expectedResponse);

    FieldName name = FieldName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]", "[FIELD]");

    Field actualResponse = client.getField(name);
    Assert.assertEquals(expectedResponse, actualResponse);

    List<AbstractMessage> actualRequests = mockFirestoreAdmin.getRequests();
    Assert.assertEquals(1, actualRequests.size());
    GetFieldRequest actualRequest = (GetFieldRequest) actualRequests.get(0);

    Assert.assertEquals(name, FieldName.parse(actualRequest.getName()));
    Assert.assertTrue(
        channelProvider.isHeaderSent(
            ApiClientHeaderProvider.getDefaultApiClientHeaderKey(),
            GaxGrpcProperties.getDefaultApiClientHeaderPattern()));
  }

  @Test
  @SuppressWarnings("all")
  public void getFieldExceptionTest() throws Exception {
    StatusRuntimeException exception = new StatusRuntimeException(Status.INVALID_ARGUMENT);
    mockFirestoreAdmin.addException(exception);

    try {
      FieldName name = FieldName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]", "[FIELD]");

      client.getField(name);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception
    }
  }

  @Test
  @SuppressWarnings("all")
  public void updateFieldTest() throws Exception {
    FieldName name = FieldName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]", "[FIELD]");
    Field expectedResponse = Field.newBuilder().setName(name.toString()).build();
    Operation resultOperation =
        Operation.newBuilder()
            .setName("updateFieldTest")
            .setDone(true)
            .setResponse(Any.pack(expectedResponse))
            .build();
    mockFirestoreAdmin.addResponse(resultOperation);

    Field field = Field.newBuilder().build();

    Field actualResponse = client.updateFieldAsync(field).get();
    Assert.assertEquals(expectedResponse, actualResponse);

    List<AbstractMessage> actualRequests = mockFirestoreAdmin.getRequests();
    Assert.assertEquals(1, actualRequests.size());
    UpdateFieldRequest actualRequest = (UpdateFieldRequest) actualRequests.get(0);

    Assert.assertEquals(field, actualRequest.getField());
    Assert.assertTrue(
        channelProvider.isHeaderSent(
            ApiClientHeaderProvider.getDefaultApiClientHeaderKey(),
            GaxGrpcProperties.getDefaultApiClientHeaderPattern()));
  }

  @Test
  @SuppressWarnings("all")
  public void updateFieldExceptionTest() throws Exception {
    StatusRuntimeException exception = new StatusRuntimeException(Status.INVALID_ARGUMENT);
    mockFirestoreAdmin.addException(exception);

    try {
      Field field = Field.newBuilder().build();

      client.updateFieldAsync(field).get();
      Assert.fail("No exception raised");
    } catch (ExecutionException e) {
      Assert.assertEquals(InvalidArgumentException.class, e.getCause().getClass());
      InvalidArgumentException apiException = (InvalidArgumentException) e.getCause();
      Assert.assertEquals(StatusCode.Code.INVALID_ARGUMENT, apiException.getStatusCode().getCode());
    }
  }

  @Test
  @SuppressWarnings("all")
  public void listFieldsTest() {
    String nextPageToken = "";
    Field fieldsElement = Field.newBuilder().build();
    List<Field> fields = Arrays.asList(fieldsElement);
    ListFieldsResponse expectedResponse =
        ListFieldsResponse.newBuilder()
            .setNextPageToken(nextPageToken)
            .addAllFields(fields)
            .build();
    mockFirestoreAdmin.addResponse(expectedResponse);

    CollectionGroupName parent = CollectionGroupName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]");

    ListFieldsPagedResponse pagedListResponse = client.listFields(parent);

    List<Field> resources = Lists.newArrayList(pagedListResponse.iterateAll());
    Assert.assertEquals(1, resources.size());
    Assert.assertEquals(expectedResponse.getFieldsList().get(0), resources.get(0));

    List<AbstractMessage> actualRequests = mockFirestoreAdmin.getRequests();
    Assert.assertEquals(1, actualRequests.size());
    ListFieldsRequest actualRequest = (ListFieldsRequest) actualRequests.get(0);

    Assert.assertEquals(parent, CollectionGroupName.parse(actualRequest.getParent()));
    Assert.assertTrue(
        channelProvider.isHeaderSent(
            ApiClientHeaderProvider.getDefaultApiClientHeaderKey(),
            GaxGrpcProperties.getDefaultApiClientHeaderPattern()));
  }

  @Test
  @SuppressWarnings("all")
  public void listFieldsExceptionTest() throws Exception {
    StatusRuntimeException exception = new StatusRuntimeException(Status.INVALID_ARGUMENT);
    mockFirestoreAdmin.addException(exception);

    try {
      CollectionGroupName parent =
          CollectionGroupName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]");

      client.listFields(parent);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception
    }
  }

  @Test
  @SuppressWarnings("all")
  public void exportDocumentsTest() throws Exception {
    String outputUriPrefix = "outputUriPrefix124746435";
    ExportDocumentsResponse expectedResponse =
        ExportDocumentsResponse.newBuilder().setOutputUriPrefix(outputUriPrefix).build();
    Operation resultOperation =
        Operation.newBuilder()
            .setName("exportDocumentsTest")
            .setDone(true)
            .setResponse(Any.pack(expectedResponse))
            .build();
    mockFirestoreAdmin.addResponse(resultOperation);

    DatabaseName name = DatabaseName.of("[PROJECT]", "[DATABASE]");

    ExportDocumentsResponse actualResponse = client.exportDocumentsAsync(name).get();
    Assert.assertEquals(expectedResponse, actualResponse);

    List<AbstractMessage> actualRequests = mockFirestoreAdmin.getRequests();
    Assert.assertEquals(1, actualRequests.size());
    ExportDocumentsRequest actualRequest = (ExportDocumentsRequest) actualRequests.get(0);

    Assert.assertEquals(name, DatabaseName.parse(actualRequest.getName()));
    Assert.assertTrue(
        channelProvider.isHeaderSent(
            ApiClientHeaderProvider.getDefaultApiClientHeaderKey(),
            GaxGrpcProperties.getDefaultApiClientHeaderPattern()));
  }

  @Test
  @SuppressWarnings("all")
  public void exportDocumentsExceptionTest() throws Exception {
    StatusRuntimeException exception = new StatusRuntimeException(Status.INVALID_ARGUMENT);
    mockFirestoreAdmin.addException(exception);

    try {
      DatabaseName name = DatabaseName.of("[PROJECT]", "[DATABASE]");

      client.exportDocumentsAsync(name).get();
      Assert.fail("No exception raised");
    } catch (ExecutionException e) {
      Assert.assertEquals(InvalidArgumentException.class, e.getCause().getClass());
      InvalidArgumentException apiException = (InvalidArgumentException) e.getCause();
      Assert.assertEquals(StatusCode.Code.INVALID_ARGUMENT, apiException.getStatusCode().getCode());
    }
  }

  @Test
  @SuppressWarnings("all")
  public void importDocumentsTest() throws Exception {
    Empty expectedResponse = Empty.newBuilder().build();
    Operation resultOperation =
        Operation.newBuilder()
            .setName("importDocumentsTest")
            .setDone(true)
            .setResponse(Any.pack(expectedResponse))
            .build();
    mockFirestoreAdmin.addResponse(resultOperation);

    DatabaseName name = DatabaseName.of("[PROJECT]", "[DATABASE]");

    Empty actualResponse = client.importDocumentsAsync(name).get();
    Assert.assertEquals(expectedResponse, actualResponse);

    List<AbstractMessage> actualRequests = mockFirestoreAdmin.getRequests();
    Assert.assertEquals(1, actualRequests.size());
    ImportDocumentsRequest actualRequest = (ImportDocumentsRequest) actualRequests.get(0);

    Assert.assertEquals(name, DatabaseName.parse(actualRequest.getName()));
    Assert.assertTrue(
        channelProvider.isHeaderSent(
            ApiClientHeaderProvider.getDefaultApiClientHeaderKey(),
            GaxGrpcProperties.getDefaultApiClientHeaderPattern()));
  }

  @Test
  @SuppressWarnings("all")
  public void importDocumentsExceptionTest() throws Exception {
    StatusRuntimeException exception = new StatusRuntimeException(Status.INVALID_ARGUMENT);
    mockFirestoreAdmin.addException(exception);

    try {
      DatabaseName name = DatabaseName.of("[PROJECT]", "[DATABASE]");

      client.importDocumentsAsync(name).get();
      Assert.fail("No exception raised");
    } catch (ExecutionException e) {
      Assert.assertEquals(InvalidArgumentException.class, e.getCause().getClass());
      InvalidArgumentException apiException = (InvalidArgumentException) e.getCause();
      Assert.assertEquals(StatusCode.Code.INVALID_ARGUMENT, apiException.getStatusCode().getCode());
    }
  }
}
