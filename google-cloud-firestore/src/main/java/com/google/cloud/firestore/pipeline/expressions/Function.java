package com.google.cloud.firestore.pipeline.expressions;

import static com.google.cloud.firestore.pipeline.expressions.FunctionUtils.toExprList;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.cloud.firestore.DocumentReference;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.firestore.v1.Value;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class defines the base class for Firestore {@link com.google.cloud.firestore.Pipeline}
 * functions, which can be evaluated within pipeline execution.
 *
 * <p>This class also provides a set of static functions that can be used to build expressions for
 * Firestore pipelines.
 *
 * <p>This class offers both function variants that directly operates on string field name and
 * {@link Expr} instances. Using string variant often leads to more concise code while using {@link
 * Expr} variants offers more flexibility as {@link Expr} could be result of other functions or
 * complex expressions.
 *
 * <p>You can chain together these static functions to create more complex expressions.
 */
@BetaApi
public class Function implements Expr {
  private final String name;
  private final List<Expr> params;

  protected Function(String name, List<? extends Expr> params) {
    this.name = name;
    this.params = Collections.unmodifiableList(params);
  }

  @InternalApi
  Value toProto() {
    return Value.newBuilder()
        .setFunctionValue(
            com.google.firestore.v1.Function.newBuilder()
                .setName(this.name)
                .addAllArgs(
                    this.params.stream()
                        .map(FunctionUtils::exprToValue)
                        .collect(Collectors.toList())))
        .build();
  }

  /**
   * Creates a `CollectionId` expression representing a Firestore collection path from a string
   * constant.
   *
   * @param path The path to the Firestore collection (e.g., "users").
   * @return A `CollectionId` expression.
   */
  @BetaApi
  public static CollectionId collectionId(String path) {
    return new CollectionId(Constant.of(path));
  }

  /**
   * Creates a `CollectionId` expression representing a Firestore collection path from a {@link
   * DocumentReference}.
   *
   * @param ref The {@link DocumentReference} to extract the collection path from.
   * @return A `CollectionId` expression.
   */
  @BetaApi
  public static CollectionId collectionId(DocumentReference ref) {
    return new CollectionId(Constant.of(ref.getPath()));
  }

  /**
   * Creates a `Parent` expression representing a Firestore document path from a string constant.
   *
   * @param path The path to the Firestore document (e.g., "users/user123").
   * @return A `Parent` expression.
   */
  @BetaApi
  public static Parent parent(String path) {
    return new Parent(Constant.of(path));
  }

  /**
   * Creates a `Parent` expression representing a Firestore document path from a {@link
   * DocumentReference}.
   *
   * @param ref The {@link DocumentReference} to extract the document path from.
   * @return A `Parent` expression.
   */
  @BetaApi
  public static Parent parent(DocumentReference ref) {
    return new Parent(Constant.of(ref.getPath()));
  }

  /**
   * Creates an expression that adds two expressions together.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Add the value of the 'quantity' field and the 'reserve' field.
   * Function.add(Field.of("quantity"), Field.of("reserve"));
   * }</pre>
   *
   * @param left The first expression to add.
   * @param right The second expression to add.
   * @return A new {@code Expr} representing the addition operation.
   */
  @BetaApi
  public static Add add(Expr left, Expr right) {
    return new Add(left, right);
  }

  /**
   * Creates an expression that adds an expression to a constant value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Add 5 to the value of the 'age' field
   * Function.add(Field.of("age"), 5);
   * }</pre>
   *
   * @param left The expression to add to.
   * @param right The constant value to add.
   * @return A new {@code Expr} representing the addition operation.
   */
  @BetaApi
  public static Add add(Expr left, Object right) {
    return new Add(left, Constant.of(right));
  }

  /**
   * Creates an expression that adds a field's value to an expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Add the value of the 'quantity' field and the 'reserve' field.
   * Function.add("quantity", Field.of("reserve"));
   * }</pre>
   *
   * @param left The field name to add to.
   * @param right The expression to add.
   * @return A new {@code Expr} representing the addition operation.
   */
  @BetaApi
  public static Add add(String left, Expr right) {
    return new Add(Field.of(left), right);
  }

  /**
   * Creates an expression that adds a field's value to a constant value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Add 5 to the value of the 'age' field
   * Function.add("age", 5);
   * }</pre>
   *
   * @param left The field name to add to.
   * @param right The constant value to add.
   * @return A new {@code Expr} representing the addition operation.
   */
  @BetaApi
  public static Add add(String left, Object right) {
    return new Add(Field.of(left), Constant.of(right));
  }

  /**
   * Creates an expression that subtracts two expressions.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Subtract the 'discount' field from the 'price' field
   * Function.subtract(Field.of("price"), Field.of("discount"));
   * }</pre>
   *
   * @param left The expression to subtract from.
   * @param right The expression to subtract.
   * @return A new {@code Expr} representing the subtraction operation.
   */
  @BetaApi
  public static Subtract subtract(Expr left, Expr right) {
    return new Subtract(left, right);
  }

  /**
   * Creates an expression that subtracts a constant value from an expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Subtract the constant value 2 from the 'value' field
   * Function.subtract(Field.of("value"), 2);
   * }</pre>
   *
   * @param left The expression to subtract from.
   * @param right The constant value to subtract.
   * @return A new {@code Expr} representing the subtraction operation.
   */
  @BetaApi
  public static Subtract subtract(Expr left, Object right) {
    return new Subtract(left, Constant.of(right));
  }

  /**
   * Creates an expression that subtracts an expression from a field's value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Subtract the 'discount' field from the 'price' field
   * Function.subtract("price", Field.of("discount"));
   * }</pre>
   *
   * @param left The field name to subtract from.
   * @param right The expression to subtract.
   * @return A new {@code Expr} representing the subtraction operation.
   */
  @BetaApi
  public static Subtract subtract(String left, Expr right) {
    return new Subtract(Field.of(left), right);
  }

  /**
   * Creates an expression that subtracts a constant value from a field's value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Subtract 20 from the value of the 'total' field
   * Function.subtract("total", 20);
   * }</pre>
   *
   * @param left The field name to subtract from.
   * @param right The constant value to subtract.
   * @return A new {@code Expr} representing the subtraction operation.
   */
  @BetaApi
  public static Subtract subtract(String left, Object right) {
    return new Subtract(Field.of(left), Constant.of(right));
  }

  /**
   * Creates an expression that multiplies two expressions together.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Multiply the 'quantity' field by the 'price' field
   * Function.multiply(Field.of("quantity"), Field.of("price"));
   * }</pre>
   *
   * @param left The first expression to multiply.
   * @param right The second expression to multiply.
   * @return A new {@code Expr} representing the multiplication operation.
   */
  @BetaApi
  public static Multiply multiply(Expr left, Expr right) {
    return new Multiply(left, right);
  }

  /**
   * Creates an expression that multiplies an expression by a constant value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Multiply the value of the 'price' field by 2
   * Function.multiply(Field.of("price"), 2);
   * }</pre>
   *
   * @param left The expression to multiply.
   * @param right The constant value to multiply by.
   * @return A new {@code Expr} representing the multiplication operation.
   */
  @BetaApi
  public static Multiply multiply(Expr left, Object right) {
    return new Multiply(left, Constant.of(right));
  }

  /**
   * Creates an expression that multiplies a field's value by an expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Multiply the 'quantity' field by the 'price' field
   * Function.multiply("quantity", Field.of("price"));
   * }</pre>
   *
   * @param left The field name to multiply.
   * @param right The expression to multiply by.
   * @return A new {@code Expr} representing the multiplication operation.
   */
  @BetaApi
  public static Multiply multiply(String left, Expr right) {
    return new Multiply(Field.of(left), right);
  }

  /**
   * Creates an expression that multiplies a field's value by a constant value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Multiply the 'value' field by 2
   * Function.multiply("value", 2);
   * }</pre>
   *
   * @param left The field name to multiply.
   * @param right The constant value to multiply by.
   * @return A new {@code Expr} representing the multiplication operation.
   */
  @BetaApi
  public static Multiply multiply(String left, Object right) {
    return new Multiply(Field.of(left), Constant.of(right));
  }

