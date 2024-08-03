package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.cloud.firestore.FieldPath;
import com.google.cloud.firestore.Pipeline;
import com.google.common.base.Objects;
import com.google.firestore.v1.Value;
import javax.annotation.Nullable;

/**
 * Represents a reference to a field in a Firestore document.
 *
 * <p>Field references are used to access document field values in expressions and to specify fields
 * for sorting, filtering, and projecting data in Firestore pipelines.
 *
 * <p>You can create a `Field` instance using the static {@link #of(String)} method:
 *
 * <pre>{@code
 * // Create a Field instance for the 'name' field
 * Field nameField = Field.of("name");
 *
 * // Create a Field instance for a nested field 'address.city'
 * Field cityField = Field.of("address.city");
 * }</pre>
 */
@BetaApi
public final class Field implements Expr, Selectable {
  public static final String DOCUMENT_ID = "__name__";
  private final FieldPath path;
  @Nullable private Pipeline pipeline; // Nullable

  private Field(FieldPath path) {
    this.path = path;
  }

  /**
   * Creates a {@code Field} instance representing the field at the given path.
   *
   * <p>The path can be a simple field name (e.g., "name") or a dot-separated path to a nested field
   * (e.g., "address.city").
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Create a Field instance for the 'title' field
   * Field titleField = Field.of("title");
   *
   * // Create a Field instance for a nested field 'author.firstName'
   * Field authorFirstNameField = Field.of("author.firstName");
   * }</pre>
   *
   * @param path The path to the field.
   * @return A new {@code Field} instance representing the specified field.
   */
  @BetaApi
  public static Field of(String path) {
    if (path.equals(DOCUMENT_ID)) {
      return new Field(FieldPath.of("__path__"));
    }
    return new Field(FieldPath.fromDotSeparatedString(path));
  }

  @InternalApi
  public Value toProto() {
    return Value.newBuilder().setFieldReferenceValue(path.toString()).build();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Field field = (Field) o;
    return Objects.equal(path, field.path) && Objects.equal(pipeline, field.pipeline);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(path, pipeline);
  }

  @InternalApi
  public FieldPath getPath() {
    return path;
  }
}
