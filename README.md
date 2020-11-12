# Google Cloud Firestore Client for Java

Java idiomatic client for [Cloud Firestore][product-docs].

[![Maven][maven-version-image]][maven-version-link]
![Stability][stability-image]

- [Product Documentation][product-docs]
- [Client Library Documentation][javadocs]

## Quickstart

If you are using Maven with [BOM][libraries-bom], add this to your pom.xml file
```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>com.google.cloud</groupId>
      <artifactId>libraries-bom</artifactId>
      <version>15.1.0</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>

<dependencies>
  <dependency>
    <groupId>com.google.cloud</groupId>
    <artifactId>google-cloud-firestore</artifactId>
  </dependency>

```

If you are using Maven without BOM, add this to your dependencies:

```xml
<dependency>
  <groupId>com.google.cloud</groupId>
  <artifactId>google-cloud-firestore</artifactId>
  <version>2.1.0</version>
</dependency>

```

If you are using Gradle, add this to your dependencies
```Groovy
compile 'com.google.cloud:google-cloud-firestore:2.1.0'
```
If you are using SBT, add this to your dependencies
```Scala
libraryDependencies += "com.google.cloud" % "google-cloud-firestore" % "2.1.0"
```

## Authentication

See the [Authentication][authentication] section in the base directory's README.

## Getting Started

### Prerequisites

You will need a [Google Cloud Platform Console][developer-console] project with the Cloud Firestore [API enabled][enable-api].

[Follow these instructions][create-project] to get your project set up. You will also need to set up the local development environment by
[installing the Google Cloud SDK][cloud-sdk] and running the following commands in command line:
`gcloud auth login` and `gcloud config set project [YOUR PROJECT ID]`.

### Installation and setup

You'll need to obtain the `google-cloud-firestore` library.  See the [Quickstart](#quickstart) section
to add `google-cloud-firestore` as a dependency in your code.

## About Cloud Firestore


[Cloud Firestore][product-docs] is a fully-managed NoSQL document database for mobile, web, and server development from Firebase and Google Cloud Platform.  It's backed by a multi-region replicated database that ensures once data is committed, it's durable even in the face of unexpected disasters. Not only that, but despite being a distributed database, it's also strongly consistent and offers seamless integration with other Firebase and Google Cloud Platform products, including Google Cloud Functions.

See the [Cloud Firestore client library docs][javadocs] to learn how to
use this Cloud Firestore Client Library.





## Samples