  /**
   * Creates an expression that divides two expressions.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Divide the 'total' field by the 'count' field
   * Function.divide(Field.of("total"), Field.of("count"));
   * }</pre>
   *
   * @param left The expression to be divided.
   * @param right The expression to divide by.
   * @return A new {@code Expr} representing the division operation.
   */
  @BetaApi
  public static Divide divide(Expr left, Expr right) {
    return new Divide(left, right);
  }

  /**
   * Creates an expression that divides an expression by a constant value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Divide the 'value' field by 10
   * Function.divide(Field.of("value"), 10);
   * }</pre>
   *
   * @param left The expression to be divided.
   * @param right The constant value to divide by.
   * @return A new {@code Expr} representing the division operation.
   */
  @BetaApi
  public static Divide divide(Expr left, Object right) {
    return new Divide(left, Constant.of(right));
  }

  /**
   * Creates an expression that divides a field's value by an expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Divide the 'total' field by the 'count' field
   * Function.divide("total", Field.of("count"));
   * }</pre>
   *
   * @param left The field name to be divided.
   * @param right The expression to divide by.
   * @return A new {@code Expr} representing the division operation.
   */
  @BetaApi
  public static Divide divide(String left, Expr right) {
    return new Divide(Field.of(left), right);
  }

  /**
   * Creates an expression that divides a field's value by a constant value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Divide the 'value' field by 10
   * Function.divide("value", 10);
   * }</pre>
   *
   * @param left The field name to be divided.
   * @param right The constant value to divide by.
   * @return A new {@code Expr} representing the division operation.
   */
  @BetaApi
  public static Divide divide(String left, Object right) {
    return new Divide(Field.of(left), Constant.of(right));
  }

  /**
   * Creates an expression that checks if two expressions are equal.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'age' field is equal to an expression
   * Function.eq(Field.of("age"), Field.of("minAge").add(10));
   * }</pre>
   *
   * @param left The first expression to compare.
   * @param right The second expression to compare.
   * @return A new {@code Expr} representing the equality comparison.
   */
  @BetaApi
  public static Eq eq(Expr left, Expr right) {
    return new Eq(left, right);
  }

  /**
   * Creates an expression that checks if an expression is equal to a constant value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'age' field is equal to 21
   * Function.eq(Field.of("age"), 21);
   * }</pre>
   *
   * @param left The expression to compare.
   * @param right The constant value to compare to.
   * @return A new {@code Expr} representing the equality comparison.
   */
  @BetaApi
  public static Eq eq(Expr left, Object right) {
    return new Eq(left, Constant.of(right));
  }

  /**
   * Creates an expression that checks if a field's value is equal to an expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'age' field is equal to the 'limit' field
   * Function.eq("age", Field.of("limit"));
   * }</pre>
   *
   * @param left The field name to compare.
   * @param right The expression to compare to.
   * @return A new {@code Expr} representing the equality comparison.
   */
  @BetaApi
  public static Eq eq(String left, Expr right) {
    return new Eq(Field.of(left), right);
  }

  /**
   * Creates an expression that checks if a field's value is equal to a constant value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'city' field is equal to string constant "London"
   * Function.eq("city", "London");
   * }</pre>
   *
   * @param left The field name to compare.
   * @param right The constant value to compare to.
   * @return A new {@code Expr} representing the equality comparison.
   */
  @BetaApi
  public static Eq eq(String left, Object right) {
    return new Eq(Field.of(left), Constant.of(right));
  }

  /**
   * Creates an expression that checks if two expressions are not equal.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'status' field is not equal to field 'finalState'
   * Function.neq(Field.of("status"), Field.of("finalState"));
   * }</pre>
   *
   * @param left The first expression to compare.
   * @param right The second expression to compare.
   * @return A new {@code Expr} representing the inequality comparison.
   */
  @BetaApi
  public static Neq neq(Expr left, Expr right) {
    return new Neq(left, right);
  }

  /**
   * Creates an expression that checks if an expression is not equal to a constant value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'status' field is not equal to "completed"
   * Function.neq(Field.of("status"), "completed");
   * }</pre>
   *
   * @param left The expression to compare.
   * @param right The constant value to compare to.
   * @return A new {@code Expr} representing the inequality comparison.
   */
  @BetaApi
  public static Neq neq(Expr left, Object right) {
    return new Neq(left, Constant.of(right));
  }

  /**
   * Creates an expression that checks if a field's value is not equal to an expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'status' field is not equal to the value of 'expectedStatus'
   * Function.neq("status", Field.of("expectedStatus"));
   * }</pre>
   *
   * @param left The field name to compare.
   * @param right The expression to compare to.
   * @return A new {@code Expr} representing the inequality comparison.
   */
  @BetaApi
  public static Neq neq(String left, Expr right) {
    return new Neq(Field.of(left), right);
  }

  /**
   * Creates an expression that checks if a field's value is not equal to a constant value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'country' field is not equal to "USA"
   * Function.neq("country", "USA");
   * }</pre>
   *
   * @param left The field name to compare.
   * @param right The constant value to compare to.
   * @return A new {@code Expr} representing the inequality comparison.
   */
  @BetaApi
  public static Neq neq(String left, Object right) {
    return new Neq(Field.of(left), Constant.of(right));
  }

  /**
   * Creates an expression that checks if the first expression is greater than the second
   * expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'age' field is greater than 18
   * Function.gt(Field.of("age"), Constant(9).add(9));
   * }</pre>
   *
   * @param left The first expression to compare.
   * @param right The second expression to compare.
   * @return A new {@code Expr} representing the greater than comparison.
   */
  @BetaApi
  public static Gt gt(Expr left, Expr right) {
    return new Gt(left, right);
  }

  /**
   * Creates an expression that checks if an expression is greater than a constant value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'age' field is greater than 18
   * Function.gt(Field.of("age"), 18);
   * }</pre>
   *
   * @param left The expression to compare.
   * @param right The constant value to compare to.
   * @return A new {@code Expr} representing the greater than comparison.
   */
  @BetaApi
  public static Gt gt(Expr left, Object right) {
    return new Gt(left, Constant.of(right));
  }

  /**
   * Creates an expression that checks if a field's value is greater than an expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the value of field 'age' is greater than the value of field 'limit'
   * Function.gt("age", Field.of("limit"));
   * }</pre>
   *
   * @param left The field name to compare.
   * @param right The expression to compare to.
   * @return A new {@code Expr} representing the greater than comparison.
   */
  @BetaApi
  public static Gt gt(String left, Expr right) {
    return new Gt(Field.of(left), right);
  }

  /**
   * Creates an expression that checks if a field's value is greater than a constant value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'price' field is greater than 100
   * Function.gt("price", 100);
   * }</pre>
   *
   * @param left The field name to compare.
   * @param right The constant value to compare to.
   * @return A new {@code Expr} representing the greater than comparison.
   */
  @BetaApi
  public static Gt gt(String left, Object right) {
    return new Gt(Field.of(left), Constant.of(right));
  }

  /**
   * Creates an expression that checks if the first expression is greater than or equal to the
   * second expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'quantity' field is greater than or equal to the field "threshold"
   * Function.gte(Field.of("quantity"), Field.of("threshold"));
   * }</pre>
   *
   * @param left The first expression to compare.
   * @param right The second expression to compare.
   * @return A new {@code Expr} representing the greater than or equal to comparison.
   */
  @BetaApi
  public static Gte gte(Expr left, Expr right) {
    return new Gte(left, right);
  }

  /**
   * Creates an expression that checks if an expression is greater than or equal to a constant
   * value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'quantity' field is greater than or equal to 10
   * Function.gte(Field.of("quantity"), 10);
   * }</pre>
   *
   * @param left The expression to compare.
   * @param right The constant value to compare to.
   * @return A new {@code Expr} representing the greater than or equal to comparison.
   */
  @BetaApi
  public static Gte gte(Expr left, Object right) {
    return new Gte(left, Constant.of(right));
  }

  /**
   * Creates an expression that checks if a field's value is greater than or equal to an expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the value of field 'age' is greater than or equal to the value of field 'limit'
   * Function.gte("age", Field.of("limit"));
   * }</pre>
   *
   * @param left The field name to compare.
   * @param right The expression to compare to.
   * @return A new {@code Expr} representing the greater than or equal to comparison.
   */
  @BetaApi
  public static Gte gte(String left, Expr right) {
    return new Gte(Field.of(left), right);
  }

