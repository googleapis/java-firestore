# Changelog

### [3.0.8](https://www.github.com/googleapis/java-firestore/compare/v3.0.7...v3.0.8) (2021-11-16)


### Bug Fixes

* respect total request timeout for Query retries ([#806](https://www.github.com/googleapis/java-firestore/issues/806)) ([feb1921](https://www.github.com/googleapis/java-firestore/commit/feb1921b39fc7630aa6549025c5ffe526e080d54))


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v2.5.0 ([#814](https://www.github.com/googleapis/java-firestore/issues/814)) ([c1f4dac](https://www.github.com/googleapis/java-firestore/commit/c1f4dac67230b77864b4c4a64982cbbd7e28369f))

### [3.0.7](https://www.github.com/googleapis/java-firestore/compare/v3.0.6...v3.0.7) (2021-10-27)


### Bug Fixes

* **java:** java 17 dependency arguments ([#802](https://www.github.com/googleapis/java-firestore/issues/802)) ([ec07294](https://www.github.com/googleapis/java-firestore/commit/ec07294dc67d884a8bd6b0c4c9b9b1cdfb6e95c3))
* set timeouts for BatchGetDocuments/RunQuery ([#799](https://www.github.com/googleapis/java-firestore/issues/799)) ([6cd2a45](https://www.github.com/googleapis/java-firestore/commit/6cd2a4526561a74f1aed355fa8d0405960b34965))

### [3.0.6](https://www.github.com/googleapis/java-firestore/compare/v3.0.5...v3.0.6) (2021-10-19)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v2.4.0 ([#793](https://www.github.com/googleapis/java-firestore/issues/793)) ([1af36a9](https://www.github.com/googleapis/java-firestore/commit/1af36a9e8eccc5edc4ccfbfd865c2966b0fab366))

### [3.0.5](https://www.github.com/googleapis/java-firestore/compare/v3.0.4...v3.0.5) (2021-09-22)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v2.3.0 ([#775](https://www.github.com/googleapis/java-firestore/issues/775)) ([35469d6](https://www.github.com/googleapis/java-firestore/commit/35469d6f83a8027f2f54ce16b78141811ff393c3))

### [3.0.4](https://www.github.com/googleapis/java-firestore/compare/v3.0.3...v3.0.4) (2021-09-14)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v2.2.1 ([#765](https://www.github.com/googleapis/java-firestore/issues/765)) ([e7a6537](https://www.github.com/googleapis/java-firestore/commit/e7a65374525da1b290835f6d6c7ec94d9cbde121))

### [3.0.3](https://www.github.com/googleapis/java-firestore/compare/v3.0.2...v3.0.3) (2021-08-31)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v2.2.0 ([#753](https://www.github.com/googleapis/java-firestore/issues/753)) ([6744bfa](https://www.github.com/googleapis/java-firestore/commit/6744bfad1dc8a6a4e1da2d3e7cb7cd17514259b7))
* update google-cloud-conformance-tests to 0.2.0 ([#752](https://www.github.com/googleapis/java-firestore/issues/752)) ([135bc6b](https://www.github.com/googleapis/java-firestore/commit/135bc6be06fff41fb8933b63fcde2f247a293b07))

### [3.0.2](https://www.github.com/googleapis/java-firestore/compare/v3.0.1...v3.0.2) (2021-08-23)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v2.1.0 ([#739](https://www.github.com/googleapis/java-firestore/issues/739)) ([99b5820](https://www.github.com/googleapis/java-firestore/commit/99b5820f28c36ac83c44c6547764c7e41e4a8031))

### [3.0.1](https://www.github.com/googleapis/java-firestore/compare/v3.0.0...v3.0.1) (2021-08-11)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v2.0.1 ([#728](https://www.github.com/googleapis/java-firestore/issues/728)) ([167683d](https://www.github.com/googleapis/java-firestore/commit/167683deebf4724795b013cc7d8b495239a918a6))

## [3.0.0](https://www.github.com/googleapis/java-firestore/compare/v2.6.2...v3.0.0) (2021-08-09)


### ⚠ BREAKING CHANGES

* update Java compile to 1.8 ([#710](https://github.com/googleapis/java-firestore/pull/710))
* update gapic generator to 2.0.0, java 8 features (#719)

### Features

* update Java compile to 1.8 ([#710](https://github.com/googleapis/java-firestore/pull/710))
* gapic-generator-java v1.0.13, mtls support ([#712](https://www.github.com/googleapis/java-firestore/issues/712)) ([a27dd9c](https://www.github.com/googleapis/java-firestore/commit/a27dd9cb0e2681a15eae754c97255d0d8c86bcae))
* update gapic generator to 2.0.0, java 8 features ([#719](https://www.github.com/googleapis/java-firestore/issues/719)) ([0d5b378](https://www.github.com/googleapis/java-firestore/commit/0d5b378075f3723126c8809f4afcc8fc9f81d611))


### Dependencies

* update shared dependencies to 2.0.0 ([#720](https://www.github.com/googleapis/java-firestore/issues/720)) ([e4f985e](https://www.github.com/googleapis/java-firestore/commit/e4f985e8b17a02f84aa9b100be1aa72f97521763))

### [2.6.2](https://www.github.com/googleapis/java-firestore/compare/v2.6.1...v2.6.2) (2021-07-29)


### Bug Fixes

* Add shopt -s nullglob to dependencies script ([8f4b199](https://www.github.com/googleapis/java-firestore/commit/8f4b199c1dfdf268723e11696733fe5fb6bd5c64))
* Ensures bundles are encoded as UTF8 bytes. ([#695](https://www.github.com/googleapis/java-firestore/issues/695)) ([0946a17](https://www.github.com/googleapis/java-firestore/commit/0946a170a963f50ec77409291c02696f2c416edb))
* lower batch size on BulkWriter retry ([#688](https://www.github.com/googleapis/java-firestore/issues/688)) ([146b21d](https://www.github.com/googleapis/java-firestore/commit/146b21dd6d5772bfd9e023dbf5a1147b29076cdd))
* Update dependencies.sh to not break on mac ([#694](https://www.github.com/googleapis/java-firestore/issues/694)) ([8f4b199](https://www.github.com/googleapis/java-firestore/commit/8f4b199c1dfdf268723e11696733fe5fb6bd5c64))


### Documentation

* began merging variant client samples ([#696](https://www.github.com/googleapis/java-firestore/issues/696)) ([0a10dd8](https://www.github.com/googleapis/java-firestore/commit/0a10dd85de02647a9d08f41d45ebc25ee2689a52))

### [2.6.1](https://www.github.com/googleapis/java-firestore/compare/v2.6.0...v2.6.1) (2021-06-30)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v1.4.0 ([#680](https://www.github.com/googleapis/java-firestore/issues/680)) ([b8ffcb4](https://www.github.com/googleapis/java-firestore/commit/b8ffcb43d79029f9b2a490fb84e78653c716b367))

## [2.6.0](https://www.github.com/googleapis/java-firestore/compare/v2.5.1...v2.6.0) (2021-06-29)


### Features

* add shutdown() and shutdownNow() ([#673](https://www.github.com/googleapis/java-firestore/issues/673)) ([4f20858](https://www.github.com/googleapis/java-firestore/commit/4f2085887981121316fc046d13e945e6800aaad1))


### Bug Fixes

* handle thrown exceptions in runAsyncTransaction callback ([#671](https://www.github.com/googleapis/java-firestore/issues/671)) ([969f7fd](https://www.github.com/googleapis/java-firestore/commit/969f7fd72b07aa9c916609b73528ff0f17dfaead))

### [2.5.1](https://www.github.com/googleapis/java-firestore/compare/v2.5.0...v2.5.1) (2021-06-22)


### Bug Fixes

* return results from getPartitions() in order ([#653](https://www.github.com/googleapis/java-firestore/issues/653)) ([12d17d1](https://www.github.com/googleapis/java-firestore/commit/12d17d1ac9d7a1c21eca1469164b079de4476633))


### Dependencies

* update dependency com.google.cloud:google-cloud-conformance-tests to v0.1.1 ([#650](https://www.github.com/googleapis/java-firestore/issues/650)) ([b93ca8a](https://www.github.com/googleapis/java-firestore/commit/b93ca8a2b5751c61b3fbe0ca608056e2c0398575))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v1.3.0 ([#660](https://www.github.com/googleapis/java-firestore/issues/660)) ([0f13fd0](https://www.github.com/googleapis/java-firestore/commit/0f13fd0c0db0208b9f68a57dabcb1e998b4a7b9b))

## [2.5.0](https://www.github.com/googleapis/java-firestore/compare/v2.4.0...v2.5.0) (2021-06-01)


### Features

* add `gcf-owl-bot[bot]` to `ignoreAuthors` ([#641](https://www.github.com/googleapis/java-firestore/issues/641)) ([6f40f0f](https://www.github.com/googleapis/java-firestore/commit/6f40f0fc821d31136686a41e82e4b57d0ad3dede))
* add recursiveDelete() to Firestore ([#622](https://www.github.com/googleapis/java-firestore/issues/622)) ([#649](https://www.github.com/googleapis/java-firestore/issues/649)) ([9ff2f41](https://www.github.com/googleapis/java-firestore/commit/9ff2f41b765c8878c3b3fb7df962f6f1ed537f05))


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v1.2.0 ([#640](https://www.github.com/googleapis/java-firestore/issues/640)) ([9bd881a](https://www.github.com/googleapis/java-firestore/commit/9bd881a5ddd0bdb62396e8b778bf9784c00feba2))

## [2.4.0](https://www.github.com/googleapis/java-firestore/compare/v2.3.0...v2.4.0) (2021-05-17)


### Features

* add buffering layer to BulkWriter ([#611](https://www.github.com/googleapis/java-firestore/issues/611)) ([a7caff2](https://www.github.com/googleapis/java-firestore/commit/a7caff2c15ad8ad4e98165bf4029d9615c079637))


### Bug Fixes

* add flush check in BulkWriter.sendBatchLocked() callback ([#631](https://www.github.com/googleapis/java-firestore/issues/631)) ([ac3db81](https://www.github.com/googleapis/java-firestore/commit/ac3db81741ac74614d9eeae70a3f2eae6dcc9548))


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v1.1.0 ([#626](https://www.github.com/googleapis/java-firestore/issues/626)) ([00dbe33](https://www.github.com/googleapis/java-firestore/commit/00dbe33b426ea1509f9fb72181a36dd1a83b15cc))

## [2.3.0](https://www.github.com/googleapis/java-firestore/compare/v2.2.7...v2.3.0) (2021-04-23)


### Features

* add backoff to BulkWriter ([#600](https://www.github.com/googleapis/java-firestore/issues/600)) ([e295aa5](https://www.github.com/googleapis/java-firestore/commit/e295aa5d20007a513e1647575f6935e243825c4d))


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v1 ([#607](https://www.github.com/googleapis/java-firestore/issues/607)) ([21e8cde](https://www.github.com/googleapis/java-firestore/commit/21e8cde718b5f2e2f8269d860d0ea3ae810dabdd))

### [2.2.7](https://www.github.com/googleapis/java-firestore/compare/v2.2.6...v2.2.7) (2021-04-20)


### Bug Fixes

* release scripts from issuing overlapping phases ([#595](https://www.github.com/googleapis/java-firestore/issues/595)) ([4372479](https://www.github.com/googleapis/java-firestore/commit/43724796ea940062512370c270f6bedc680b5b6a))


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.21.1 ([#597](https://www.github.com/googleapis/java-firestore/issues/597)) ([dbd3d33](https://www.github.com/googleapis/java-firestore/commit/dbd3d331d83fc7b2c4e79946b9921ff158c8b1e9))

### [2.2.6](https://www.github.com/googleapis/java-firestore/compare/v2.2.5...v2.2.6) (2021-04-13)


### Bug Fixes

* Removed Jackson dependency ([#508](https://www.github.com/googleapis/java-firestore/issues/508)) ([7ada73d](https://www.github.com/googleapis/java-firestore/commit/7ada73d3c0a282be8ce2682c2e9c737690d9f891))
* typo ([#587](https://www.github.com/googleapis/java-firestore/issues/587)) ([520ac44](https://www.github.com/googleapis/java-firestore/commit/520ac441bcb7944be378ec6099b9ec4de37844d4))


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.21.0 ([#584](https://www.github.com/googleapis/java-firestore/issues/584)) ([c83a222](https://www.github.com/googleapis/java-firestore/commit/c83a222bd602a489552276c5202796db7fadeffa))

### [2.2.5](https://www.github.com/googleapis/java-firestore/compare/v2.2.4...v2.2.5) (2021-03-11)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.20.1 ([#567](https://www.github.com/googleapis/java-firestore/issues/567)) ([7370d21](https://www.github.com/googleapis/java-firestore/commit/7370d21994126d206f51a8c0c1677b5dc0ee030f))

### [2.2.4](https://www.github.com/googleapis/java-firestore/compare/v2.2.3...v2.2.4) (2021-02-25)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.20.0 ([#554](https://www.github.com/googleapis/java-firestore/issues/554)) ([0ae4ea3](https://www.github.com/googleapis/java-firestore/commit/0ae4ea3ce4bdd3a361331615115bdf25928b7da6))

### [2.2.3](https://www.github.com/googleapis/java-firestore/compare/v2.2.2...v2.2.3) (2021-02-19)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.19.0 ([#543](https://www.github.com/googleapis/java-firestore/issues/543)) ([7eed88c](https://www.github.com/googleapis/java-firestore/commit/7eed88c70cfe197a93b88b47db6e4edd89836063))

### [2.2.2](https://www.github.com/googleapis/java-firestore/compare/v2.2.1...v2.2.2) (2021-02-19)


### Bug Fixes

* block close() on the termination of all RPC channels ([#512](https://www.github.com/googleapis/java-firestore/issues/512)) ([67bd06a](https://www.github.com/googleapis/java-firestore/commit/67bd06aac7a53a210e1a5c081929104c43a5d48a))
* **samples:** close firestore instance in test ([#476](https://www.github.com/googleapis/java-firestore/issues/476)) ([fb1cb17](https://www.github.com/googleapis/java-firestore/commit/fb1cb175c06e2782f7cd9cb3bbb9c751e9d33713))

### [2.2.1](https://www.github.com/googleapis/java-firestore/compare/v2.2.0...v2.2.1) (2021-02-02)


### Bug Fixes

* don't throw NPE in DocumentSnapshot.getDate() when value is missing ([#513](https://www.github.com/googleapis/java-firestore/issues/513)) ([a41b07e](https://www.github.com/googleapis/java-firestore/commit/a41b07ea8a02aefb9f2798240dd7ba32589791fe))

## [2.2.0](https://www.github.com/googleapis/java-firestore/compare/v2.1.0...v2.2.0) (2021-01-20)


### Features

* Add bundle proto building ([#271](https://www.github.com/googleapis/java-firestore/issues/271)) ([994835c](https://www.github.com/googleapis/java-firestore/commit/994835c0a3be077404afa60abd4d4685d17fb533))
* add bundle.proto from googleapis/googleapis ([#407](https://www.github.com/googleapis/java-firestore/issues/407)) ([37da386](https://www.github.com/googleapis/java-firestore/commit/37da386875d1b65121e8a9a92b1a000537f07625))
* add CollectionGroup#getPartitions(long) ([#478](https://www.github.com/googleapis/java-firestore/issues/478)) ([bab064e](https://www.github.com/googleapis/java-firestore/commit/bab064edde26325bf0041ffe28d4c63b97a089c5))
* add implicit ordering for startAt(DocumentReference) calls ([#417](https://www.github.com/googleapis/java-firestore/issues/417)) ([aae6dc9](https://www.github.com/googleapis/java-firestore/commit/aae6dc960f7c42830ceed23c65acaad3e457dcff))
* add max/min throttling options to BulkWriterOptions ([#400](https://www.github.com/googleapis/java-firestore/issues/400)) ([27a9397](https://www.github.com/googleapis/java-firestore/commit/27a9397f67e151d723241c80ccb2ec9f1bfbba1c))
* add success and error callbacks to BulkWriter ([#483](https://www.github.com/googleapis/java-firestore/issues/483)) ([3c05037](https://www.github.com/googleapis/java-firestore/commit/3c05037e8fce8d3ce4907fde85bd254fc98ea588))
* Implementation of Firestore Bundle Builder ([#293](https://www.github.com/googleapis/java-firestore/issues/293)) ([fd5ef90](https://www.github.com/googleapis/java-firestore/commit/fd5ef90b6681cc67aeee6c95f3de80267798dcd0))
* Release bundles ([#466](https://www.github.com/googleapis/java-firestore/issues/466)) ([3af065e](https://www.github.com/googleapis/java-firestore/commit/3af065e32b193931c805b576f410ad90124b43a7))


### Bug Fixes

* add @BetaApi, make BulkWriter public, and refactor Executor ([#497](https://www.github.com/googleapis/java-firestore/issues/497)) ([27ff9f6](https://www.github.com/googleapis/java-firestore/commit/27ff9f6dfa92cac9119d2014c24a0759baa44fb7))
* **build:** sample checkstyle violations ([#457](https://www.github.com/googleapis/java-firestore/issues/457)) ([777ecab](https://www.github.com/googleapis/java-firestore/commit/777ecabd1ce12cbc5f4169de6c23a90f98deac06))
* bulkWriter: writing to the same doc doesn't create a new batch ([#394](https://www.github.com/googleapis/java-firestore/issues/394)) ([259ece8](https://www.github.com/googleapis/java-firestore/commit/259ece8511db71ea79cc1a080eb785a15db88756))
* empty commit to trigger release-please ([fcef0d3](https://www.github.com/googleapis/java-firestore/commit/fcef0d302cd0a9339d82db73152289d6f9f67ff2))
* make BulkWriterOptions public ([#502](https://www.github.com/googleapis/java-firestore/issues/502)) ([6ea05be](https://www.github.com/googleapis/java-firestore/commit/6ea05beb3f27337bef910ca64f0e3f32de6b7e98))
* retry Query streams ([#426](https://www.github.com/googleapis/java-firestore/issues/426)) ([3513cd3](https://www.github.com/googleapis/java-firestore/commit/3513cd39ff43d26c8432c05ce20693350539ae8f))
* retry transactions that fail with expired transaction IDs ([#447](https://www.github.com/googleapis/java-firestore/issues/447)) ([5905438](https://www.github.com/googleapis/java-firestore/commit/5905438af6501353e978210808834a26947aae95))
* verify partition count before invoking GetPartition RPC ([#418](https://www.github.com/googleapis/java-firestore/issues/418)) ([2054ae9](https://www.github.com/googleapis/java-firestore/commit/2054ae971083277e1cf81c2b57500c40a6faa0ef))


### Documentation

* **sample:** normalize firestore sample's region tags ([#453](https://www.github.com/googleapis/java-firestore/issues/453)) ([b529245](https://www.github.com/googleapis/java-firestore/commit/b529245c75f770e1b47ca5d9561bab55a7610677))


### Dependencies

* remove explicit version for jackson ([#479](https://www.github.com/googleapis/java-firestore/issues/479)) ([e2aecfe](https://www.github.com/googleapis/java-firestore/commit/e2aecfec51465b8fb3413337a76f9a3de57b8500))
* update dependency com.google.cloud:google-cloud-conformance-tests to v0.0.12 ([#367](https://www.github.com/googleapis/java-firestore/issues/367)) ([2bdd846](https://www.github.com/googleapis/java-firestore/commit/2bdd84693bbd968cafabd0e7ee56d1a9a7dc31ca))
* update dependency com.google.cloud:google-cloud-conformance-tests to v0.0.13 ([#411](https://www.github.com/googleapis/java-firestore/issues/411)) ([e6157b5](https://www.github.com/googleapis/java-firestore/commit/e6157b5cb532e0184125355b12115058e72afa67))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.10.0 ([#383](https://www.github.com/googleapis/java-firestore/issues/383)) ([cb39ee8](https://www.github.com/googleapis/java-firestore/commit/cb39ee820c2f67e22da623f5a6eaa7ee6bf351e2))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.10.2 ([#403](https://www.github.com/googleapis/java-firestore/issues/403)) ([991dd81](https://www.github.com/googleapis/java-firestore/commit/991dd810360e654fca0b53e0611da0cd77febc7c))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.12.1 ([#425](https://www.github.com/googleapis/java-firestore/issues/425)) ([b897ffa](https://www.github.com/googleapis/java-firestore/commit/b897ffa90427a8f597c02c24f80d1d162be48b23))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.13.0 ([#430](https://www.github.com/googleapis/java-firestore/issues/430)) ([0f8f218](https://www.github.com/googleapis/java-firestore/commit/0f8f218678c3ddebb73748c382cab8e38c2f140d))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.14.1 ([#446](https://www.github.com/googleapis/java-firestore/issues/446)) ([e241f8e](https://www.github.com/googleapis/java-firestore/commit/e241f8ebbfdf202f00424177c69962311b37fc88))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.15.0 ([#460](https://www.github.com/googleapis/java-firestore/issues/460)) ([b82fc35](https://www.github.com/googleapis/java-firestore/commit/b82fc3561d1a397438829ab69df24141481369a2))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.16.0 ([#481](https://www.github.com/googleapis/java-firestore/issues/481)) ([ae98824](https://www.github.com/googleapis/java-firestore/commit/ae988245e6d6391c85414e9b6f7ae1b8148c3a6d))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.16.1 ([4ace93c](https://www.github.com/googleapis/java-firestore/commit/4ace93c7be580a8f7870e71cad2dc19bb5fdef29))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.17.0 ([#487](https://www.github.com/googleapis/java-firestore/issues/487)) ([e11e472](https://www.github.com/googleapis/java-firestore/commit/e11e4723bc75727086bee0436492f458def29ff5))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.18.0 ([#495](https://www.github.com/googleapis/java-firestore/issues/495)) ([f78720a](https://www.github.com/googleapis/java-firestore/commit/f78720a155f1294321f05266b9a546bbf2cb9a04))
* update jackson dependencies to v2.11.3 ([#396](https://www.github.com/googleapis/java-firestore/issues/396)) ([2e176e2](https://www.github.com/googleapis/java-firestore/commit/2e176e2f864262f31e6f93705fa7e794023b9649))

## [2.1.0](https://www.github.com/googleapis/java-firestore/compare/v2.0.0...v2.1.0) (2020-09-10)


### Features

* add method to set emulator host programmatically ([#319](https://www.github.com/googleapis/java-firestore/issues/319)) ([#336](https://www.github.com/googleapis/java-firestore/issues/336)) ([97037f4](https://www.github.com/googleapis/java-firestore/commit/97037f42f76e9df3ae458d4e2b04336e64b834c3)), closes [#210](https://www.github.com/googleapis/java-firestore/issues/210) [#190](https://www.github.com/googleapis/java-firestore/issues/190)
* add opencensus tracing support ([#360](https://www.github.com/googleapis/java-firestore/issues/360)) ([edaa539](https://www.github.com/googleapis/java-firestore/commit/edaa5395be0353fb261d954429c624623bc4e346))
* add support for != and NOT_IN queries ([#350](https://www.github.com/googleapis/java-firestore/issues/350)) ([68aff5b](https://www.github.com/googleapis/java-firestore/commit/68aff5b406fb2732951750f3d5f9768df6ee12b5))
* generate protos to add NOT_EQUAL, NOT_IN, IS_NOT_NAN, IS_NOT_NULL query operators ([#343](https://www.github.com/googleapis/java-firestore/issues/343)) ([3fb1b63](https://www.github.com/googleapis/java-firestore/commit/3fb1b631f8dd087f0f3e1c43363e9642f497993a))


### Bug Fixes

* **samples:** re-add maven exec config for Quickstart sample ([#347](https://www.github.com/googleapis/java-firestore/issues/347)) ([4c2329b](https://www.github.com/googleapis/java-firestore/commit/4c2329bf89ffab4bd3060e16e1cf231b7caf4653))
* add support to deserialize to custom Lists and Maps ([#337](https://www.github.com/googleapis/java-firestore/issues/337)) ([dc897e0](https://www.github.com/googleapis/java-firestore/commit/dc897e00a85e745f57f615460b29d17b7dd247c6))


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.9.0 ([#352](https://www.github.com/googleapis/java-firestore/issues/352)) ([783d41e](https://www.github.com/googleapis/java-firestore/commit/783d41e167c7c79957faeeebd7a76ab72b5b157d))

## [2.0.0](https://www.github.com/googleapis/java-firestore/compare/v1.35.2...v2.0.0) (2020-08-14)


### New Features

#### Query Partition API

New API and backend RPC which allows for fetching a set of cursor keys for a 
Collection Group Query. Accessible via the new [`CollectionGroup#getPartitions(long,ApiStreamObserver)`](https://googleapis.dev/java/google-cloud-firestore/2.0.0/com/google/cloud/firestore/CollectionGroup.html#getPartitions-long-com.google.api.gax.rpc.ApiStreamObserver-) method. 

#### Read-Only Transaction Options

[`TransactionOptions`](https://googleapis.dev/java/google-cloud-firestore/2.0.0/com/google/cloud/firestore/TransactionOptions.html)
has been refactored to provide the ability to configure options for read-only 
transactions along with the existing configuration for read-write transactions.

This new ability is provided via the new [`TransactionOptions.createReadOnlyOptionsBuilder()`](https://googleapis.dev/java/google-cloud-firestore/2.0.0/com/google/cloud/firestore/TransactionOptions.html#createReadOnlyOptionsBuilder--) 
type safe builder.

Along with the new type safe builder for read-only options, there is a new type 
safe builder for read-write options as well accessible via [`TransactionOptions.createReadWriteOptionsBuilder()`](https://googleapis.dev/java/google-cloud-firestore/2.0.0/com/google/cloud/firestore/TransactionOptions.html#createReadWriteOptionsBuilder--). Each of the existing `TransactionOptions.create(...)`
methods for configuring read-write options has been deprecated in favor of the new builder.

#### EmulatorCredentials

`com.google.cloud.firestore.FirestoreOptions.Builder.FakeCredentials` has been
made static and renamed to `com.google.cloud.firestore.FirestoreOptions.EmulatorCredentials`
allowing instantiation outside `FirestoreOptions.Builder`.

When connecting to the Cloud Firestore Emulator via `FirestoreOptions` rather than
the environment variable `FIRESTORE_EMULATOR_HOST`, a custom credential implementation
must be specified to allow various admin operations in the emulator. Previously
this required users to create their own implementation due to it not being 
possible to construct a `FakeCredential`. As part of this change, `EmulatorCredentials`
is static and therefore able to be constructed from any location.

### Breaking Changes

#### New Firestore Admin Client API Artifact

The Cloud Firestore Admin Client has been migrated to its own maven artifact `com.google.cloud:google-cloud-firestore-admin`
rather than being bundled in `com.google.cloud:google-cloud-firestore`. All 
packages and classes have retained their existing names.

The new artifact is included in the `com.google.cloud:google-cloud-firestore-bom`, 
`com.google.cloud:google-cloud-bom` and `com.google.cloud:libraries-bom` 
artifacts and is accessible by adding the new dependency to your `pom.xml` file:

```xml
<dependency>
  <groupId>com.google.cloud</groupId>
  <artifactId>google-cloud-firestore-admin</artifactId>
</dependency>
```

#### Removal of v1beta1

Cloud Firestore has been GA for some time now, and the `google-cloud-firestore` 
code base has been using the protos and generated classes for the v1 api since 
that time. As such, we will no longer be publishing artifacts for the deprecated
v1beta1 protos. All functionality from v1beta1 is present in v1, and all users
should update any code to use v1.

#### Removal of support for `java.util.Date` in Snapshots

It is no longer possible to configure the ability for `java.util.Date` to be
returned from `DocumentSnapshot.get(FieldPath)` or `DocumentSnapshot.getData()`
for properties which are stored as Timestamps in Cloud Firestore.

The default behavior has been to return `com.google.cloud.Timestamp` by default
for some time, and is now the only option. Any code that is dependent on the old
behavior must be updated to use Timestamps instead of Date.

### Laundry List of Pull Requests

#### ⚠ BREAKING CHANGES

* add support for the Query Partition API (#202)
  * `Firestore#collectionGroup(...)` has a new return type `CollectionGroup` 
    which requires any code that previously used the method be re-compiled to
    pick up the new signature. `CollectionGroup` extends `Query` and as such 
    does not require your code to be updated, only the compiled class files.
* move FirestoreAdminClient and associated classes to new artifact google-cloud-firestore-admin (#311)
* remove deprecated v1beta1 protos and grpc client (#305)
* remove deprecated FirestoreOptions#setTimestampsInSnapshotsEnabled (#308)
* remove deprecated getCollections() methods (#307)
* various renames due to generator changes

#### Features

* add support for read-only transactions in TransactionOptions ([#320](https://www.github.com/googleapis/java-firestore/issues/320)) ([c25dca3](https://www.github.com/googleapis/java-firestore/commit/c25dca3ed6ca0c156ec60569ebc9f3a481bd4fee))
* add support for the Query Partition API ([#202](https://www.github.com/googleapis/java-firestore/issues/202)) ([3996548](https://www.github.com/googleapis/java-firestore/commit/39965489cbc836af573e500d57007c88241d7eb6))


#### Bug Fixes

* refactor FakeCredentials ([#325](https://www.github.com/googleapis/java-firestore/issues/325)) ([269e62c](https://www.github.com/googleapis/java-firestore/commit/269e62c6b8031d48e7f2e282b09b5ffcfadae547)), closes [#190](https://www.github.com/googleapis/java-firestore/issues/190)


#### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.8.5 ([#322](https://www.github.com/googleapis/java-firestore/issues/322)) ([1b21350](https://www.github.com/googleapis/java-firestore/commit/1b21350c0bc4a21cee2b281f944cbd061b1f8898))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.8.6 ([#324](https://www.github.com/googleapis/java-firestore/issues/324)) ([b945fdb](https://www.github.com/googleapis/java-firestore/commit/b945fdb04da76a1e007d012c809449c5a43bb990))
* update jackson dependencies to v2.11.2 ([#314](https://www.github.com/googleapis/java-firestore/issues/314)) ([15d68cd](https://www.github.com/googleapis/java-firestore/commit/15d68cd93ac1fd206895fd37155a9ba82b9196ca))


#### Miscellaneous Chores

* enable gapicv2 ([#188](https://www.github.com/googleapis/java-firestore/issues/188)) ([92224bc](https://www.github.com/googleapis/java-firestore/commit/92224bcd52aa88cc6eb1da28747de0535d776a0f))
* move FirestoreAdminClient and associated classes to new artifact google-cloud-firestore-admin ([#311](https://www.github.com/googleapis/java-firestore/issues/311)) ([03ef755](https://www.github.com/googleapis/java-firestore/commit/03ef755dd164e6f1ec749f3f985b913b5ae23d14))
* remove deprecated FirestoreOptions#setTimestampsInSnapshotsEnabled ([#308](https://www.github.com/googleapis/java-firestore/issues/308)) ([7255a42](https://www.github.com/googleapis/java-firestore/commit/7255a42bcee3a6938dd5fafaef3465f948f39600))
* remove deprecated getCollections() methods ([#307](https://www.github.com/googleapis/java-firestore/issues/307)) ([bb4ddf1](https://www.github.com/googleapis/java-firestore/commit/bb4ddf1ce3cc3bd2e06a4ad5097bd18060e4467b))
* remove deprecated v1beta1 protos and grpc client ([#305](https://www.github.com/googleapis/java-firestore/issues/305)) ([96adacb](https://www.github.com/googleapis/java-firestore/commit/96adacbf52ace27e54b7a210d7c73b46922fbcbd))
* add BulkWriter ([#323](https://www.github.com/googleapis/java-firestore/issues/323)) ([e7054df](https://www.github.com/googleapis/java-firestore/commit/e7054df79b4139fdfd0cc6aa0620fbfa1a10a6b0))
* make BulkWriter package private ([#330](https://github.com/googleapis/java-firestore/pull/330)) ([ef0869a](https://github.com/googleapis/java-firestore/commit/ef0869a7fa619bc15fef27ad90d41cb718cb981d))

## [1.35.2](https://www.github.com/googleapis/java-firestore/compare/v1.35.1...v1.35.2) (2020-07-16)


### Bug Fixes

* add Internal#autoId() ([#292](https://www.github.com/googleapis/java-firestore/issues/292)) ([b91c57c](https://www.github.com/googleapis/java-firestore/commit/b91c57c4b2d3e92478ceaa1a39d467c40e1344dc))
* add support for deleting nested fields that contain periods ([#295](https://www.github.com/googleapis/java-firestore/issues/295)) ([84f602e](https://www.github.com/googleapis/java-firestore/commit/84f602ef8be67e5748b77e549d46ea53d0c74335))
* use test credentials when connecting to the Emulator from the Firebase Admin SDK ([#296](https://www.github.com/googleapis/java-firestore/issues/296)) ([a0a6e80](https://www.github.com/googleapis/java-firestore/commit/a0a6e806217693fc62a4cf432354c76e719aa140))


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.8.3 ([#289](https://www.github.com/googleapis/java-firestore/issues/289)) ([2ddb8f1](https://www.github.com/googleapis/java-firestore/commit/2ddb8f133dd3bf31d28bf6bd67cddf8ba2e8846b))

## [1.35.1](https://www.github.com/googleapis/java-firestore/compare/v1.35.0...v1.35.1) (2020-07-01)

### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.8.2 ([#280](https://www.github.com/googleapis/java-firestore/issues/280)) ([9296f7a](https://www.github.com/googleapis/java-firestore/commit/9296f7a67c6194ea9e75b952d29b0f1aa486d194))
* update jackson dependencies to v2.11.1 ([#272](https://www.github.com/googleapis/java-firestore/issues/272)) ([1065704](https://www.github.com/googleapis/java-firestore/commit/1065704f3e571f3f51b0e9ed13ec67eb7c662b88))
* adopt flatten plugin and google-cloud-shared-dependencies ([#261](https://www.github.com/googleapis/java-firestore/issues/261)) ([78b2ebf](https://www.github.com/googleapis/java-firestore/commit/78b2ebf3c75c774f1ace386fa5f32e76b470ed9c))

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
