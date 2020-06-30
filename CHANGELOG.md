# Changelog

## [1.35.0](https://www.github.com/googleapis/java-firestore/compare/v1.34.0...v1.35.0) (2020-06-17)


### Features

* ability to serialize Query to Proto ([#241](https://www.github.com/googleapis/java-firestore/issues/241)) ([bae22e0](https://www.github.com/googleapis/java-firestore/commit/bae22e0839de55e11dda604c3034feaedbbc172a))
* add support for fieldmask to document reference ([#245](https://www.github.com/googleapis/java-firestore/issues/245)) ([4a846b1](https://www.github.com/googleapis/java-firestore/commit/4a846b1f067ad8e462df673ada38589da224fcef))


### Dependencies

* update core dependencies ([#254](https://www.github.com/googleapis/java-firestore/issues/254)) ([9b275ca](https://www.github.com/googleapis/java-firestore/commit/9b275ca5b3f2adbe18be77ea8c86d8446a5833d6))
* update dependency com.google.api:api-common to v1.9.2 ([#238](https://www.github.com/googleapis/java-firestore/issues/238)) ([c47d327](https://www.github.com/googleapis/java-firestore/commit/c47d32705645a76d8f9598aa954dbc3b1c067c73))
* update dependency io.grpc:grpc-bom to v1.30.0 ([#244](https://www.github.com/googleapis/java-firestore/issues/244)) ([b5749d4](https://www.github.com/googleapis/java-firestore/commit/b5749d4e9bac3628da66451fa070c1bf6f852614))

## [1.34.0](https://www.github.com/googleapis/java-firestore/compare/v1.33.0...v1.34.0) (2020-05-29)


### Features

* add support for BigDecimal to CustomClassMapper ([#196](https://www.github.com/googleapis/java-firestore/issues/196)) ([a471f1e](https://www.github.com/googleapis/java-firestore/commit/a471f1eed1e555e95b3d9bcda31ce0277e35a14a))
* Create CODEOWNERS ([#207](https://www.github.com/googleapis/java-firestore/issues/207)) ([cd19eae](https://www.github.com/googleapis/java-firestore/commit/cd19eae68a4898a53c6c3cc8189eab30545a661d))


### Bug Fixes

* add RateLimiter ([#230](https://www.github.com/googleapis/java-firestore/issues/230)) ([47d4a11](https://www.github.com/googleapis/java-firestore/commit/47d4a11625d5888d6f31e494923853a08bb8af77))
* catch null Firestore in system tests ([#215](https://www.github.com/googleapis/java-firestore/issues/215)) ([2a4a7b5](https://www.github.com/googleapis/java-firestore/commit/2a4a7b50d40ff1c165e3d359d5f4eaf929f6ffbc))
* Fields used in whereIn should be equality filters ([#216](https://www.github.com/googleapis/java-firestore/issues/216)) ([4a62633](https://www.github.com/googleapis/java-firestore/commit/4a626333e5af0d70a4dc4853ed373dcf50ea0f4a))
* replace usages of transform proto with update_transform ([#213](https://www.github.com/googleapis/java-firestore/issues/213)) ([46a3c51](https://www.github.com/googleapis/java-firestore/commit/46a3c51386b57f20bd65c564e93181e9ce399e2b))
* support array of references for IN queries ([#211](https://www.github.com/googleapis/java-firestore/issues/211)) ([b376003](https://www.github.com/googleapis/java-firestore/commit/b3760032952529f148065928c3bf13ff73a34edd))


### Dependencies

* update core dependencies to v1.93.5 ([#229](https://www.github.com/googleapis/java-firestore/issues/229)) ([b078213](https://www.github.com/googleapis/java-firestore/commit/b078213209f3936cfe9c9e2cdea040c1262621d4))
* update dependency com.google.api:api-common to v1.9.1 ([#228](https://www.github.com/googleapis/java-firestore/issues/228)) ([7e4568d](https://www.github.com/googleapis/java-firestore/commit/7e4568d8b3f0fc6f591640ccc2d646eb2764e572))
* update dependency com.google.api.grpc:proto-google-common-protos to v1.18.0 ([#204](https://www.github.com/googleapis/java-firestore/issues/204)) ([1e05de4](https://www.github.com/googleapis/java-firestore/commit/1e05de4ecfde055a1c84c2f6dd338604b8580a61))
* update dependency com.google.cloud:google-cloud-conformance-tests to v0.0.10 ([#197](https://www.github.com/googleapis/java-firestore/issues/197)) ([69372af](https://www.github.com/googleapis/java-firestore/commit/69372af7253564691b291766e2bf4d80e9ecc770))
* update dependency com.google.guava:guava-bom to v29 ([#180](https://www.github.com/googleapis/java-firestore/issues/180)) ([3c204b4](https://www.github.com/googleapis/java-firestore/commit/3c204b42ddfbe435ac095368d1e695ed282280bd))
* update dependency io.grpc:grpc-bom to v1.29.0 ([#206](https://www.github.com/googleapis/java-firestore/issues/206)) ([5d8c50f](https://www.github.com/googleapis/java-firestore/commit/5d8c50f105649100abf4fa7a6882bb0469ccbf8f))
* update dependency org.threeten:threetenbp to v1.4.4 ([#194](https://www.github.com/googleapis/java-firestore/issues/194)) ([c867bd5](https://www.github.com/googleapis/java-firestore/commit/c867bd5772aa4a4710c622546e69fdc0f1ca22b6))
* update jackson dependencies to v2.11.0 ([#195](https://www.github.com/googleapis/java-firestore/issues/195)) ([5066812](https://www.github.com/googleapis/java-firestore/commit/50668126e99422cc9498b317c9c76a80a8bf7b30))
* update protobuf.version to v3.12.0 ([#220](https://www.github.com/googleapis/java-firestore/issues/220)) ([2c0b35d](https://www.github.com/googleapis/java-firestore/commit/2c0b35dfc5786b986b5301a00f06177f527496c3))
* update protobuf.version to v3.12.2 ([#226](https://www.github.com/googleapis/java-firestore/issues/226)) ([2eeea19](https://www.github.com/googleapis/java-firestore/commit/2eeea193d7eb54b1efa92b4d5dd996c170048a73))


### Documentation

* update README to include code formatting ([#209](https://www.github.com/googleapis/java-firestore/issues/209)) ([04f8b3b](https://www.github.com/googleapis/java-firestore/commit/04f8b3b0f873c2f1988c184de1e5268e0de9053f))

## [1.33.0](https://www.github.com/googleapis/java-firestore/compare/v1.32.5...v1.33.0) (2020-04-08)


### Features

* add new Firestore.runAsyncTransaction ([#103](https://www.github.com/googleapis/java-firestore/issues/103)) ([b28b660](https://www.github.com/googleapis/java-firestore/commit/b28b66088194f997cca62f759e4201cba3da38b5))
  * __NOTICE__ This change will require any users of the library that implement `com.google.cloud.firestore.Firestore` to have to implement the new runAsyncTransaction methods, and is a binary incompatible change in Java 7. Those users who only use `com.google.cloud.firestore.Firestore` through the instance returned from `FirestoreOptions.getDefaultInstance().getService()` will not have to recompile their code.
* add Query.limitToLast() ([#151](https://www.github.com/googleapis/java-firestore/issues/151)) ([c104615](https://www.github.com/googleapis/java-firestore/commit/c104615210271977a48205a8d8e6acd69acc5fb6))
* base transaction retries on error codes ([#129](https://www.github.com/googleapis/java-firestore/issues/129)) ([00b6eb3](https://www.github.com/googleapis/java-firestore/commit/00b6eb3703c8f4942d6da42b827ecbeeb9a13ef5)), closes [googleapis/nodejs-firestore#953](https://www.github.com/googleapis/nodejs-firestore/issues/953)
* use SecureRandom instead of Random to reduce the chance of auto-id collisions ([#156](https://www.github.com/googleapis/java-firestore/issues/156)) ([0088ee7](https://www.github.com/googleapis/java-firestore/commit/0088ee7b1f0c5bb65d7636de77e2c7f9098978e9))


### Bug Fixes

* add missing @InternalExtensionOnly annotation to com.google.cloud.firestore.Firestore ([#141](https://www.github.com/googleapis/java-firestore/issues/141)) ([d3458cb](https://www.github.com/googleapis/java-firestore/commit/d3458cbdae14e1f623dcb9848dd0fc51b8a8c30f))
* add support for updating an individual field with pojo in all update method ([#136](https://www.github.com/googleapis/java-firestore/issues/136)) ([7d6c2c1](https://www.github.com/googleapis/java-firestore/commit/7d6c2c10be4eb5bf7250de4bb0ea447302464d05))
* mark v1beta1 as deprecated ([#154](https://www.github.com/googleapis/java-firestore/issues/154)) ([495f7f9](https://www.github.com/googleapis/java-firestore/commit/495f7f97405fcd2bff4d09e67ddbeb51615ea843))


### Dependencies

* update core dependencies ([#127](https://www.github.com/googleapis/java-firestore/issues/127)) ([7995db0](https://www.github.com/googleapis/java-firestore/commit/7995db0d4e7ba38e7f28ce200de95c2069dd323e))
* update core dependencies ([#167](https://www.github.com/googleapis/java-firestore/issues/167)) ([11f16fd](https://www.github.com/googleapis/java-firestore/commit/11f16fd2b64dff0e7d97df66af4cfa722ce8f418))
* update core dependencies to v1.55.0 ([#158](https://www.github.com/googleapis/java-firestore/issues/158)) ([f3a20d3](https://www.github.com/googleapis/java-firestore/commit/f3a20d3d35c98c62d78ba6fcd6533878b28b7653))
* update core dependencies to v1.93.3 ([#132](https://www.github.com/googleapis/java-firestore/issues/132)) ([50394e1](https://www.github.com/googleapis/java-firestore/commit/50394e13fe58b86b13e65aea6b80135c3e9cfe44))
* update core dependencies to v1.93.4 ([#168](https://www.github.com/googleapis/java-firestore/issues/168)) ([2118232](https://www.github.com/googleapis/java-firestore/commit/211823214cc4cceea86048e2fe8fbdc4e6571b49))
* update dependency com.google.api:api-common to v1.9.0 ([#153](https://www.github.com/googleapis/java-firestore/issues/153)) ([8ca0ea8](https://www.github.com/googleapis/java-firestore/commit/8ca0ea8d30f3318a89d6efec420b65f2b41dc805))
* update dependency com.google.cloud:google-cloud-conformance-tests to v0.0.8 ([#123](https://www.github.com/googleapis/java-firestore/issues/123)) ([0bbf4b0](https://www.github.com/googleapis/java-firestore/commit/0bbf4b03f722ca45a7c7f5466e19dd490eda94af))
* update dependency com.google.cloud:google-cloud-conformance-tests to v0.0.9 ([#152](https://www.github.com/googleapis/java-firestore/issues/152)) ([d41c41f](https://www.github.com/googleapis/java-firestore/commit/d41c41f6e27d66af133b89155474404fd392245f))
* update dependency com.google.cloud.samples:shared-configuration to v1.0.13 ([#148](https://www.github.com/googleapis/java-firestore/issues/148)) ([6bd8e7f](https://www.github.com/googleapis/java-firestore/commit/6bd8e7fcc126872c635714a6d2edbecbbde55ea8))
* update dependency org.threeten:threetenbp to v1.4.2 ([#142](https://www.github.com/googleapis/java-firestore/issues/142)) ([41b2a9a](https://www.github.com/googleapis/java-firestore/commit/41b2a9a8c556ddcdbe7ef244f0ae0e012546e09c))
* update dependency org.threeten:threetenbp to v1.4.3 ([#165](https://www.github.com/googleapis/java-firestore/issues/165)) ([d8bfa80](https://www.github.com/googleapis/java-firestore/commit/d8bfa801a855a2079fe42cec234a685bc35288a9))

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
