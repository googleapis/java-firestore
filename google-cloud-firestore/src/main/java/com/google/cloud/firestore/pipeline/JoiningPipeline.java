package com.google.cloud.firestore.pipeline;

import com.google.cloud.firestore.Pipeline;
import com.google.cloud.firestore.pipeline.expressions.Field;
import com.google.cloud.firestore.pipeline.expressions.Fields;
import com.google.cloud.firestore.pipeline.expressions.FilterCondition;
import com.google.cloud.firestore.pipeline.stages.Join;

public class JoiningPipeline {
  private final Pipeline left;
  private final Pipeline right;
  private final Join.Type join;
  private String leftPrefix = null;
  private String rightPrefix = null;

  public JoiningPipeline(Pipeline left, Pipeline right, Join.Type join) {
    this.left = left;
    this.right = right;
    this.join = join;
  }

  public Pipeline on(FilterCondition condition) {
    // TODO: Implement actual join condition logic
    return left;
  }

  public Pipeline using(Field... field) {
    // TODO: Implement actual join condition logic
    return left;
  }

  public Pipeline using(Fields field) {
    // TODO: Implement actual join condition logic
    return left;
  }
}
