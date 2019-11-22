# Changelog

## [2.0.0](https://www.github.com/googleapis/java-firestore/compare/v1.31.0...v2.0.0) (2019-11-22)


### ⚠ BREAKING CHANGES

* The `areTimestampsInSnapshotsEnabled()` setting is now enabled
by default so timestamp fields read from a `DocumentSnapshot` will be returned
as `Timestamp` objects instead of `Date`. Any code expecting to receive a
`Date` object must be updated.

### Features

* **firestore:** add support for IN & ARRAY_CONTAINS_ANY query field filters ([#5803](https://www.github.com/googleapis/java-firestore/issues/5803)) ([76a7e46](https://www.github.com/googleapis/java-firestore/commit/76a7e466c837f2d9dfde9be01c0b78668b393494)), closes [/github.com/googleapis/googleapis/blob/10f91fa12f70e8e0209a45fc10807ed1f77c7e4e/google/firestore/v1/query.proto#L106-L112](https://www.github.com/googleapis//github.com/googleapis/googleapis/blob/10f91fa12f70e8e0209a45fc10807ed1f77c7e4e/google/firestore/v1/query.proto/issues/L106-L112)


### Firestore

* change timestampsInSnapshots default to true. ([#4353](https://www.github.com/googleapis/java-firestore/issues/4353)) ([66539a8](https://www.github.com/googleapis/java-firestore/commit/66539a85f8f9c8e476b2b99e9335309ae86a8cdc))
