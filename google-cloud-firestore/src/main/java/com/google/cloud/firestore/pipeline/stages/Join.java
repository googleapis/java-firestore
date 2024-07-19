package com.google.cloud.firestore.pipeline.stages;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.cloud.firestore.Pipeline;
import com.google.cloud.firestore.pipeline.expressions.Field;

@BetaApi
public class Join implements Stage {
  private final Type type;
  private final JoinCondition condition;
  private final Field alias;
  private final Pipeline otherPipeline;
  private final Field otherAlias;

  @InternalApi
  Join(Type type, JoinCondition condition, Field alias, Pipeline otherPipeline, Field otherAlias) {
    this.type = type;
    this.condition = condition;
    this.alias = alias;
    this.otherPipeline = otherPipeline;
    this.otherAlias = otherAlias;
  }

  @BetaApi
  public static Join with(Pipeline other, Type type) {
    return new Join(type, null, null, null, null);
  }

  @BetaApi
  public Join onCondition(JoinCondition condition) {
    return new Join(this.type, condition, this.alias, this.otherPipeline, this.otherAlias);
  }

  @BetaApi
  public Join onFields(String... fields) {
    return new Join(this.type, condition, this.alias, this.otherPipeline, this.otherAlias);
  }

  @BetaApi
  public Join withThisAlias(String alias) {
    return new Join(this.type, condition, this.alias, this.otherPipeline, this.otherAlias);
  }

  @BetaApi
  public Join withOtherAlias(String alias) {
    return new Join(this.type, condition, this.alias, this.otherPipeline, this.otherAlias);
  }

  @Override
  public String getName() {
    return "join";
  }

  @InternalApi
  public enum Type {
    CROSS,
    INNER,
    FULL,
    LEFT,
    RIGHT,
    LEFT_SEMI,
    RIGHT_SEMI,
    LEFT_ANTI_SEMI,
    RIGHT_ANTI_SEMI
  }
}
