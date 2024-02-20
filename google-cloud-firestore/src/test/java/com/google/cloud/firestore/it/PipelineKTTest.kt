package com.google.cloud.firestore.it

import com.google.cloud.firestore.Pipeline
import com.google.cloud.firestore.pipeline.Expr
import com.google.cloud.firestore.pipeline.Expr.Companion.equal
import com.google.cloud.firestore.pipeline.Expr.Companion.or
import com.google.cloud.firestore.pipeline.Expr.Field
import com.google.cloud.firestore.pipeline.Expr.Constant

class PipelineKTTest {
  fun pipeline() {
    Pipeline.from("test")
      .filter(Field.of("foo").equal(Expr.Constant.of(42)))
      .filter(or(Field.of("abc").lessThan(Constant.of(100))))
  }
}