  /**
   * Creates an expression that checks if a field's value is greater than or equal to a constant
   * value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'score' field is greater than or equal to 80
   * Function.gte("score", 80);
   * }</pre>
   *
   * @param left The field name to compare.
   * @param right The constant value to compare to.
   * @return A new {@code Expr} representing the greater than or equal to comparison.
   */
  @BetaApi
  public static Gte gte(String left, Object right) {
    return new Gte(Field.of(left), Constant.of(right));
  }

  /**
   * Creates an expression that checks if the first expression is less than the second expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'age' field is less than 30
   * Function.lt(Field.of("age"), Field.of("limit"));
   * }</pre>
   *
   * @param left The first expression to compare.
   * @param right The second expression to compare.
   * @return A new {@code Expr} representing the less than comparison.
   */
  @BetaApi
  public static Lt lt(Expr left, Expr right) {
    return new Lt(left, right);
  }

  /**
   * Creates an expression that checks if an expression is less than a constant value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'age' field is less than 30
   * Function.lt(Field.of("age"), 30);
   * }</pre>
   *
   * @param left The expression to compare.
   * @param right The constant value to compare to.
   * @return A new {@code Expr} representing the less than comparison.
   */
  @BetaApi
  public static Lt lt(Expr left, Object right) {
    return new Lt(left, Constant.of(right));
  }

  /**
   * Creates an expression that checks if a field's value is less than an expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'age' field is less than the 'limit' field
   * Function.lt("age", Field.of("limit"));
   * }</pre>
   *
   * @param left The field name to compare.
   * @param right The expression to compare to.
   * @return A new {@code Expr} representing the less than comparison.
   */
  @BetaApi
  public static Lt lt(String left, Expr right) {
    return new Lt(Field.of(left), right);
  }

  /**
   * Creates an expression that checks if a field's value is less than a constant value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'price' field is less than 50
   * Function.lt("price", 50);
   * }</pre>
   *
   * @param left The field name to compare.
   * @param right The constant value to compare to.
   * @return A new {@code Expr} representing the less than comparison.
   */
  @BetaApi
  public static Lt lt(String left, Object right) {
    return new Lt(Field.of(left), Constant.of(right));
  }

  /**
   * Creates an expression that checks if the first expression is less than or equal to the second
   * expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'quantity' field is less than or equal to 20
   * Function.lte(Field.of("quantity"), Field.of("limit"));
   * }</pre>
   *
   * @param left The first expression to compare.
   * @param right The second expression to compare.
   * @return A new {@code Expr} representing the less than or equal to comparison.
   */
  @BetaApi
  public static Lte lte(Expr left, Expr right) {
    return new Lte(left, right);
  }

  /**
   * Creates an expression that checks if an expression is less than or equal to a constant value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'quantity' field is less than or equal to 20
   * Function.lte(Field.of("quantity"), 20);
   * }</pre>
   *
   * @param left The expression to compare.
   * @param right The constant value to compare to.
   * @return A new {@code Expr} representing the less than or equal to comparison.
   */
  @BetaApi
  public static Lte lte(Expr left, Object right) {
    return new Lte(left, Constant.of(right));
  }

  /**
   * Creates an expression that checks if a field's value is less than or equal to an expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'quantity' field is less than or equal to the 'limit' field
   * Function.lte("quantity", Field.of("limit"));
   * }</pre>
   *
   * @param left The field name to compare.
   * @param right The expression to compare to.
   * @return A new {@code Expr} representing the less than or equal to comparison.
   */
  @BetaApi
  public static Lte lte(String left, Expr right) {
    return new Lte(Field.of(left), right);
  }

  /**
   * Creates an expression that checks if a field's value is less than or equal to a constant value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'score' field is less than or equal to 70
   * Function.lte("score", 70);
   * }</pre>
   *
   * @param left The field name to compare.
   * @param right The constant value to compare to.
   * @return A new {@code Expr} representing the less than or equal to comparison.
   */
  @BetaApi
  public static Lte lte(String left, Object right) {
    return new Lte(Field.of(left), Constant.of(right));
  }

  /**
   * Creates an expression that checks if a field exists.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the document has a field named "phoneNumber"
   * Function.exists("phoneNumber");
   * }</pre>
   *
   * @param field The field name to check.
   * @return A new {@code Expr} representing the 'exists' check.
   */
  @BetaApi
  public static Exists exists(String field) {
    return new Exists(Field.of(field));
  }

  /**
   * Creates an expression that checks if a field exists.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the document has a field named "phoneNumber"
   * Function.exists(Field.of("phoneNumber"));
   * }</pre>
   *
   * @param field An expression evaluates to the name of the field to check.
   * @return A new {@code Expr} representing the 'exists' check.
   */
  @BetaApi
  public static Exists exists(Expr field) {
    return new Exists(field);
  }

  /**
   * Creates an expression that checks if an expression is equal to any of the provided values or
   * expressions.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'category' field is either "Electronics" or value of field 'primaryType'
   * Function.inAny(Field.of("category"), List.of("Electronics", Field.of("primaryType")));
   * }</pre>
   *
   * @param left The expression to compare.
   * @param values The values to check against.
   * @return A new {@code Expr} representing the 'IN' comparison.
   */
  @BetaApi
  public static In inAny(Expr left, List<Object> values) {
    List<Expr> othersAsExpr =
        values.stream()
            .map(obj -> (obj instanceof Expr) ? (Expr) obj : Constant.of(obj))
            .collect(Collectors.toList());
    return new In(left, othersAsExpr);
  }

  /**
   * Creates an expression that checks if a field's value is equal to any of the provided values or
   * expressions.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'category' field is either "Electronics" or value of field 'primaryType'
   * Function.inAny("category", List.of("Electronics", Field.of("primaryType")));
   * }</pre>
   *
   * @param left The field to compare.
   * @param values The values to check against.
   * @return A new {@code Expr} representing the 'IN' comparison.
   */
  @BetaApi
  public static In inAny(String left, List<Object> values) {
    return inAny(Field.of(left), values);
  }

  /**
   * Creates an expression that checks if an expression is not equal to any of the provided values
   * or expressions.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'status' field is neither "pending" nor the value of 'rejectedStatus'
   * Function.notInAny(Field.of("status"), List.of("pending", Field.of("rejectedStatus")));
   * }</pre>
   *
   * @param left The expression to compare.
   * @param values The values to check against.
   * @return A new {@code Expr} representing the 'NOT IN' comparison.
   */
  @BetaApi
  public static Not notInAny(Expr left, List<Object> values) {
    return new Not(inAny(left, values));
  }

  /**
   * Creates an expression that checks if a field's value is not equal to any of the provided values
   * or expressions.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'status' field is neither "pending" nor the value of 'rejectedStatus'
   * Function.notInAny("status", List.of("pending", Field.of("rejectedStatus")));
   * }</pre>
   *
   * @param left The field name to compare.
   * @param values The values to check against.
   * @return A new {@code Expr} representing the 'NOT IN' comparison.
   */
  @BetaApi
  public static Not notInAny(String left, List<Object> values) {
    return new Not(inAny(Field.of(left), values));
  }

  /**
   * Creates an expression that performs a logical 'AND' operation on two filter conditions.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'age' field is greater than 18 AND the 'city' field is "London"
   * FilterCondition condition = Function.and(Function.gt("age", 18), Function.eq("city", "London"));
   * }</pre>
   *
   * @param left The first filter condition.
   * @param right The second filter condition.
   * @return A new {@code Expr} representing the logical 'AND' operation.
   */
  @BetaApi
  public static And and(FilterCondition left, FilterCondition right) {
    return new And(Lists.newArrayList(left, right));
  }

