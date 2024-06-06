
Run below command to generate api.txt
```bash
mvn exec:java -Dexec.mainClass="com.android.tools.metalava.Driver" -Dexec.args="--source-path ../google-cloud-firestore/src/main/java --hide HiddenSuperclass --hide HiddenAbstractMethod --api api.txt --format=v2"
```
