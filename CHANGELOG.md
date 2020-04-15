# Changelog

### [1.32.5](https://www.github.com/googleapis/java-firestore/compare/v1.32.4...v1.32.5) (2020-03-05)


### Bug Fixes

* deflake-ify ITSystemTest#queryWatch ([#107](https://www.github.com/googleapis/java-firestore/issues/107)) ([f701c67](https://www.github.com/googleapis/java-firestore/commit/f701c67f9015ca96ca42c40f7e58d95dce70b18e))


### Dependencies

* update core dependencies ([#120](https://www.github.com/googleapis/java-firestore/issues/120)) ([293ba55](https://www.github.com/googleapis/java-firestore/commit/293ba5523bef2310acc91d5d97462c50889b75a8))
* update core dependencies to v1.93.1 ([#124](https://www.github.com/googleapis/java-firestore/issues/124)) ([6ce14ce](https://www.github.com/googleapis/java-firestore/commit/6ce14ce0fe4ff366792c18816f047c217e67f9f3))
* update dependency com.google.cloud:google-cloud-conformance-tests to v0.0.6 ([#118](https://www.github.com/googleapis/java-firestore/issues/118)) ([7dad920](https://www.github.com/googleapis/java-firestore/commit/7dad9207802c6d8e95aad13622735f43118405e1))
* update dependency io.grpc:grpc-bom to v1.27.2 ([#116](https://www.github.com/googleapis/java-firestore/issues/116)) ([83ef310](https://www.github.com/googleapis/java-firestore/commit/83ef31071ab3a26ea6d7a8ab2c0009de227b7ee7))
* update jackson dependencies to v2.10.3 ([#122](https://www.github.com/googleapis/java-firestore/issues/122)) ([85a3ced](https://www.github.com/googleapis/java-firestore/commit/85a3ced134d1752e6bee3b1445d946a51d5925cd))

### [1.32.4](https://www.github.com/googleapis/java-firestore/compare/v1.32.3...v1.32.4) (2020-02-18)


### Bug Fixes

* add cause to transaction errors on transaction commit ([#108](https://www.github.com/googleapis/java-firestore/issues/108)) ([00b3c6f](https://www.github.com/googleapis/java-firestore/commit/00b3c6f933eeb4a11cf4b18ea8c938549121f6c6))
* remove error_prone_annotations exclusion rules from poms ([#97](https://www.github.com/googleapis/java-firestore/issues/97)) ([0f9b474](https://www.github.com/googleapis/java-firestore/commit/0f9b4745f120644e9116a4461372260ce8506160))


### Dependencies

* update core dependencies to v1.92.5 ([#101](https://www.github.com/googleapis/java-firestore/issues/101)) ([e767078](https://www.github.com/googleapis/java-firestore/commit/e767078638e5ef22f753608cb64586f813ffc21b))
* update dependency io.grpc:grpc-bom to v1.27.1 ([#106](https://www.github.com/googleapis/java-firestore/issues/106)) ([46a7c48](https://www.github.com/googleapis/java-firestore/commit/46a7c4855a48d4ada559cf4941fb011ed40a6cf3))
* update opencensus.version to v0.25.0 ([#105](https://www.github.com/googleapis/java-firestore/issues/105)) ([fcdbab3](https://www.github.com/googleapis/java-firestore/commit/fcdbab3856f1b0c0ad81554e381c5158d87e490d))
* update protobuf.version to v3.11.4 ([#109](https://www.github.com/googleapis/java-firestore/issues/109)) ([4f498b4](https://www.github.com/googleapis/java-firestore/commit/4f498b448d6212ea921a2240413e17b86d2fb3d3))


### Documentation

* **regen:** update sample code to set total timeout, add API client header test ([#100](https://www.github.com/googleapis/java-firestore/issues/100)) ([d14a4a1](https://www.github.com/googleapis/java-firestore/commit/d14a4a1e30e4f9651a71ad9c10893b1eef321441))

### [1.32.3](https://www.github.com/googleapis/java-firestore/compare/v1.32.2...v1.32.3) (2020-02-03)


### Dependencies

* update core dependencies ([#89](https://www.github.com/googleapis/java-firestore/issues/89)) ([7593432](https://www.github.com/googleapis/java-firestore/commit/7593432fdeb3ec88dde1400e8517f6bc35372648))
* update dependency com.google.auth:google-auth-library-credentials to v0.20.0 ([#86](https://www.github.com/googleapis/java-firestore/issues/86)) ([f32bd9b](https://www.github.com/googleapis/java-firestore/commit/f32bd9bd23d5caf1f121927df173978b66d61833))
* update dependency com.google.cloud:google-cloud-conformance-tests to v0.0.5 ([#90](https://www.github.com/googleapis/java-firestore/issues/90)) ([f701446](https://www.github.com/googleapis/java-firestore/commit/f701446f7fb4d0b46b49a698171c8b1878d18623))
* update protobuf.version to v3.11.3 ([#94](https://www.github.com/googleapis/java-firestore/issues/94)) ([6155816](https://www.github.com/googleapis/java-firestore/commit/6155816c61ec999ae026884075af053500f5d448))

### [1.32.2](https://www.github.com/googleapis/java-firestore/compare/v1.32.1...v1.32.2) (2020-01-14)


### Dependencies

* update core dependencies ([#63](https://www.github.com/googleapis/java-firestore/issues/63)) ([4c1fb09](https://www.github.com/googleapis/java-firestore/commit/4c1fb091ed001cbebab9c2c16aaa93dd6f6875c8))
* update dependency com.fasterxml.jackson.core:jackson-core to v2.10.2 ([#69](https://www.github.com/googleapis/java-firestore/issues/69)) ([5e296b8](https://www.github.com/googleapis/java-firestore/commit/5e296b8439da390661a3c4eb44452c51fb4bf486))
* update dependency com.fasterxml.jackson.core:jackson-databind to v2.10.2 ([#70](https://www.github.com/googleapis/java-firestore/issues/70)) ([2f3a14b](https://www.github.com/googleapis/java-firestore/commit/2f3a14b075ff90209190319266ce25a5b94fcb31))
* update dependency com.google.cloud:google-cloud-conformance-tests to v0.0.4 ([#82](https://www.github.com/googleapis/java-firestore/issues/82)) ([6050055](https://www.github.com/googleapis/java-firestore/commit/6050055779d8e45f2904efddd8b58a8b9dd43c40))
* update dependency com.google.guava:guava-bom to v28.2-android ([#76](https://www.github.com/googleapis/java-firestore/issues/76)) ([c1dcb9f](https://www.github.com/googleapis/java-firestore/commit/c1dcb9f67b141d06226c1ec0d6661ff3c7d7c09c)), closes [#33](https://www.github.com/googleapis/java-firestore/issues/33)
* update dependency com.google.truth:truth to v1.0.1 ([#81](https://www.github.com/googleapis/java-firestore/issues/81)) ([5d16063](https://www.github.com/googleapis/java-firestore/commit/5d16063587bb3d195b3440ff1b62698b21b111af))
* update dependency org.threeten:threetenbp to v1.4.1 ([#78](https://www.github.com/googleapis/java-firestore/issues/78)) ([0db7256](https://www.github.com/googleapis/java-firestore/commit/0db72560449de74dc92030d84c0116d209f8ba12))

### [1.32.1](https://www.github.com/googleapis/java-firestore/compare/v1.32.0...v1.32.1) (2020-01-02)


### Bug Fixes

* add google-cloud-firestore to bom ([#65](https://www.github.com/googleapis/java-firestore/issues/65)) ([b273a58](https://www.github.com/googleapis/java-firestore/commit/b273a58c714c1d4e9b07bda68fe47aa5496d8456))
* set google-cloud-conformance-tests to test scope ([#44](https://www.github.com/googleapis/java-firestore/issues/44)) ([03983c3](https://www.github.com/googleapis/java-firestore/commit/03983c33228806fcf1fe7d9eaf8240f9d60ad75b))


### Dependencies

* update dependencies ([#51](https://www.github.com/googleapis/java-firestore/issues/51)) ([ce4c37b](https://www.github.com/googleapis/java-firestore/commit/ce4c37b06683a34665cbc963df7fbb5d5112888b)), closes [#49](https://www.github.com/googleapis/java-firestore/issues/49) [#50](https://www.github.com/googleapis/java-firestore/issues/50)
* update dependency com.google.auth:google-auth-library-credentials to v0.19.0 ([#48](https://www.github.com/googleapis/java-firestore/issues/48)) ([d68b457](https://www.github.com/googleapis/java-firestore/commit/d68b45785ec0d948e1c290ce71e698e19574ca28))
* update dependency com.google.cloud:google-cloud-conformance-tests to v0.0.3 ([#55](https://www.github.com/googleapis/java-firestore/issues/55)) ([6e6644f](https://www.github.com/googleapis/java-firestore/commit/6e6644fd6e18032d4a2633e74c17f02259d97759))
* update dependency junit:junit to v4.13 ([#62](https://www.github.com/googleapis/java-firestore/issues/62)) ([7f80e44](https://www.github.com/googleapis/java-firestore/commit/7f80e447a89d27b74730bf89bbcf448e15960ff9))


### Documentation

* bump bom version in README to 3.1.0 ([#47](https://www.github.com/googleapis/java-firestore/issues/47)) ([d8bebb3](https://www.github.com/googleapis/java-firestore/commit/d8bebb3847f24df13b3a2fd5a1fe5aa11348daaf))

## [1.32.0](https://www.github.com/googleapis/java-firestore/compare/1.31.0...v1.32.0) (2019-12-04)


### Features

* **firestore:** allow passing POJOs as field values throughout API reference ([#6843](https://www.github.com/googleapis/java-firestore/issues/6843)) ([180f5a9](https://www.github.com/googleapis/java-firestore/commit/180f5a965ca2ea8b22338d0cc186b3d8d3bb997e))


### Dependencies

* update gax.version to v1.51.0 ([#31](https://www.github.com/googleapis/java-firestore/issues/31)) ([d5125c5](https://www.github.com/googleapis/java-firestore/commit/d5125c57fe02652d0e1b3bb10a2f162ce7317659))
* update protobuf packages to v3.11.0 ([#26](https://www.github.com/googleapis/java-firestore/issues/26)) ([862ebce](https://www.github.com/googleapis/java-firestore/commit/862ebce0c9ef77d90bc16b612a856dbc75265929))
* update protobuf packages to v3.11.1 ([#28](https://www.github.com/googleapis/java-firestore/issues/28)) ([c0c3b2c](https://www.github.com/googleapis/java-firestore/commit/c0c3b2c8b00fdc44dd6235d477538b1c7f085325))