  /**
   * Creates an expression that performs a logical 'AND' operation on multiple filter conditions.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'age' field is greater than 18 AND the 'city' field is "London" AND
   * // the 'status' field is "active"
   * FilterCondition condition = Function.and(Function.gt("age", 18),
   *     Function.eq("city", "London"), Function.eq("status", "active"));
   * }</pre>
   *
   * @param left The first filter condition.
   * @param other Additional filter conditions to 'AND' together.
   * @return A new {@code Expr} representing the logical 'AND' operation.
   */
  @BetaApi
  public static And and(FilterCondition left, FilterCondition... other) {
    List<FilterCondition> conditions = Lists.newArrayList(left);
    conditions.addAll(Arrays.asList(other));
    return new And(conditions);
  }

  /**
   * Creates an expression that performs a logical 'OR' operation on two filter conditions.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'age' field is greater than 18 OR the 'city' field is "London"
   * FilterCondition condition = Function.or(Function.gt("age", 18), Function.eq("city", "London"));
   * }</pre>
   *
   * @param left The first filter condition.
   * @param right The second filter condition.
   * @return A new {@code Expr} representing the logical 'OR' operation.
   */
  @BetaApi
  public static Or or(FilterCondition left, FilterCondition right) {
    return new Or(Lists.newArrayList(left, right));
  }

  /**
   * Creates an expression that performs a logical 'OR' operation on multiple filter conditions.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'age' field is greater than 18 OR the 'city' field is "London" OR
   * // the 'status' field is "active"
   * FilterCondition condition = Function.or(Function.gt("age", 18),
   *     Function.eq("city", "London"), Function.eq("status", "active"));
   * }</pre>
   *
   * @param left The first filter condition.
   * @param other Additional filter conditions to 'OR' together.
   * @return A new {@code Expr} representing the logical 'OR' operation.
   */
  @BetaApi
  public static Or or(FilterCondition left, FilterCondition... other) {
    List<FilterCondition> conditions = Lists.newArrayList(left);
    conditions.addAll(Arrays.asList(other));
    return new Or(conditions);
  }

  /**
   * Creates an expression that performs a logical 'XOR' (exclusive OR) operation on two filter
   * conditions.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if either the 'age' field is greater than 18 OR the 'city' field is "London",
   * // but NOT both.
   * FilterCondition condition =
   *     Function.xor(Function.gt("age", 18), Function.eq("city", "London"));
   * }</pre>
   *
   * @param left The first filter condition.
   * @param right The second filter condition.
   * @return A new {@code Expr} representing the logical 'XOR' operation.
   */
  @BetaApi
  public static Xor xor(FilterCondition left, FilterCondition right) {
    return new Xor(Lists.newArrayList(left, right));
  }

  /**
   * Creates an expression that performs a logical 'XOR' (exclusive OR) operation on multiple filter
   * conditions.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if only one of the conditions is true: 'age' greater than 18, 'city' is "London",
   * // or 'status' is "active".
   * FilterCondition condition = Function.xor(
   *     Function.gt("age", 18),
   *     Function.eq("city", "London"),
   *     Function.eq("status", "active"));
   * }</pre>
   *
   * @param left The first filter condition.
   * @param other Additional filter conditions to 'XOR' together.
   * @return A new {@code Expr} representing the logical 'XOR' operation.
   */
  @BetaApi
  public static Xor xor(FilterCondition left, FilterCondition... other) {
    List<FilterCondition> conditions = Lists.newArrayList(left);
    conditions.addAll(Arrays.asList(other));
    return new Xor(conditions);
  }

  /**
   * Creates a conditional expression that evaluates to a 'then' expression if a condition is true.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // If 'age' is greater than 18, evaluates to "Adult"; otherwise, evaluates null.
   * Function.ifThen(Function.gt("age", 18), Constant.of("Adult"));
   * }</pre>
   *
   * @param condition The condition to evaluate.
   * @param thenExpr The expression to evaluate if the condition is true.
   * @return A new {@code Expr} representing the conditional expression.
   */
  @BetaApi
  public static If ifThen(FilterCondition condition, Expr thenExpr) {
    return new If(condition, thenExpr, null);
  }

  /**
   * Creates a conditional expression that evaluates to a 'then' expression if a condition is true
   * and an 'else' expression if the condition is false.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // If 'age' is greater than 18, return "Adult"; otherwise, return "Minor".
   * Function.ifThenElse(
   *     Function.gt("age", 18), Constant.of("Adult"), Constant.of("Minor"));
   * }</pre>
   *
   * @param condition The condition to evaluate.
   * @param thenExpr The expression to evaluate if the condition is true.
   * @param elseExpr The expression to evaluate if the condition is false.
   * @return A new {@code Expr} representing the conditional expression.
   */
  @BetaApi
  public static If ifThenElse(FilterCondition condition, Expr thenExpr, Expr elseExpr) {
    return new If(condition, thenExpr, elseExpr);
  }

  /**
   * Creates an expression that concatenates an array expression with other arrays.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Combine the 'items' array with two new item arrays
   * Function.arrayConcat(Field.of("items"), Field.of("newItems"), Field.of("otherItems"));
   * }</pre>
   *
   * @param expr The array expression to concatenate to.
   * @param elements The array expressions to concatenate.
   * @return A new {@code Expr} representing the concatenated array.
   */
  @BetaApi
  public static ArrayConcat arrayConcat(Expr expr, Expr... elements) {
    return new ArrayConcat(expr, Arrays.asList(elements));
  }

  /**
   * Creates an expression that concatenates an array expression with other arrays and/or values.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Combine the 'tags' array with a new array
   * Function.arrayConcat(Field.of("tags"), Arrays.asList("newTag1", "newTag2"));
   * }</pre>
   *
   * @param expr The array expression to concatenate to.
   * @param elements The array expressions or single values to concatenate.
   * @return A new {@code Expr} representing the concatenated array.
   */
  @BetaApi
  public static ArrayConcat arrayConcat(Expr expr, Object... elements) {
    return new ArrayConcat(expr, toExprList(elements));
  }

  /**
   * Creates an expression that concatenates a field's array value with other arrays.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Combine the 'items' array with two new item arrays
   * Function.arrayConcat("items", Field.of("newItems"), Field.of("otherItems"));
   * }</pre>
   *
   * @param field The field name containing array values.
   * @param elements The array expressions to concatenate.
   * @return A new {@code Expr} representing the concatenated array.
   */
  @BetaApi
  public static ArrayConcat arrayConcat(String field, Expr... elements) {
    return new ArrayConcat(Field.of(field), Arrays.asList(elements));
  }

  /**
   * Creates an expression that concatenates a field's array value with other arrays and/or values.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Combine the 'tags' array with a new array
   * Function.arrayConcat("tags", Arrays.asList("newTag1", "newTag2"));
   * }</pre>
   *
   * @param field The field name containing array values.
   * @param elements The array expressions or single values to concatenate.
   * @return A new {@code Expr} representing the concatenated array.
   */
  @BetaApi
  public static ArrayConcat arrayConcat(String field, Object... elements) {
    return new ArrayConcat(Field.of(field), toExprList(elements));
  }

  /**
   * Creates an expression that checks if an array expression contains a specific element.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'colors' array contains the value of field 'selectedColor'
   * Function.arrayContains(Field.of("colors"), Field.of("selectedColor"));
   * }</pre>
   *
   * @param expr The array expression to check.
   * @param element The element to search for in the array.
   * @return A new {@code Expr} representing the 'array_contains' comparison.
   */
  @BetaApi
  public static ArrayContains arrayContains(Expr expr, Expr element) {
    return new ArrayContains(expr, element);
  }

  /**
   * Creates an expression that checks if a field's array value contains a specific element.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'colors' array contains the value of field 'selectedColor'
   * Function.arrayContains("colors", Field.of("selectedColor"));
   * }</pre>
   *
   * @param field The field name to check.
   * @param element The element to search for in the array.
   * @return A new {@code Expr} representing the 'array_contains' comparison.
   */
  @BetaApi
  public static ArrayContains arrayContains(String field, Expr element) {
    return new ArrayContains(Field.of(field), element);
  }

  /**
   * Creates an expression that checks if an array expression contains a specific element.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'colors' array contains "red"
   * Function.arrayContains(Field.of("colors"), "red");
   * }</pre>
   *
   * @param expr The array expression to check.
   * @param element The element to search for in the array.
   * @return A new {@code Expr} representing the 'array_contains' comparison.
   */
  @BetaApi
  public static ArrayContains arrayContains(Expr expr, Object element) {
    return new ArrayContains(expr, Constant.of(element));
  }

