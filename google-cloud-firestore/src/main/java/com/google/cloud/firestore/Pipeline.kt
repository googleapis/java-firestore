package com.google.cloud.firestore

import com.google.cloud.firestore.pipeline.Collection
import com.google.cloud.firestore.pipeline.CollectionGroup
import com.google.cloud.firestore.pipeline.Database
import com.google.cloud.firestore.pipeline.Operation

class Pipeline {
  private val operations: MutableList<Operation> = mutableListOf()

  private constructor(db: Database) {
    operations.add(db)
  }
  private constructor(collection: Collection) {
    operations.add(collection)
  }
  private constructor(group: CollectionGroup) {
    operations.add(group)
  }
  companion object {
    @JvmStatic
    fun from(collectionName: String): Pipeline {
      return Pipeline(Collection(collectionName))
    }

    @JvmStatic
    fun fromCollectionGroup(group: String): Pipeline {
      return Pipeline(CollectionGroup(group))
    }

    @JvmStatic
    fun entireDatabase(): Pipeline {
      return Pipeline(Database())
    }
  }

  // Fluent API


}
