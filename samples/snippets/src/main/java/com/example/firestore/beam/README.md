# Firestore Beam Samples

## Running instructions
These examples will show you how to read and write data from/into a Firestore database using the Firestore Beam connector.

1. Set up the following environment variables
```
GOOGLE_CLOUD_PROJECT=<your-project-id>
REGION=<your-region>
TEMP_LOCATION=gs://<your-temp-bucket>/temp/
NUM_WORKERS=<num-workers>
MAX_NUM_WORKERS=<max-num-workers>
```
2. Run the command to write example "cities" data:
```
mvn compile exec:java -Dexec.mainClass=ExampleFirestoreWrite \
"-Dexec.args=--project=$GOOGLE_CLOUD_PROJECT \
--runner=DataflowRunner \
--project=$GOOGLE_CLOUD_PROJECT \
--region=$REGION \
--tempLocation=$TEMP_LOCATION \
--numWorkers=$NUM_WORKERS \
--maxNumWorkers=$MAX_NUM_WORKERS"
```

3. Run the command to filter and read the data you wrote:
```
mvn compile exec:java -Dexec.mainClass=ExampleFirestoreRead \
"-Dexec.args=--project=$GOOGLE_CLOUD_PROJECT \
--runner=DataflowRunner \
--region=$REGION \
--tempLocation=$TEMP_LOCATION \
--numWorkers=$NUM_WORKERS \
--maxNumWorkers=$MAX_NUM_WORKERS"
```