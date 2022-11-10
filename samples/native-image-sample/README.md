# Firestore Sample Application with Native Image

This application uses the [Google Cloud Firestore client libraries](https://cloud.google.com/firestore/docs/quickstart-servers#java) and is compatible with Native Image compilation.

This sample runs through basic operations of creating a new document, running queries, and then deleting the created resources.

## Setup Instructions

You will need to follow these prerequisite steps in order to run the samples:

1. If you have not already, [create a Google Cloud Platform Project](https://cloud.google.com/resource-manager/docs/creating-managing-projects#creating_a_project).

2. Install the [Google Cloud SDK](https://cloud.google.com/sdk/) which will allow you to run the sample with your project's credentials.

   Once installed, log in with Application Default Credentials using the following command:

    ```
    gcloud auth application-default login
    ```

   **Note:** Authenticating with Application Default Credentials is convenient to use during development, but we recommend [alternate methods of authentication](https://cloud.google.com/docs/authentication/production) during production use.

3. Install the GraalVM compiler.

   You can follow the [official installation instructions](https://www.graalvm.org/docs/getting-started/#install-graalvm) from the GraalVM website.
   After following the instructions, ensure that you install the native image extension installed by running:

    ```
    gu install native-image
    ```

   Once you finish following the instructions, verify that the default version of Java is set to the GraalVM version by running `java -version` in a terminal.

   You will see something similar to the below output:

    ```
    $ java -version
   
    openjdk version "11.0.7" 2020-04-14
    OpenJDK Runtime Environment GraalVM CE 20.1.0 (build 11.0.7+10-jvmci-20.1-b02)
    OpenJDK 64-Bit Server VM GraalVM CE 20.1.0 (build 11.0.7+10-jvmci-20.1-b02, mixed mode, sharing)
    ```
   
## Sample

1. If you wish to run the application against the [Firestore emulator](https://cloud.google.com/sdk/gcloud/reference/beta/emulators/firestore), make sure that you have the [Google Cloud SDK](https://cloud.google.com/sdk) installed.

   In a new terminal window, start the emulator via `gcloud`:

    ```
    gcloud beta emulators firestore start --host-port=localhost:9010
    ```

   Leave the emulator running in this terminal for now.
   In the next section, we will run the sample application against the Firestore emulator instance.


2. Navigate to this directory and compile the application with the Native Image compiler.

    ```
    mvn package -P native -DskipTests
    ```

3. **(Optional)** If you're using the emulator, export the `FIRESTORE_EMULATOR_HOST` as an environment variable in your terminal.

    ```
    export FIRESTORE_EMULATOR_HOST=localhost:9010
    ``` 

   The Firestore Client Libraries will detect this environment variable and automatically connect to the emulator instance if this variable is set.

4. Run the application.

    ```
    ./target/native-image-sample
    ```

5. The application will run through some basic Firestore operations and log some output statements.

    ```
    Created user alovelace. Timestamp: 2020-12-15T20:19:28.444070000Z
    The following users were saved:
    Document: alovelace | Ada Lovelace born 1815
    Number of users born before 1900: 1
    Number of users born earlier after 1900: 0
    Number of users whose first name is 'Ada': 0
    ```

## Sample Integration test with Native Image Support

In order to run the sample integration test as a native image, call the following command:

   ```
   mvn test -Pnative
   ```