  /**
   * Creates an expression that checks if a field's array value contains a specific value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'colors' array contains "red"
   * Function.arrayContains("colors", "red");
   * }</pre>
   *
   * @param field The field name to check.
   * @param element The element to search for in the array.
   * @return A new {@code Expr} representing the 'array_contains' comparison.
   */
  @BetaApi
  public static ArrayContains arrayContains(String field, Object element) {
    return new ArrayContains(Field.of(field), Constant.of(element));
  }

  /**
   * Creates an expression that checks if an array expression contains all the specified elements.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'tags' array contains both of the values from field 'tag1', 'tag2' and "tag3"
   * Function.arrayContainsAll(Field.of("tags"), List.of(Field.of("tag1"), "SciFi", "Adventure"));
   * }</pre>
   *
   * @param expr The array expression to check.
   * @param elements The elements to check for in the array.
   * @return A new {@code Expr} representing the 'array_contains_all' comparison.
   */
  @BetaApi
  public static ArrayContainsAll arrayContainsAll(Expr expr, List<Object> elements) {
    return new ArrayContainsAll(expr, toExprList(elements.toArray()));
  }

  /**
   * Creates an expression that checks if a field's array value contains all the specified values or
   * expressions.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'tags' array contains both of the values from field 'tag1' and "tag2"
   * Function.arrayContainsAll("tags", List.of(Field.of("tag1"), "SciFi", "Adventure"));
   * }</pre>
   *
   * @param field The field name to check.
   * @param elements The elements to check for in the array.
   * @return A new {@code Expr} representing the 'array_contains_all' comparison.
   */
  @BetaApi
  public static ArrayContainsAll arrayContainsAll(String field, List<Object> elements) {
    return new ArrayContainsAll(Field.of(field), toExprList(elements.toArray()));
  }

  /**
   * Creates an expression that checks if an array expression contains any of the specified
   * elements.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'categories' array contains either values from field "cate1" or "Science"
   * Function.arrayContainsAny(Field.of("categories"), List.of(Field.of("cate1"), "Science"));
   * }</pre>
   *
   * @param expr The array expression to check.
   * @param elements The elements to check for in the array.
   * @return A new {@code Expr} representing the 'array_contains_any' comparison.
   */
  @BetaApi
  public static ArrayContainsAny arrayContainsAny(Expr expr, List<Object> elements) {
    return new ArrayContainsAny(expr, toExprList(elements.toArray()));
  }

  /**
   * Creates an expression that checks if a field's array value contains any of the specified
   * elements.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'groups' array contains either the value from the 'userGroup' field
   * // or the value "guest"
   * Function.arrayContainsAny("categories", List.of(Field.of("cate1"), "Science"));
   * }</pre>
   *
   * @param field The field name to check.
   * @param elements The elements to check for in the array.
   * @return A new {@code Expr} representing the 'array_contains_any' comparison.
   */
  @BetaApi
  public static ArrayContainsAny arrayContainsAny(String field, List<Object> elements) {
    return new ArrayContainsAny(Field.of(field), toExprList(elements.toArray()));
  }

  /**
   * Creates an expression that filters elements from an array expression using the given {@link
   * FilterCondition} and returns the filtered elements as a new array.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Get items from the 'inventoryPrices' array where the array item is greater than 0
   * // Note we use {@link Function#arrayElement} to represent array elements to construct a
   * // filtering condition.
   * Function.arrayFilter(Field.of("inventoryPrices"), arrayElement().gt(0));
   * }</pre>
   *
   * @param expr The array expression to filter.
   * @param filter The {@link FilterCondition} to apply to the array elements.
   * @return A new {@code Expr} representing the filtered array.
   */
  @BetaApi
  public static ArrayFilter arrayFilter(Expr expr, FilterCondition filter) {
    return new ArrayFilter(expr, filter);
  }

  /**
   * Creates an expression that filters elements from an array field using the given {@link
   * FilterCondition} and returns the filtered elements as a new array.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Get items from the 'inventoryPrices' array where the array item is greater than 0
   * // Note we use {@link Function#arrayElement} to represent array elements to construct a
   * // filtering condition.
   * Function.arrayFilter("inventoryPrices", arrayElement().gt(0));
   * }</pre>
   *
   * @param field The field name containing array values.
   * @param filter The {@link FilterCondition} to apply to the array elements.
   * @return A new {@code Expr} representing the filtered array.
   */
  @BetaApi
  public static ArrayFilter arrayFilter(String field, FilterCondition filter) {
    return new ArrayFilter(Field.of(field), filter);
  }

  /**
   * Creates an expression that calculates the length of an array expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Get the number of items in the 'cart' array
   * Function.arrayLength(Field.of("cart"));
   * }</pre>
   *
   * @param expr The array expression to calculate the length of.
   * @return A new {@code Expr} representing the length of the array.
   */
  @BetaApi
  public static ArrayLength arrayLength(Expr expr) {
    return new ArrayLength(expr);
  }

  /**
   * Creates an expression that calculates the length of an array field.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Get the number of items in the 'cart' array
   * Function.arrayLength("cart");
   * }</pre>
   *
   * @param field The field name containing array values.
   * @return A new {@code Expr} representing the length of the array.
   */
  @BetaApi
  public static ArrayLength arrayLength(String field) {
    return new ArrayLength(Field.of(field));
  }

  /**
   * Creates an expression that applies a transformation function to each element in an array
   * expression and returns the new array as the result of the evaluation.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Convert all strings in the 'names' array to uppercase
   * // Note we use {@link Function#arrayElement} to represent array elements to construct a
   * // transforming function.
   * Function.arrayTransform(Field.of("names"), arrayElement().toUppercase());
   * }</pre>
   *
   * @param expr The array expression to transform.
   * @param transform The {@link Function} to apply to each array element.
   * @return A new {@code Expr} representing the transformed array.
   */
  @BetaApi
  public static ArrayTransform arrayTransform(Expr expr, Function transform) {
    return new ArrayTransform(expr, transform);
  }

  /**
   * Creates an expression that applies a transformation function to each element in an array field
   * and returns the new array as the result of the evaluation.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Convert all strings in the 'names' array to uppercase
   * // Note we use {@link Function#arrayElement} to represent array elements to construct a
   * // transforming function.
   * Function.arrayTransform("names", arrayElement().toUppercase());
   * }</pre>
   *
   * @param field The field name containing array values.
   * @param transform The {@link Function} to apply to each array element.
   * @return A new {@code Expr} representing the transformed array.
   */
  @BetaApi
  public static ArrayTransform arrayTransform(String field, Function transform) {
    return new ArrayTransform(Field.of(field), transform);
  }

  /**
   * Creates an expression that calculates the length of a string expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Get the length of the 'name' field
   * Function.length(Field.of("name"));
   * }</pre>
   *
   * @param expr The expression representing the string to calculate the length of.
   * @return A new {@code Expr} representing the length of the string.
   */
  @BetaApi
  public static Length length(Expr expr) {
    return new Length(expr);
  }

  /**
   * Creates an expression that calculates the length of a string field.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Get the length of the 'name' field
   * Function.length("name");
   * }</pre>
   *
   * @param field The name of the field containing the string.
   * @return A new {@code Expr} representing the length of the string.
   */
  @BetaApi
  public static Length length(String field) {
    return new Length(Field.of(field));
  }

  /**
   * Creates an expression that performs a case-sensitive wildcard string comparison.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'title' field contains the string "guide"
   * Function.like(Field.of("title"), "%guide%");
   * }</pre>
   *
   * @param expr The expression representing the string to perform the comparison on.
   * @param pattern The pattern to search for. You can use "%" as a wildcard character.
   * @return A new {@code Expr} representing the 'like' comparison.
   */
  @BetaApi
  public static Like like(Expr expr, String pattern) {
    return new Like(expr, Constant.of(pattern));
  }

  /**
   * Creates an expression that performs a case-sensitive wildcard string comparison against a
   * field.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'title' field contains the string "guide"
   * Function.like("title", "%guide%");
   * }</pre>
   *
   * @param field The name of the field containing the string.
   * @param pattern The pattern to search for. You can use "%" as a wildcard character.
   * @return A new {@code Expr} representing the 'like' comparison.
   */
  @BetaApi
  public static Like like(String field, String pattern) {
    return new Like(Field.of(field), Constant.of(pattern));
  }