Samples are in the [`samples/`](https://github.com/googleapis/java-firestore/tree/master/samples) directory. The samples' `README.md`
has instructions for running the samples.

| Sample                      | Source Code                       | Try it |
| --------------------------- | --------------------------------- | ------ |
| Quickstart | [source code](https://github.com/googleapis/java-firestore/blob/master/samples/snippets/src/main/java/com/example/firestore/Quickstart.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-firestore&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/firestore/Quickstart.java) |
| Listen Data Snippets | [source code](https://github.com/googleapis/java-firestore/blob/master/samples/snippets/src/main/java/com/example/firestore/snippets/ListenDataSnippets.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-firestore&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/firestore/snippets/ListenDataSnippets.java) |
| Manage Data Snippets | [source code](https://github.com/googleapis/java-firestore/blob/master/samples/snippets/src/main/java/com/example/firestore/snippets/ManageDataSnippets.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-firestore&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/firestore/snippets/ManageDataSnippets.java) |
| Query Data Snippets | [source code](https://github.com/googleapis/java-firestore/blob/master/samples/snippets/src/main/java/com/example/firestore/snippets/QueryDataSnippets.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-firestore&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/firestore/snippets/QueryDataSnippets.java) |
| References | [source code](https://github.com/googleapis/java-firestore/blob/master/samples/snippets/src/main/java/com/example/firestore/snippets/References.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-firestore&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/firestore/snippets/References.java) |
| Retrieve Data Snippets | [source code](https://github.com/googleapis/java-firestore/blob/master/samples/snippets/src/main/java/com/example/firestore/snippets/RetrieveDataSnippets.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-firestore&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/firestore/snippets/RetrieveDataSnippets.java) |
| City | [source code](https://github.com/googleapis/java-firestore/blob/master/samples/snippets/src/main/java/com/example/firestore/snippets/model/City.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-firestore&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/firestore/snippets/model/City.java) |



## Troubleshooting

To get help, follow the instructions in the [shared Troubleshooting document][troubleshooting].

## Transport

Cloud Firestore uses gRPC for the transport layer.

## Java Versions

Java 7 or above is required for using this client.

## Versioning


This library follows [Semantic Versioning](http://semver.org/).


## Contributing


Contributions to this library are always welcome and highly encouraged.

See [CONTRIBUTING][contributing] for more information how to get started.

Please note that this project is released with a Contributor Code of Conduct. By participating in
this project you agree to abide by its terms. See [Code of Conduct][code-of-conduct] for more
information.

## License

Apache 2.0 - See [LICENSE][license] for more information.

## CI Status

Java Version | Status
------------ | ------
Java 7 | [![Kokoro CI][kokoro-badge-image-1]][kokoro-badge-link-1]
Java 8 | [![Kokoro CI][kokoro-badge-image-2]][kokoro-badge-link-2]
Java 8 OSX | [![Kokoro CI][kokoro-badge-image-3]][kokoro-badge-link-3]
Java 8 Windows | [![Kokoro CI][kokoro-badge-image-4]][kokoro-badge-link-4]
Java 11 | [![Kokoro CI][kokoro-badge-image-5]][kokoro-badge-link-5]

[product-docs]: https://cloud.google.com/firestore
[javadocs]: https://googleapis.dev/java/google-cloud-firestore/latest
[kokoro-badge-image-1]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-firestore/java7.svg
[kokoro-badge-link-1]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-firestore/java7.html
[kokoro-badge-image-2]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-firestore/java8.svg
[kokoro-badge-link-2]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-firestore/java8.html
[kokoro-badge-image-3]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-firestore/java8-osx.svg
[kokoro-badge-link-3]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-firestore/java8-osx.html
[kokoro-badge-image-4]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-firestore/java8-win.svg
[kokoro-badge-link-4]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-firestore/java8-win.html
[kokoro-badge-image-5]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-firestore/java11.svg
[kokoro-badge-link-5]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-firestore/java11.html
[stability-image]: https://img.shields.io/badge/stability-ga-green
[maven-version-image]: https://img.shields.io/maven-central/v/com.google.cloud/google-cloud-firestore.svg
[maven-version-link]: https://search.maven.org/search?q=g:com.google.cloud%20AND%20a:google-cloud-firestore&core=gav
[authentication]: https://github.com/googleapis/google-cloud-java#authentication
[developer-console]: https://console.developers.google.com/
[create-project]: https://cloud.google.com/resource-manager/docs/creating-managing-projects
[cloud-sdk]: https://cloud.google.com/sdk/
[troubleshooting]: https://github.com/googleapis/google-cloud-common/blob/master/troubleshooting/readme.md#troubleshooting
[contributing]: https://github.com/googleapis/java-firestore/blob/master/CONTRIBUTING.md
[code-of-conduct]: https://github.com/googleapis/java-firestore/blob/master/CODE_OF_CONDUCT.md#contributor-code-of-conduct
[license]: https://github.com/googleapis/java-firestore/blob/master/LICENSE

[enable-api]: https://console.cloud.google.com/flows/enableapi?apiid=firestore.googleapis.com
[libraries-bom]: https://github.com/GoogleCloudPlatform/cloud-opensource-java/wiki/The-Google-Cloud-Platform-Libraries-BOM
[shell_img]: https://gstatic.com/cloudssh/images/open-btn.png
