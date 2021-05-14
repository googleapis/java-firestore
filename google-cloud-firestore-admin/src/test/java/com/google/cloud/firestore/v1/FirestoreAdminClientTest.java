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
import io.grpc.StatusRuntimeException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import javax.annotation.Generated;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

@Generated("by gapic-generator-java")
public class FirestoreAdminClientTest {
  private static MockServiceHelper mockServiceHelper;
  private FirestoreAdminClient client;
  private LocalChannelProvider channelProvider;
  private static MockFirestoreAdmin mockFirestoreAdmin;

  @BeforeClass
  public static void startStaticServer() {
    mockFirestoreAdmin = new MockFirestoreAdmin();
    mockServiceHelper =
        new MockServiceHelper(
            UUID.randomUUID().toString(), Arrays.<MockGrpcService>asList(mockFirestoreAdmin));
    mockServiceHelper.start();
  }

  @AfterClass
  public static void stopServer() {
    mockServiceHelper.stop();
  }

  @Before
  public void setUp() throws IOException {
    mockServiceHelper.reset();
    channelProvider = mockServiceHelper.createChannelProvider();
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
  public void createIndexTest() throws Exception {
    Index expectedResponse =
        Index.newBuilder()
            .setName(IndexName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]", "[INDEX]").toString())
            .addAllFields(new ArrayList<Index.IndexField>())
            .build();
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
    CreateIndexRequest actualRequest = ((CreateIndexRequest) actualRequests.get(0));

    Assert.assertEquals(parent.toString(), actualRequest.getParent());
    Assert.assertEquals(index, actualRequest.getIndex());
    Assert.assertTrue(
        channelProvider.isHeaderSent(
            ApiClientHeaderProvider.getDefaultApiClientHeaderKey(),
            GaxGrpcProperties.getDefaultApiClientHeaderPattern()));
  }

  @Test
  public void createIndexExceptionTest() throws Exception {
    StatusRuntimeException exception = new StatusRuntimeException(io.grpc.Status.INVALID_ARGUMENT);
    mockFirestoreAdmin.addException(exception);

    try {
      CollectionGroupName parent =
          CollectionGroupName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]");
      Index index = Index.newBuilder().build();
      client.createIndexAsync(parent, index).get();
      Assert.fail("No exception raised");
    } catch (ExecutionException e) {
      Assert.assertEquals(InvalidArgumentException.class, e.getCause().getClass());
      InvalidArgumentException apiException = ((InvalidArgumentException) e.getCause());
      Assert.assertEquals(StatusCode.Code.INVALID_ARGUMENT, apiException.getStatusCode().getCode());
    }
  }

  @Test
  public void createIndexTest2() throws Exception {
    Index expectedResponse =
        Index.newBuilder()
            .setName(IndexName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]", "[INDEX]").toString())
            .addAllFields(new ArrayList<Index.IndexField>())
            .build();
    Operation resultOperation =
        Operation.newBuilder()
            .setName("createIndexTest")
            .setDone(true)
            .setResponse(Any.pack(expectedResponse))
            .build();
    mockFirestoreAdmin.addResponse(resultOperation);

    String parent = "parent-995424086";
    Index index = Index.newBuilder().build();

    Index actualResponse = client.createIndexAsync(parent, index).get();
    Assert.assertEquals(expectedResponse, actualResponse);

    List<AbstractMessage> actualRequests = mockFirestoreAdmin.getRequests();
    Assert.assertEquals(1, actualRequests.size());
    CreateIndexRequest actualRequest = ((CreateIndexRequest) actualRequests.get(0));

    Assert.assertEquals(parent, actualRequest.getParent());
    Assert.assertEquals(index, actualRequest.getIndex());
    Assert.assertTrue(
        channelProvider.isHeaderSent(
            ApiClientHeaderProvider.getDefaultApiClientHeaderKey(),
            GaxGrpcProperties.getDefaultApiClientHeaderPattern()));
  }

  @Test
  public void createIndexExceptionTest2() throws Exception {
    StatusRuntimeException exception = new StatusRuntimeException(io.grpc.Status.INVALID_ARGUMENT);
    mockFirestoreAdmin.addException(exception);

    try {
      String parent = "parent-995424086";
      Index index = Index.newBuilder().build();
      client.createIndexAsync(parent, index).get();
      Assert.fail("No exception raised");
    } catch (ExecutionException e) {
      Assert.assertEquals(InvalidArgumentException.class, e.getCause().getClass());
      InvalidArgumentException apiException = ((InvalidArgumentException) e.getCause());
      Assert.assertEquals(StatusCode.Code.INVALID_ARGUMENT, apiException.getStatusCode().getCode());
    }
  }