  /**
   * Creates an expression that checks if a string expression contains a specified regular
   * expression as a substring.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'description' field contains "example" (case-insensitive)
   * Function.regexContains(Field.of("description"), "(?i)example");
   * }</pre>
   *
   * @param expr The expression representing the string to perform the comparison on.
   * @param pattern The regular expression to use for the search.
   * @return A new {@code Expr} representing the 'contains' comparison.
   */
  @BetaApi
  public static RegexContains regexContains(Expr expr, String pattern) {
    return new RegexContains(expr, Constant.of(pattern));
  }

  /**
   * Creates an expression that checks if a string field contains a specified regular expression as
   * a substring.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'description' field contains "example" (case-insensitive)
   * Function.regexContains("description", "(?i)example");
   * }</pre>
   *
   * @param field The name of the field containing the string.
   * @param pattern The regular expression to use for the search.
   * @return A new {@code Expr} representing the 'contains' comparison.
   */
  @BetaApi
  public static RegexContains regexContains(String field, String pattern) {
    return new RegexContains(Field.of(field), Constant.of(pattern));
  }

  /**
   * Creates an expression that checks if a string expression matches a specified regular
   * expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'email' field matches a valid email pattern
   * Function.regexMatch(Field.of("email"), "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");
   * }</pre>
   *
   * @param expr The expression representing the string to match against.
   * @param pattern The regular expression to use for the match.
   * @return A new {@code Expr} representing the regular expression match.
   */
  @BetaApi
  public static RegexMatch regexMatch(Expr expr, String pattern) {
    return new RegexMatch(expr, Constant.of(pattern));
  }

  /**
   * Creates an expression that checks if a string field matches a specified regular expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'email' field matches a valid email pattern
   * Function.regexMatch("email", "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");
   * }</pre>
   *
   * @param field The name of the field containing the string.
   * @param pattern The regular expression to use for the match.
   * @return A new {@code Expr} representing the regular expression match.
   */
  @BetaApi
  public static RegexMatch regexMatch(String field, String pattern) {
    return new RegexMatch(Field.of(field), Constant.of(pattern));
  }

  /**
   * Creates an expression that checks if a field's value starts with a given prefix.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'name' field starts with "Mr."
   * Function.startsWith("name", "Mr.");
   * }</pre>
   *
   * @param field The field name to check.
   * @param prefix The prefix to check for.
   * @return A new {@code Expr} representing the 'starts with' comparison.
   */
  @BetaApi
  public static StartsWith startsWith(String field, String prefix) {
    return new StartsWith(Field.of(field), Constant.of(prefix));
  }

  /**
   * Creates an expression that checks if a field's value starts with a given prefix.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'fullName' field starts with the value of the 'firstName' field
   * Function.startsWith("fullName", Field.of("firstName"));
   * }</pre>
   *
   * @param field The field name to check.
   * @param prefix The expression representing the prefix.
   * @return A new {@code Expr} representing the 'starts with' comparison.
   */
  @BetaApi
  public static StartsWith startsWith(String field, Expr prefix) {
    return new StartsWith(Field.of(field), prefix);
  }

  /**
   * Creates an expression that checks if a string expression starts with a given prefix.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the result of concatenating 'firstName' and 'lastName' fields starts with "Mr."
   * Function.startsWith(Function.strConcat("firstName", "lastName"), "Mr.");
   * }</pre>
   *
   * @param expr The expression to check.
   * @param prefix The prefix to check for.
   * @return A new {@code Expr} representing the 'starts with' comparison.
   */
  @BetaApi
  public static StartsWith startsWith(Expr expr, Expr prefix) {
    return new StartsWith(expr, prefix);
  }

  /**
   * Creates an expression that checks if a field's value ends with a given postfix.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'filename' field ends with ".txt"
   * Function.endsWith("filename", ".txt");
   * }</pre>
   *
   * @param field The field name to check.
   * @param postfix The postfix to check for.
   * @return A new {@code Expr} representing the 'ends with' comparison.
   */
  @BetaApi
  public static EndsWith endsWith(String field, String postfix) {
    return new EndsWith(Field.of(field), Constant.of(postfix));
  }

  /**
   * Creates an expression that checks if a field's value ends with a given postfix.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'url' field ends with the value of the 'extension' field
   * Function.endsWith("url", Field.of("extension"));
   * }</pre>
   *
   * @param field The field name to check.
   * @param postfix The expression representing the postfix.
   * @return A new {@code Expr} representing the 'ends with' comparison.
   */
  @BetaApi
  public static EndsWith endsWith(String field, Expr postfix) {
    return new EndsWith(Field.of(field), postfix);
  }

  /**
   * Creates an expression that checks if a string expression ends with a given postfix.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the result of concatenating 'firstName' and 'lastName' fields ends with "Jr."
   * Function.endsWith(Field.of("fullName"), Constant.of("Jr."));
   * }</pre>
   *
   * @param expr The expression to check.
   * @param postfix The postfix to check for.
   * @return A new {@code Expr} representing the 'ends with' comparison.
   */
  @BetaApi
  public static EndsWith endsWith(Expr expr, Expr postfix) {
    return new EndsWith(expr, postfix);
  }

  /**
   * Creates an expression that concatenates string expressions together.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Combine the 'firstName', " ", and 'lastName' fields into a single string
   * Function.strConcat(Field.of("firstName"), Constant.of(" "), Field.of("lastName"));
   * }</pre>
   *
   * @param expr The initial string expression to concatenate to.
   * @param elements The expressions (typically strings) to concatenate.
   * @return A new {@code Expr} representing the concatenated string.
   */
  @BetaApi
  public static StrConcat strConcat(Expr expr, Expr... elements) {
    return new StrConcat(expr, Arrays.asList(elements));
  }

  /**
   * Creates an expression that concatenates string functions, fields or constants together.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Combine the 'firstName', " ", and 'lastName' fields into a single string
   * Function.strConcat(Field.of("firstName"), " ", Field.of("lastName"));
   * }</pre>
   *
   * @param expr The initial string expression to concatenate to.
   * @param elements The expressions (typically strings) to concatenate.
   * @return A new {@code Expr} representing the concatenated string.
   */
  @BetaApi
  public static StrConcat strConcat(Expr expr, Object... elements) {
    return new StrConcat(expr, toExprList(elements));
  }

  /**
   * Creates an expression that concatenates string expressions for the specified field together.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Combine the 'firstName', " ", and 'lastName' fields into a single string
   * Function.strConcat("firstName", Constant.of(" "), Field.of("lastName"));
   * }</pre>
   *
   * @param field The field name containing the initial string value.
   * @param elements The expressions (typically strings) to concatenate.
   * @return A new {@code Expr} representing the concatenated string.
   */
  @BetaApi
  public static StrConcat strConcat(String field, Expr... elements) {
    return new StrConcat(Field.of(field), Arrays.asList(elements));
  }

  /**
   * Creates an expression that concatenates string functions, fields or constants for the specified
   * field together.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Combine the 'firstName', " ", and 'lastName' fields into a single string
   * Function.strConcat("firstName", " ", Field.of("lastName"));
   * }</pre>
   *
   * @param field The field name containing the initial string value.
   * @param elements The expressions (typically strings) to concatenate.
   * @return A new {@code Expr} representing the concatenated string.
   */
  @BetaApi
  public static StrConcat strConcat(String field, Object... elements) {
    return new StrConcat(Field.of(field), toExprList(elements));
  }

  /**
   * Creates an expression that converts a string expression to lowercase.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Convert the 'name' field to lowercase
   * Function.toLowerCase(Field.of("name"));
   * }</pre>
   *
   * @param expr The expression representing the string to convert to lowercase.
   * @return A new {@code Expr} representing the lowercase string.
   */
  @BetaApi
  public static ToLowercase toLowercase(Expr expr) {
    return new ToLowercase(expr);
  }

  /**
   * Creates an expression that converts a string field to lowercase.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Convert the 'name' field to lowercase
   * Function.toLowerCase("name");
   * }</pre>
   *
   * @param field The name of the field containing the string.
   * @return A new {@code Expr} representing the lowercase string.
   */
  @BetaApi
  public static ToLowercase toLowercase(String field) {
    return new ToLowercase(Field.of(field));
  }

