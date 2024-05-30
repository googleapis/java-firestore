package com.google.cloud.firestore.pipeline;

import com.google.cloud.firestore.Pipeline;
import com.google.cloud.firestore.PipelineResult;
import com.google.cloud.firestore.pipeline.expressions.Ordering;
import com.google.firestore.v1.Cursor;
import java.util.List;

public class PaginatingPipeline {
  private final Pipeline p;
  private final int pageSize;
  private final List<Ordering> orders;
  private final Integer offset;
  private final Cursor startCursor;
  private final Cursor endCursor;

  public PaginatingPipeline(Pipeline p, int pageSize, List<Ordering> orders) {
    this(p, pageSize, orders, null, null, null);
  }

  private PaginatingPipeline(
      Pipeline p,
      int pageSize,
      List<Ordering> orders,
      Integer offset,
      Cursor startCursor,
      Cursor endCursor) {
    this.p = p;
    this.pageSize = pageSize;
    this.orders = orders;
    this.offset = offset;
    this.startCursor = startCursor;
    this.endCursor = endCursor;
  }

  public Pipeline firstPage() {
    return this.p;
  }

  public Pipeline lastPage() {
    return this.p;
  }

  public PaginatingPipeline startAt(PipelineResult result) {
    return this;
  }

  public PaginatingPipeline startAfter(PipelineResult result) {
    return this;
  }

  public PaginatingPipeline endAt(PipelineResult result) {
    return this;
  }

  public PaginatingPipeline endBefore(PipelineResult result) {
    return this;
  }

  public PaginatingPipeline offset(int offset) {
    return this;
  }

  public PaginatingPipeline limit(int limit) {
    return this;
  }

  public PaginatingPipeline withStartCursor(Cursor cursor) {
    return this;
  }

  public PaginatingPipeline withEndCursor(Cursor cursor) {
    return this;
  }
}