  @Test
  public void listIndexesTest() throws Exception {
    Index responsesElement = Index.newBuilder().build();
    ListIndexesResponse expectedResponse =
        ListIndexesResponse.newBuilder()
            .setNextPageToken("")
            .addAllIndexes(Arrays.asList(responsesElement))
            .build();
    mockFirestoreAdmin.addResponse(expectedResponse);

    CollectionGroupName parent = CollectionGroupName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]");

    ListIndexesPagedResponse pagedListResponse = client.listIndexes(parent);

    List<Index> resources = Lists.newArrayList(pagedListResponse.iterateAll());

    Assert.assertEquals(1, resources.size());
    Assert.assertEquals(expectedResponse.getIndexesList().get(0), resources.get(0));

    List<AbstractMessage> actualRequests = mockFirestoreAdmin.getRequests();
    Assert.assertEquals(1, actualRequests.size());
    ListIndexesRequest actualRequest = ((ListIndexesRequest) actualRequests.get(0));

    Assert.assertEquals(parent.toString(), actualRequest.getParent());
    Assert.assertTrue(
        channelProvider.isHeaderSent(
            ApiClientHeaderProvider.getDefaultApiClientHeaderKey(),
            GaxGrpcProperties.getDefaultApiClientHeaderPattern()));
  }

  @Test
  public void listIndexesExceptionTest() throws Exception {
    StatusRuntimeException exception = new StatusRuntimeException(io.grpc.Status.INVALID_ARGUMENT);
    mockFirestoreAdmin.addException(exception);

    try {
      CollectionGroupName parent =
          CollectionGroupName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]");
      client.listIndexes(parent);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception.
    }
  }

  @Test
  public void listIndexesTest2() throws Exception {
    Index responsesElement = Index.newBuilder().build();
    ListIndexesResponse expectedResponse =
        ListIndexesResponse.newBuilder()
            .setNextPageToken("")
            .addAllIndexes(Arrays.asList(responsesElement))
            .build();
    mockFirestoreAdmin.addResponse(expectedResponse);

    String parent = "parent-995424086";

    ListIndexesPagedResponse pagedListResponse = client.listIndexes(parent);

    List<Index> resources = Lists.newArrayList(pagedListResponse.iterateAll());

    Assert.assertEquals(1, resources.size());
    Assert.assertEquals(expectedResponse.getIndexesList().get(0), resources.get(0));

    List<AbstractMessage> actualRequests = mockFirestoreAdmin.getRequests();
    Assert.assertEquals(1, actualRequests.size());
    ListIndexesRequest actualRequest = ((ListIndexesRequest) actualRequests.get(0));

    Assert.assertEquals(parent, actualRequest.getParent());
    Assert.assertTrue(
        channelProvider.isHeaderSent(
            ApiClientHeaderProvider.getDefaultApiClientHeaderKey(),
            GaxGrpcProperties.getDefaultApiClientHeaderPattern()));
  }

  @Test
  public void listIndexesExceptionTest2() throws Exception {
    StatusRuntimeException exception = new StatusRuntimeException(io.grpc.Status.INVALID_ARGUMENT);
    mockFirestoreAdmin.addException(exception);

    try {
      String parent = "parent-995424086";
      client.listIndexes(parent);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception.
    }
  }

  @Test
  public void getIndexTest() throws Exception {
    Index expectedResponse =
        Index.newBuilder()
            .setName(IndexName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]", "[INDEX]").toString())
            .addAllFields(new ArrayList<Index.IndexField>())
            .build();
    mockFirestoreAdmin.addResponse(expectedResponse);

    IndexName name = IndexName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]", "[INDEX]");

    Index actualResponse = client.getIndex(name);
    Assert.assertEquals(expectedResponse, actualResponse);

    List<AbstractMessage> actualRequests = mockFirestoreAdmin.getRequests();
    Assert.assertEquals(1, actualRequests.size());
    GetIndexRequest actualRequest = ((GetIndexRequest) actualRequests.get(0));

    Assert.assertEquals(name.toString(), actualRequest.getName());
    Assert.assertTrue(
        channelProvider.isHeaderSent(
            ApiClientHeaderProvider.getDefaultApiClientHeaderKey(),
            GaxGrpcProperties.getDefaultApiClientHeaderPattern()));
  }

  @Test
  public void getIndexExceptionTest() throws Exception {
    StatusRuntimeException exception = new StatusRuntimeException(io.grpc.Status.INVALID_ARGUMENT);
    mockFirestoreAdmin.addException(exception);

    try {
      IndexName name = IndexName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]", "[INDEX]");
      client.getIndex(name);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception.
    }
  }

  @Test
  public void getIndexTest2() throws Exception {
    Index expectedResponse =
        Index.newBuilder()
            .setName(IndexName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]", "[INDEX]").toString())
            .addAllFields(new ArrayList<Index.IndexField>())
            .build();
    mockFirestoreAdmin.addResponse(expectedResponse);

    String name = "name3373707";

    Index actualResponse = client.getIndex(name);
    Assert.assertEquals(expectedResponse, actualResponse);

    List<AbstractMessage> actualRequests = mockFirestoreAdmin.getRequests();
    Assert.assertEquals(1, actualRequests.size());
    GetIndexRequest actualRequest = ((GetIndexRequest) actualRequests.get(0));

    Assert.assertEquals(name, actualRequest.getName());
    Assert.assertTrue(
        channelProvider.isHeaderSent(
            ApiClientHeaderProvider.getDefaultApiClientHeaderKey(),
            GaxGrpcProperties.getDefaultApiClientHeaderPattern()));
  }

  @Test
  public void getIndexExceptionTest2() throws Exception {
    StatusRuntimeException exception = new StatusRuntimeException(io.grpc.Status.INVALID_ARGUMENT);
    mockFirestoreAdmin.addException(exception);

    try {
      String name = "name3373707";
      client.getIndex(name);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception.
    }
  }

  @Test
  public void deleteIndexTest() throws Exception {
    Empty expectedResponse = Empty.newBuilder().build();
    mockFirestoreAdmin.addResponse(expectedResponse);

    IndexName name = IndexName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]", "[INDEX]");

    client.deleteIndex(name);

    List<AbstractMessage> actualRequests = mockFirestoreAdmin.getRequests();
    Assert.assertEquals(1, actualRequests.size());
    DeleteIndexRequest actualRequest = ((DeleteIndexRequest) actualRequests.get(0));

    Assert.assertEquals(name.toString(), actualRequest.getName());
    Assert.assertTrue(
        channelProvider.isHeaderSent(
            ApiClientHeaderProvider.getDefaultApiClientHeaderKey(),
            GaxGrpcProperties.getDefaultApiClientHeaderPattern()));
  }

  @Test
  public void deleteIndexExceptionTest() throws Exception {
    StatusRuntimeException exception = new StatusRuntimeException(io.grpc.Status.INVALID_ARGUMENT);
    mockFirestoreAdmin.addException(exception);

    try {
      IndexName name = IndexName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]", "[INDEX]");
      client.deleteIndex(name);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception.
    }
  }

  @Test
  public void deleteIndexTest2() throws Exception {
    Empty expectedResponse = Empty.newBuilder().build();
    mockFirestoreAdmin.addResponse(expectedResponse);

    String name = "name3373707";

    client.deleteIndex(name);

    List<AbstractMessage> actualRequests = mockFirestoreAdmin.getRequests();
    Assert.assertEquals(1, actualRequests.size());
    DeleteIndexRequest actualRequest = ((DeleteIndexRequest) actualRequests.get(0));

    Assert.assertEquals(name, actualRequest.getName());
    Assert.assertTrue(
        channelProvider.isHeaderSent(
            ApiClientHeaderProvider.getDefaultApiClientHeaderKey(),
            GaxGrpcProperties.getDefaultApiClientHeaderPattern()));
  }

  @Test
  public void deleteIndexExceptionTest2() throws Exception {
    StatusRuntimeException exception = new StatusRuntimeException(io.grpc.Status.INVALID_ARGUMENT);
    mockFirestoreAdmin.addException(exception);

    try {
      String name = "name3373707";
      client.deleteIndex(name);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception.
    }
  }

  @Test
  public void getFieldTest() throws Exception {
    Field expectedResponse =
        Field.newBuilder()
            .setName(FieldName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]", "[FIELD]").toString())
            .setIndexConfig(Field.IndexConfig.newBuilder().build())
            .build();
    mockFirestoreAdmin.addResponse(expectedResponse);

    FieldName name = FieldName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]", "[FIELD]");

    Field actualResponse = client.getField(name);
    Assert.assertEquals(expectedResponse, actualResponse);

    List<AbstractMessage> actualRequests = mockFirestoreAdmin.getRequests();
    Assert.assertEquals(1, actualRequests.size());
    GetFieldRequest actualRequest = ((GetFieldRequest) actualRequests.get(0));

    Assert.assertEquals(name.toString(), actualRequest.getName());
    Assert.assertTrue(
        channelProvider.isHeaderSent(
            ApiClientHeaderProvider.getDefaultApiClientHeaderKey(),
            GaxGrpcProperties.getDefaultApiClientHeaderPattern()));
  }

  @Test
  public void getFieldExceptionTest() throws Exception {
    StatusRuntimeException exception = new StatusRuntimeException(io.grpc.Status.INVALID_ARGUMENT);
    mockFirestoreAdmin.addException(exception);

    try {
      FieldName name = FieldName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]", "[FIELD]");
      client.getField(name);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception.
    }
  }

  @Test
  public void getFieldTest2() throws Exception {
    Field expectedResponse =
        Field.newBuilder()
            .setName(FieldName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]", "[FIELD]").toString())
            .setIndexConfig(Field.IndexConfig.newBuilder().build())
            .build();
    mockFirestoreAdmin.addResponse(expectedResponse);

    String name = "name3373707";

    Field actualResponse = client.getField(name);
    Assert.assertEquals(expectedResponse, actualResponse);

    List<AbstractMessage> actualRequests = mockFirestoreAdmin.getRequests();
    Assert.assertEquals(1, actualRequests.size());
    GetFieldRequest actualRequest = ((GetFieldRequest) actualRequests.get(0));

    Assert.assertEquals(name, actualRequest.getName());
    Assert.assertTrue(
        channelProvider.isHeaderSent(
            ApiClientHeaderProvider.getDefaultApiClientHeaderKey(),
            GaxGrpcProperties.getDefaultApiClientHeaderPattern()));
  }

  @Test
  public void getFieldExceptionTest2() throws Exception {
    StatusRuntimeException exception = new StatusRuntimeException(io.grpc.Status.INVALID_ARGUMENT);
    mockFirestoreAdmin.addException(exception);

    try {
      String name = "name3373707";
      client.getField(name);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception.
    }
  }

  @Test
  public void updateFieldTest() throws Exception {
    Field expectedResponse =
        Field.newBuilder()
            .setName(FieldName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]", "[FIELD]").toString())
            .setIndexConfig(Field.IndexConfig.newBuilder().build())
            .build();
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
    UpdateFieldRequest actualRequest = ((UpdateFieldRequest) actualRequests.get(0));

    Assert.assertEquals(field, actualRequest.getField());
    Assert.assertTrue(
        channelProvider.isHeaderSent(
            ApiClientHeaderProvider.getDefaultApiClientHeaderKey(),
            GaxGrpcProperties.getDefaultApiClientHeaderPattern()));
  }

  @Test
  public void updateFieldExceptionTest() throws Exception {
    StatusRuntimeException exception = new StatusRuntimeException(io.grpc.Status.INVALID_ARGUMENT);
    mockFirestoreAdmin.addException(exception);

    try {
      Field field = Field.newBuilder().build();
      client.updateFieldAsync(field).get();
      Assert.fail("No exception raised");
    } catch (ExecutionException e) {
      Assert.assertEquals(InvalidArgumentException.class, e.getCause().getClass());
      InvalidArgumentException apiException = ((InvalidArgumentException) e.getCause());
      Assert.assertEquals(StatusCode.Code.INVALID_ARGUMENT, apiException.getStatusCode().getCode());
    }
  }

  @Test
  public void listFieldsTest() throws Exception {
    Field responsesElement = Field.newBuilder().build();
    ListFieldsResponse expectedResponse =
        ListFieldsResponse.newBuilder()
            .setNextPageToken("")
            .addAllFields(Arrays.asList(responsesElement))
            .build();
    mockFirestoreAdmin.addResponse(expectedResponse);

    CollectionGroupName parent = CollectionGroupName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]");

    ListFieldsPagedResponse pagedListResponse = client.listFields(parent);

    List<Field> resources = Lists.newArrayList(pagedListResponse.iterateAll());

    Assert.assertEquals(1, resources.size());
    Assert.assertEquals(expectedResponse.getFieldsList().get(0), resources.get(0));

    List<AbstractMessage> actualRequests = mockFirestoreAdmin.getRequests();
    Assert.assertEquals(1, actualRequests.size());
    ListFieldsRequest actualRequest = ((ListFieldsRequest) actualRequests.get(0));

    Assert.assertEquals(parent.toString(), actualRequest.getParent());
    Assert.assertTrue(
        channelProvider.isHeaderSent(
            ApiClientHeaderProvider.getDefaultApiClientHeaderKey(),
            GaxGrpcProperties.getDefaultApiClientHeaderPattern()));
  }

  @Test
  public void listFieldsExceptionTest() throws Exception {
    StatusRuntimeException exception = new StatusRuntimeException(io.grpc.Status.INVALID_ARGUMENT);
    mockFirestoreAdmin.addException(exception);

    try {
      CollectionGroupName parent =
          CollectionGroupName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]");
      client.listFields(parent);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception.
    }
  }

  @Test
  public void listFieldsTest2() throws Exception {
    Field responsesElement = Field.newBuilder().build();
    ListFieldsResponse expectedResponse =
        ListFieldsResponse.newBuilder()
            .setNextPageToken("")
            .addAllFields(Arrays.asList(responsesElement))
            .build();
    mockFirestoreAdmin.addResponse(expectedResponse);

    String parent = "parent-995424086";

    ListFieldsPagedResponse pagedListResponse = client.listFields(parent);

    List<Field> resources = Lists.newArrayList(pagedListResponse.iterateAll());

    Assert.assertEquals(1, resources.size());
    Assert.assertEquals(expectedResponse.getFieldsList().get(0), resources.get(0));

    List<AbstractMessage> actualRequests = mockFirestoreAdmin.getRequests();
    Assert.assertEquals(1, actualRequests.size());
    ListFieldsRequest actualRequest = ((ListFieldsRequest) actualRequests.get(0));

    Assert.assertEquals(parent, actualRequest.getParent());
    Assert.assertTrue(
        channelProvider.isHeaderSent(
            ApiClientHeaderProvider.getDefaultApiClientHeaderKey(),
            GaxGrpcProperties.getDefaultApiClientHeaderPattern()));
  }

  @Test
  public void listFieldsExceptionTest2() throws Exception {
    StatusRuntimeException exception = new StatusRuntimeException(io.grpc.Status.INVALID_ARGUMENT);
    mockFirestoreAdmin.addException(exception);

    try {
      String parent = "parent-995424086";
      client.listFields(parent);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception.
    }
  }

  @Test
  public void exportDocumentsTest() throws Exception {
    ExportDocumentsResponse expectedResponse =
        ExportDocumentsResponse.newBuilder().setOutputUriPrefix("outputUriPrefix499858205").build();
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
    ExportDocumentsRequest actualRequest = ((ExportDocumentsRequest) actualRequests.get(0));

    Assert.assertEquals(name.toString(), actualRequest.getName());
    Assert.assertTrue(
        channelProvider.isHeaderSent(
            ApiClientHeaderProvider.getDefaultApiClientHeaderKey(),
            GaxGrpcProperties.getDefaultApiClientHeaderPattern()));
  }

  @Test
  public void exportDocumentsExceptionTest() throws Exception {
    StatusRuntimeException exception = new StatusRuntimeException(io.grpc.Status.INVALID_ARGUMENT);
    mockFirestoreAdmin.addException(exception);

    try {
      DatabaseName name = DatabaseName.of("[PROJECT]", "[DATABASE]");
      client.exportDocumentsAsync(name).get();
      Assert.fail("No exception raised");
    } catch (ExecutionException e) {
      Assert.assertEquals(InvalidArgumentException.class, e.getCause().getClass());
      InvalidArgumentException apiException = ((InvalidArgumentException) e.getCause());
      Assert.assertEquals(StatusCode.Code.INVALID_ARGUMENT, apiException.getStatusCode().getCode());
    }
  }

  @Test
  public void exportDocumentsTest2() throws Exception {
    ExportDocumentsResponse expectedResponse =
        ExportDocumentsResponse.newBuilder().setOutputUriPrefix("outputUriPrefix499858205").build();
    Operation resultOperation =
        Operation.newBuilder()
            .setName("exportDocumentsTest")
            .setDone(true)
            .setResponse(Any.pack(expectedResponse))
            .build();
    mockFirestoreAdmin.addResponse(resultOperation);

    String name = "name3373707";

    ExportDocumentsResponse actualResponse = client.exportDocumentsAsync(name).get();
    Assert.assertEquals(expectedResponse, actualResponse);

    List<AbstractMessage> actualRequests = mockFirestoreAdmin.getRequests();
    Assert.assertEquals(1, actualRequests.size());
    ExportDocumentsRequest actualRequest = ((ExportDocumentsRequest) actualRequests.get(0));

    Assert.assertEquals(name, actualRequest.getName());
    Assert.assertTrue(
        channelProvider.isHeaderSent(
            ApiClientHeaderProvider.getDefaultApiClientHeaderKey(),
            GaxGrpcProperties.getDefaultApiClientHeaderPattern()));
  }

  @Test
  public void exportDocumentsExceptionTest2() throws Exception {
    StatusRuntimeException exception = new StatusRuntimeException(io.grpc.Status.INVALID_ARGUMENT);
    mockFirestoreAdmin.addException(exception);

    try {
      String name = "name3373707";
      client.exportDocumentsAsync(name).get();
      Assert.fail("No exception raised");
    } catch (ExecutionException e) {
      Assert.assertEquals(InvalidArgumentException.class, e.getCause().getClass());
      InvalidArgumentException apiException = ((InvalidArgumentException) e.getCause());
      Assert.assertEquals(StatusCode.Code.INVALID_ARGUMENT, apiException.getStatusCode().getCode());
    }
  }

  @Test
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

    client.importDocumentsAsync(name).get();

    List<AbstractMessage> actualRequests = mockFirestoreAdmin.getRequests();
    Assert.assertEquals(1, actualRequests.size());
    ImportDocumentsRequest actualRequest = ((ImportDocumentsRequest) actualRequests.get(0));

    Assert.assertEquals(name.toString(), actualRequest.getName());
    Assert.assertTrue(
        channelProvider.isHeaderSent(
            ApiClientHeaderProvider.getDefaultApiClientHeaderKey(),
            GaxGrpcProperties.getDefaultApiClientHeaderPattern()));
  }

  @Test
  public void importDocumentsExceptionTest() throws Exception {
    StatusRuntimeException exception = new StatusRuntimeException(io.grpc.Status.INVALID_ARGUMENT);
    mockFirestoreAdmin.addException(exception);

    try {
      DatabaseName name = DatabaseName.of("[PROJECT]", "[DATABASE]");
      client.importDocumentsAsync(name).get();
      Assert.fail("No exception raised");
    } catch (ExecutionException e) {
      Assert.assertEquals(InvalidArgumentException.class, e.getCause().getClass());
      InvalidArgumentException apiException = ((InvalidArgumentException) e.getCause());
      Assert.assertEquals(StatusCode.Code.INVALID_ARGUMENT, apiException.getStatusCode().getCode());
    }
  }

  @Test
  public void importDocumentsTest2() throws Exception {
    Empty expectedResponse = Empty.newBuilder().build();
    Operation resultOperation =
        Operation.newBuilder()
            .setName("importDocumentsTest")
            .setDone(true)
            .setResponse(Any.pack(expectedResponse))
            .build();
    mockFirestoreAdmin.addResponse(resultOperation);

    String name = "name3373707";

    client.importDocumentsAsync(name).get();

    List<AbstractMessage> actualRequests = mockFirestoreAdmin.getRequests();
    Assert.assertEquals(1, actualRequests.size());
    ImportDocumentsRequest actualRequest = ((ImportDocumentsRequest) actualRequests.get(0));

    Assert.assertEquals(name, actualRequest.getName());
    Assert.assertTrue(
        channelProvider.isHeaderSent(
            ApiClientHeaderProvider.getDefaultApiClientHeaderKey(),
            GaxGrpcProperties.getDefaultApiClientHeaderPattern()));
  }

  @Test
  public void importDocumentsExceptionTest2() throws Exception {
    StatusRuntimeException exception = new StatusRuntimeException(io.grpc.Status.INVALID_ARGUMENT);
    mockFirestoreAdmin.addException(exception);

    try {
      String name = "name3373707";
      client.importDocumentsAsync(name).get();
      Assert.fail("No exception raised");
    } catch (ExecutionException e) {
      Assert.assertEquals(InvalidArgumentException.class, e.getCause().getClass());
      InvalidArgumentException apiException = ((InvalidArgumentException) e.getCause());
      Assert.assertEquals(StatusCode.Code.INVALID_ARGUMENT, apiException.getStatusCode().getCode());
    }
  }
}