  /**
   * Creates an expression that converts a string expression to uppercase.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Convert the 'title' field to uppercase
   * Function.toUpperCase(Field.of("title"));
   * }</pre>
   *
   * @param expr The expression representing the string to convert to uppercase.
   * @return A new {@code Expr} representing the uppercase string.
   */
  @BetaApi
  public static ToUppercase toUppercase(Expr expr) {
    return new ToUppercase(expr);
  }

  /**
   * Creates an expression that converts a string field to uppercase.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Convert the 'title' field to uppercase
   * Function.toUpperCase("title");
   * }</pre>
   *
   * @param field The name of the field containing the string.
   * @return A new {@code Expr} representing the uppercase string.
   */
  @BetaApi
  public static ToUppercase toUppercase(String field) {
    return new ToUppercase(Field.of(field));
  }

  /**
   * Creates an expression that removes leading and trailing whitespace from a string expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Trim whitespace from the 'userInput' field
   * Function.trim(Field.of("userInput"));
   * }</pre>
   *
   * @param expr The expression representing the string to trim.
   * @return A new {@code Expr} representing the trimmed string.
   */
  @BetaApi
  public static Trim trim(Expr expr) {
    return new Trim(expr);
  }

  /**
   * Creates an expression that removes leading and trailing whitespace from a string field.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Trim whitespace from the 'userInput' field
   * Function.trim("userInput");
   * }</pre>
   *
   * @param field The name of the field containing the string.
   * @return A new {@code Expr} representing the trimmed string.
   */
  @BetaApi
  public static Trim trim(String field) {
    return new Trim(Field.of(field));
  }

  /**
   * Creates an expression that checks if an expression evaluates to 'NaN' (Not a Number).
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the result of a calculation is NaN
   * Function.isNaN(Field.of("value").divide(0));
   * }</pre>
   *
   * @param expr The expression to check.
   * @return A new {@code Expr} representing the 'isNaN' check.
   */
  @BetaApi
  public static IsNaN isNaN(Expr expr) {
    return new IsNaN(expr);
  }

  /**
   * Creates an expression that checks if a field's value evaluates to 'NaN' (Not a Number).
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the result of a calculation is NaN
   * Function.isNaN("value");
   * }</pre>
   *
   * @param field The name of the field to check.
   * @return A new {@code Expr} representing the 'isNaN' check.
   */
  @BetaApi
  public static IsNaN isNaN(String field) {
    return new IsNaN(Field.of(field));
  }

  /**
   * Creates an expression that negates a filter condition.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Find documents where the 'completed' field is NOT true
   * Function.not(Function.eq("completed", true));
   * }</pre>
   *
   * @param expr The filter condition to negate.
   * @return A new {@code Expr} representing the negated filter condition.
   */
  @BetaApi
  public static Not not(FilterCondition expr) {
    return new Not(expr);
  }

  /**
   * Creates an aggregation that calculates the sum of values from an expression across multiple
   * stage inputs.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the total revenue from a set of orders
   * Function.sum(Field.of("orderAmount")).as("totalRevenue");
   * }</pre>
   *
   * @param expr The expression to sum up.
   * @return A new {@code Accumulator} representing the 'sum' aggregation.
   */
  @BetaApi
  public static Sum sum(Expr expr) {
    return new Sum(expr, false);
  }

  /**
   * Creates an aggregation that calculates the sum of a field's values across multiple stage
   * inputs.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the total revenue from a set of orders
   * Function.sum("orderAmount").as("totalRevenue");
   * }</pre>
   *
   * @param field The name of the field containing numeric values to sum up.
   * @return A new {@code Accumulator} representing the 'sum' aggregation.
   */
  @BetaApi
  public static Sum sum(String field) {
    return new Sum(Field.of(field), false);
  }

  /**
   * Creates an aggregation that calculates the average (mean) of values from an expression across
   * multiple stage inputs.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the average age of users
   * Function.avg(Field.of("age")).as("averageAge");
   * }</pre>
   *
   * @param expr The expression representing the values to average.
   * @return A new {@code Accumulator} representing the 'avg' aggregation.
   */
  @BetaApi
  public static Avg avg(Expr expr) {
    return new Avg(expr, false);
  }

  /**
   * Creates an aggregation that calculates the average (mean) of a field's values across multiple
   * stage inputs.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the average age of users
   * Function.avg("age").as("averageAge");
   * }</pre>
   *
   * @param field The name of the field containing numeric values to average.
   * @return A new {@code Accumulator} representing the 'avg' aggregation.
   */
  @BetaApi
  public static Avg avg(String field) {
    return new Avg(Field.of(field), false);
  }

  /**
   * Creates an aggregation that finds the minimum value of an expression across multiple stage
   * inputs.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Find the lowest price of all products
   * Function.min(Field.of("price")).as("lowestPrice");
   * }</pre>
   *
   * @param expr The expression to find the minimum value of.
   * @return A new {@code Accumulator} representing the 'min' aggregation.
   */
  @BetaApi
  public static Min min(Expr expr) {
    return new Min(expr, false);
  }

  /**
   * Creates an aggregation that finds the minimum value of a field across multiple stage inputs.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Find the lowest price of all products
   * Function.min("price").as("lowestPrice");
   * }</pre>
   *
   * @param field The name of the field to find the minimum value of.
   * @return A new {@code Accumulator} representing the 'min' aggregation.
   */
  @BetaApi
  public static Min min(String field) {
    return new Min(Field.of(field), false);
  }

  /**
   * Creates an aggregation that finds the maximum value of an expression across multiple stage
   * inputs.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Find the highest score in a leaderboard
   * Function.max(Field.of("score")).as("highestScore");
   * }</pre>
   *
   * @param expr The expression to find the maximum value of.
   * @return A new {@code Accumulator} representing the 'max' aggregation.
   */
  @BetaApi
  public static Max max(Expr expr) {
    return new Max(expr, false);
  }

  /**
   * Creates an aggregation that finds the maximum value of a field across multiple stage inputs.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Find the highest score in a leaderboard
   * Function.max("score").as("highestScore");
   * }</pre>
   *
   * @param field The name of the field to find the maximum value of.
   * @return A new {@code Accumulator} representing the 'max' aggregation.
   */
  @BetaApi
  public static Max max(String field) {
    return new Max(Field.of(field), false);
  }

  /**
   * Creates an aggregation that counts the number of stage inputs with valid evaluations of the
   * provided expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Count the number of items where the price is greater than 10
   * Function.count(Field.of("price").gt(10)).as("expensiveItemCount");
   * }</pre>
   *
   * @param expr The expression to count.
   * @return A new {@code Accumulator} representing the 'count' aggregation.
   */
  @BetaApi
  public static Count count(Expr expr) {
    return new Count(expr, false);
  }

  /**
   * Creates an aggregation that counts the number of stage inputs with valid evaluations of the
   * provided field.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Count the total number of products
   * Function.count("productId").as("totalProducts");
   * }</pre>
   *
   * @param field The name of the field to count.
   * @return A new {@code Accumulator} representing the 'count' aggregation.
   */
  @BetaApi
  public static Count count(String field) {
    return new Count(Field.of(field), false);
  }

  /**
   * Creates an aggregation that counts the number of stage inputs that satisfy the provided filter
   * condition.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Count the number of completed orders
   * Function.countIf(Field.of("status").eq("completed")).as("completedOrderCount");
   * }</pre>
   *
   * @param condition The filter condition that needs to be met for the count to be incremented.
   * @return A new {@code Accumulator} representing the 'countIf' aggregation.
   */
  @BetaApi
  public static CountIf countIf(FilterCondition condition) {
    return new CountIf(condition, false);
  }

  /**
   * Creates an aggregation that counts the total number of stage inputs.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Count the total number of users
   * Function.countAll().as("totalUsers");
   * }</pre>
   *
   * @return A new {@code Accumulator} representing the 'countAll' aggregation.
   */
  @BetaApi
  public static Count countAll() {
    return new Count(null, false);
  }

  /**
   * Calculates the Cosine distance between two vector expressions.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the cosine distance between the 'userVector' field and the 'itemVector' field
   * Function.cosineDistance(Field.of("userVector"), Field.of("itemVector"));
   * }</pre>
   *
   * @param expr The first vector (represented as an Expr) to compare against.
   * @param other The other vector (represented as an Expr) to compare against.
   * @return A new {@code Expr} representing the cosine distance between the two vectors.
   */
  @BetaApi
  public static CosineDistance cosineDistance(Expr expr, Expr other) {
    return new CosineDistance(expr, other);
  }

  /**
   * Calculates the Cosine distance between a vector expression and a double array.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the cosine distance between the 'location' field and a target location
   * Function.cosineDistance(Field.of("location"), new double[] {37.7749, -122.4194});
   * }</pre>
   *
   * @param expr The first vector (represented as an Expr) to compare against.
   * @param other The other vector (as an array of doubles) to compare against.
   * @return A new {@code Expr} representing the cosine distance between the two vectors.
   */
  @BetaApi
  public static CosineDistance cosineDistance(Expr expr, double[] other) {
    return new CosineDistance(expr, Constant.vector(other));
  }

  /**
   * Calculates the Cosine distance between a field's vector value and a vector expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the cosine distance between the 'userVector' field and the 'itemVector' field
   * Function.cosineDistance("userVector", Field.of("itemVector"));
   * }</pre>
   *
   * @param field The name of the field containing the first vector.
   * @param other The other vector (represented as an Expr) to compare against.
   * @return A new {@code Expr} representing the cosine distance between the two vectors.
   */
  @BetaApi
  public static CosineDistance cosineDistance(String field, Expr other) {
    return new CosineDistance(Field.of(field), other);
  }

  /**
   * Calculates the Cosine distance between a field's vector value and a double array.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the Cosine distance between the 'location' field and a target location
   * Function.cosineDistance("location", new double[] {37.7749, -122.4194});
   * }</pre>
   *
   * @param field The name of the field containing the first vector.
   * @param other The other vector (as an array of doubles) to compare against.
   * @return A new {@code Expr} representing the Cosine distance between the two vectors.
   */
  @BetaApi
  public static CosineDistance cosineDistance(String field, double[] other) {
    return new CosineDistance(Field.of(field), Constant.vector(other));
  }

  /**
   * Calculates the dot product distance between two vector expressions.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the dot product distance between two document vectors: 'docVector1' and 'docVector2'
   * Function.dotProductDistance(Field.of("docVector1"), Field.of("docVector2"));
   * }</pre>
   *
   * @param expr The first vector (represented as an Expr) to compare against.
   * @param other The other vector (represented as an Expr) to compare against.
   * @return A new {@code Expr} representing the dot product distance between the two vectors.
   */
  @BetaApi
  public static DotProductDistance dotProductDistance(Expr expr, Expr other) {
    return new DotProductDistance(expr, other);
  }

  /**
   * Calculates the dot product distance between a vector expression and a double array.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the dot product distance between a feature vector and a target vector
   * Function.dotProductDistance(Field.of("features"), new double[] {0.5, 0.8, 0.2});
   * }</pre>
   *
   * @param expr The first vector (represented as an Expr) to compare against.
   * @param other The other vector (as an array of doubles) to compare against.
   * @return A new {@code Expr} representing the dot product distance between the two vectors.
   */
  @BetaApi
  public static DotProductDistance dotProductDistance(Expr expr, double[] other) {
    return new DotProductDistance(expr, Constant.vector(other));
  }

  /**
   * Calculates the dot product distance between a field's vector value and a vector expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the dot product distance between two document vectors: 'docVector1' and 'docVector2'
   * Function.dotProductDistance("docVector1", Field.of("docVector2"));
   * }</pre>
   *
   * @param field The name of the field containing the first vector.
   * @param other The other vector (represented as an Expr) to compare against.
   * @return A new {@code Expr} representing the dot product distance between the two vectors.
   */
  @BetaApi
  public static DotProductDistance dotProductDistance(String field, Expr other) {
    return new DotProductDistance(Field.of(field), other);
  }

  /**
   * Calculates the dot product distance between a field's vector value and a double array.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the dot product distance between a feature vector and a target vector
   * Function.dotProductDistance("features", new double[] {0.5, 0.8, 0.2});
   * }</pre>
   *
   * @param field The name of the field containing the first vector.
   * @param other The other vector (as an array of doubles) to compare against.
   * @return A new {@code Expr} representing the dot product distance between the two vectors.
   */
  @BetaApi
  public static DotProductDistance dotProductDistance(String field, double[] other) {
    return new DotProductDistance(Field.of(field), Constant.vector(other));
  }

  /**
   * Calculates the Euclidean distance between two vector expressions.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the Euclidean distance between two vector fields: 'pointA' and 'pointB'
   * Function.euclideanDistance(Field.of("pointA"), Field.of("pointB"));
   * }</pre>
   *
   * @param expr The first vector (represented as an Expr) to compare against.
   * @param other The other vector (represented as an Expr) to compare against.
   * @return A new {@code Expr} representing the Euclidean distance between the two vectors.
   */
  @BetaApi
  public static EuclideanDistance euclideanDistance(Expr expr, Expr other) {
    return new EuclideanDistance(expr, other);
  }

  /**
   * Calculates the Euclidean distance between a vector expression and a double array.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the Euclidean distance between the 'location' field and a target location
   * Function.euclideanDistance(Field.of("location"), new double[] {37.7749, -122.4194});
   * }</pre>
   *
   * @param expr The first vector (represented as an Expr) to compare against.
   * @param other The other vector (as an array of doubles) to compare against.
   * @return A new {@code Expr} representing the Euclidean distance between the two vectors.
   */
  @BetaApi
  public static EuclideanDistance euclideanDistance(Expr expr, double[] other) {
    return new EuclideanDistance(expr, Constant.vector(other));
  }

  /**
   * Calculates the Euclidean distance between a field's vector value and a vector expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the Euclidean distance between two vector fields: 'pointA' and 'pointB'
   * Function.euclideanDistance("pointA", Field.of("pointB"));
   * }</pre>
   *
   * @param field The name of the field containing the first vector.
   * @param other The other vector (represented as an Expr) to compare against.
   * @return A new {@code Expr} representing the Euclidean distance between the two vectors.
   */
  @BetaApi
  public static EuclideanDistance euclideanDistance(String field, Expr other) {
    return new EuclideanDistance(Field.of(field), other);
  }

  /**
   * Calculates the Euclidean distance between a field's vector value and a double array.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the Euclidean distance between the 'location' field and a target location
   * Function.euclideanDistance("location", new double[] {37.7749, -122.4194});
   * }</pre>
   *
   * @param field The name of the field containing the first vector.
   * @param other The other vector (as an array of doubles) to compare against.
   * @return A new {@code Expr} representing the Euclidean distance between the two vectors.
   */
  @BetaApi
  public static EuclideanDistance euclideanDistance(String field, double[] other) {
    return new EuclideanDistance(Field.of(field), Constant.vector(other));
  }

  /**
   * Returns an expression that represents an array element within an {@link ArrayFilter} or {@link
   * ArrayTransform} expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Get items from the 'inventoryPrices' array where the array item is greater than 0
   * Function.arrayFilter(Field.of("inventoryPrices"), Function.arrayElement().gt(0));
   * }</pre>
   *
   * @return A new {@code Expr} representing an array element.
   */
  @BetaApi
  public static ArrayElement arrayElement() {
    return new ArrayElement();
  }

  /**
   * Creates functions that work on the backend but do not exist in the SDK yet.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Call a user defined function named "myFunc" with the arguments 10 and 20
   * // This is the same of the 'Function.sum(Field.of("price"))', if it did not exist
   * Function.function("sum", Arrays.asList(Field.of("price")));
   * }</pre>
   *
   * @param name The name of the user defined function.
   * @param params The arguments to pass to the function.
   * @return A new {@code Function} representing the function call.
   */
  @BetaApi
  public static Function genericFunction(String name, List<Expr> params) {
    return new Function(name, params);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Function function = (Function) o;
    return Objects.equal(name, function.name) && Objects.equal(params, function.params);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, params);
  }
}
