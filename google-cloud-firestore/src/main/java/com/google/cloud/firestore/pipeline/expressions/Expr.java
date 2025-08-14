/*
 * Copyright 2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Blob;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.FieldPath;
import com.google.cloud.firestore.GeoPoint;
import com.google.cloud.firestore.VectorValue;
import com.google.common.collect.ImmutableList;
import com.google.firestore.v1.Value;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Represents an expression that can be evaluated to a value within the execution of a {@link
 * com.google.cloud.firestore.Pipeline}.
 *
 * <p>Expressions are the building blocks for creating complex queries and transformations in
 * Firestore pipelines. They can represent:
 *
 * <ul>
 *   <li>**Field references:** Access values from document fields.
 *   <li>**Literals:** Represent constant values (strings, numbers, booleans).
 *   <li>**Function calls:** Apply functions to one or more expressions.
 * </ul>
 *
 * <p>The `Expr` class provides a fluent API for building expressions. You can chain together method
 * calls to create complex expressions.
 */
@BetaApi
public abstract class Expr {

  /** Constructor is package-private to prevent extension. */
  Expr() {}

  private static Expr toExprOrConstant(Object o) {
    return o instanceof Expr ? (Expr) o : Constant.of(o);
  }

  private static ImmutableList<Expr> toArrayOfExprOrConstant(Object... others) {
    return Arrays.stream(others)
        .map(Expr::toExprOrConstant)
        .collect(ImmutableList.toImmutableList());
  }

  @InternalApi
  abstract Value toProto();

  // Constants
  /**
   * Create a constant for a {@link String} value.
   *
   * @param value The {@link String} value.
   * @return A new {@link Expr} constant instance.
   */
  @BetaApi
  public static Expr constant(String value) {
    return Constant.of(value);
  }

  /**
   * Create a constant for a {@link Number} value.
   *
   * @param value The {@link Number} value.
   * @return A new {@link Expr} constant instance.
   */
  @BetaApi
  public static Expr constant(Number value) {
    return Constant.of(value);
  }

  /**
   * Create a constant for a {@link Date} value.
   *
   * @param value The {@link Date} value.
   * @return A new {@link Expr} constant instance.
   */
  @BetaApi
  public static Expr constant(Date value) {
    return Constant.of(value);
  }

  /**
   * Create a constant for a {@link Timestamp} value.
   *
   * @param value The {@link Timestamp} value.
   * @return A new {@link Expr} constant instance.
   */
  @BetaApi
  public static Expr constant(Timestamp value) {
    return Constant.of(value);
  }

  /**
   * Create a constant for a {@link Boolean} value.
   *
   * @param value The {@link Boolean} value.
   * @return A new {@link BooleanExpr} constant instance.
   */
  @BetaApi
  public static BooleanExpr constant(Boolean value) {
    return new BooleanExpr("constant", Constant.of(value));
  }

  /**
   * Create a constant for a {@link GeoPoint} value.
   *
   * @param value The {@link GeoPoint} value.
   * @return A new {@link Expr} constant instance.
   */
  @BetaApi
  public static Expr constant(GeoPoint value) {
    return Constant.of(value);
  }

  /**
   * Create a constant for a {@link Blob} value.
   *
   * @param value The {@link Blob} value.
   * @return A new {@link Expr} constant instance.
   */
  @BetaApi
  public static Expr constant(Blob value) {
    return Constant.of(value);
  }

  /**
   * Create a constant for a {@link DocumentReference} value.
   *
   * @param value The {@link DocumentReference} value.
   * @return A new {@link Expr} constant instance.
   */
  @BetaApi
  public static Expr constant(DocumentReference value) {
    return Constant.of(value);
  }

  /**
   * Create a constant for a bytes value.
   *
   * @param value The bytes value.
   * @return A new {@link Expr} constant instance.
   */
  @BetaApi
  public static Expr constant(byte[] value) {
    return Constant.of(value);
  }

  /**
   * Create a constant for a {@link VectorValue} value.
   *
   * @param value The {@link VectorValue} value.
   * @return A new {@link Expr} constant instance.
   */
  @BetaApi
  public static Expr constant(VectorValue value) {
    return Constant.of(value);
  }

  /**
   * Create a {@link Blob} constant from a {@code byte[]}.
   *
   * @param bytes The {@code byte[]} to convert to a Blob.
   * @return A new {@link Expr} constant instance representing the Blob.
   */
  @BetaApi
  public static Expr blob(byte[] bytes) {
    return constant(Blob.fromBytes(bytes));
  }

  /**
   * Constant for a null value.
   *
   * @return An {@link Expr} constant instance.
   */
  @BetaApi
  public static Expr nullValue() {
    return Constant.nullValue();
  }

  /**
   * Create a vector constant for a {@code double[]} value.
   *
   * @param value The {@code double[]} value.
   * @return An {@link Expr} constant instance.
   */
  @BetaApi
  public static Expr vector(double[] value) {
    return Constant.vector(value);
  }

  /**
   * Create a vector constant for a {@link VectorValue} value.
   *
   * @param value The {@link VectorValue} value.
   * @return An {@link Expr} constant instance.
   */
  @BetaApi
  public static Expr vector(VectorValue value) {
    return Constant.of(value);
  }

  // Field Reference
  /**
   * Creates a {@link Field} instance representing the field at the given path.
   *
   * <p>The path can be a simple field name (e.g., "name") or a dot-separated path to a nested field
   * (e.g., "address.city").
   *
   * @param path The path to the field.
   * @return A new {@link Field} instance representing the specified path.
   */
  @BetaApi
  public static Field field(String path) {
    return Field.ofUserPath(path);
  }

  /**
   * Creates a {@link Field} instance representing the field at the given path.
   *
   * <p>The path can be a simple field name (e.g., "name") or a dot-separated path to a nested field
   * (e.g., "address.city").
   *
   * @param fieldPath The {@link FieldPath} to the field.
   * @return A new {@link Field} instance representing the specified path.
   */
  @BetaApi
  public static Field field(FieldPath fieldPath) {
    return Field.ofUserPath(fieldPath.toString());
  }

  // Generic Function
  /**
   * Creates a generic function expression that is not yet implemented.
   *
   * @param name The name of the generic function.
   * @param expr The expressions to be passed as arguments to the function.
   * @return A new {@link Expr} representing the generic function.
   */
  @BetaApi
  public static Expr generic(String name, Expr... expr) {
    return new FunctionExpr(name, ImmutableList.copyOf(expr));
  }

  // Logical Operators
  /**
   * Creates an expression that performs a logical 'AND' operation.
   *
   * @param condition The first {@link BooleanExpr}.
   * @param conditions Additional {@link BooleanExpr}s.
   * @return A new {@link BooleanExpr} representing the logical 'AND' operation.
   */
  @BetaApi
  public static BooleanExpr and(BooleanExpr condition, BooleanExpr... conditions) {
    ImmutableList.Builder<Expr> builder = ImmutableList.builder();
    builder.add(condition);
    builder.add(conditions);
    return new BooleanExpr("and", builder.build());
  }

  /**
   * Creates an expression that performs a logical 'OR' operation.
   *
   * @param condition The first {@link BooleanExpr}.
   * @param conditions Additional {@link BooleanExpr}s.
   * @return A new {@link BooleanExpr} representing the logical 'OR' operation.
   */
  @BetaApi
  public static BooleanExpr or(BooleanExpr condition, BooleanExpr... conditions) {
    ImmutableList.Builder<Expr> builder = ImmutableList.builder();
    builder.add(condition);
    builder.add(conditions);
    return new BooleanExpr("or", builder.build());
  }

  /**
   * Creates an expression that performs a logical 'XOR' operation.
   *
   * @param condition The first {@link BooleanExpr}.
   * @param conditions Additional {@link BooleanExpr}s.
   * @return A new {@link BooleanExpr} representing the logical 'XOR' operation.
   */
  @BetaApi
  public static BooleanExpr xor(BooleanExpr condition, BooleanExpr... conditions) {
    ImmutableList.Builder<Expr> builder = ImmutableList.builder();
    builder.add(condition);
    builder.add(conditions);
    return new BooleanExpr("xor", builder.build());
  }

  /**
   * Creates an expression that negates a boolean expression.
   *
   * @param condition The boolean expression to negate.
   * @return A new {@link BooleanExpr} representing the not operation.
   */
  @BetaApi
  public static BooleanExpr not(BooleanExpr condition) {
    return new BooleanExpr("not", condition);
  }

  // Arithmetic Operators
  /**
   * Creates an expression that adds numeric expressions.
   *
   * @param first Numeric expression to add.
   * @param second Numeric expression to add.
   * @return A new {@link Expr} representing the addition operation.
   */
  @BetaApi
  public static Expr add(Expr first, Expr second) {
    return new FunctionExpr("add", ImmutableList.of(first, second));
  }

  /**
   * Creates an expression that adds numeric expressions with a constant.
   *
   * @param first Numeric expression to add.
   * @param second Constant to add.
   * @return A new {@link Expr} representing the addition operation.
   */
  @BetaApi
  public static Expr add(Expr first, Number second) {
    return add(first, constant(second));
  }

  /**
   * Creates an expression that adds a numeric field with a numeric expression.
   *
   * @param fieldName Numeric field to add.
   * @param second Numeric expression to add to field value.
   * @return A new {@link Expr} representing the addition operation.
   */
  @BetaApi
  public static Expr add(String fieldName, Expr second) {
    return add(field(fieldName), second);
  }

  /**
   * Creates an expression that adds a numeric field with constant.
   *
   * @param fieldName Numeric field to add.
   * @param second Constant to add.
   * @return A new {@link Expr} representing the addition operation.
   */
  @BetaApi
  public static Expr add(String fieldName, Number second) {
    return add(field(fieldName), constant(second));
  }

  /**
   * Creates an expression that subtracts two expressions.
   *
   * @param minuend Numeric expression to subtract from.
   * @param subtrahend Numeric expression to subtract.
   * @return A new {@link Expr} representing the subtract operation.
   */
  @BetaApi
  public static Expr subtract(Expr minuend, Expr subtrahend) {
    return new FunctionExpr("subtract", ImmutableList.of(minuend, subtrahend));
  }

  /**
   * Creates an expression that subtracts a constant value from a numeric expression.
   *
   * @param minuend Numeric expression to subtract from.
   * @param subtrahend Constant to subtract.
   * @return A new {@link Expr} representing the subtract operation.
   */
  @BetaApi
  public static Expr subtract(Expr minuend, Number subtrahend) {
    return subtract(minuend, constant(subtrahend));
  }

  /**
   * Creates an expression that subtracts a numeric expressions from numeric field.
   *
   * @param fieldName Numeric field to subtract from.
   * @param subtrahend Numeric expression to subtract.
   * @return A new {@link Expr} representing the subtract operation.
   */
  @BetaApi
  public static Expr subtract(String fieldName, Expr subtrahend) {
    return subtract(field(fieldName), subtrahend);
  }

  /**
   * Creates an expression that subtracts a constant from numeric field.
   *
   * @param fieldName Numeric field to subtract from.
   * @param subtrahend Constant to subtract.
   * @return A new {@link Expr} representing the subtract operation.
   */
  @BetaApi
  public static Expr subtract(String fieldName, Number subtrahend) {
    return subtract(field(fieldName), constant(subtrahend));
  }

  /**
   * Creates an expression that multiplies numeric expressions.
   *
   * @param first Numeric expression to multiply.
   * @param second Numeric expression to multiply.
   * @return A new {@link Expr} representing the multiplication operation.
   */
  @BetaApi
  public static Expr multiply(Expr first, Expr second) {
    return new FunctionExpr("multiply", ImmutableList.of(first, second));
  }

  /**
   * Creates an expression that multiplies numeric expressions with a constant.
   *
   * @param first Numeric expression to multiply.
   * @param second Constant to multiply.
   * @return A new {@link Expr} representing the multiplication operation.
   */
  @BetaApi
  public static Expr multiply(Expr first, Number second) {
    return multiply(first, constant(second));
  }

  /**
   * Creates an expression that multiplies a numeric field with a numeric expression.
   *
   * @param fieldName Numeric field to multiply.
   * @param second Numeric expression to multiply.
   * @return A new {@link Expr} representing the multiplication operation.
   */
  @BetaApi
  public static Expr multiply(String fieldName, Expr second) {
    return multiply(field(fieldName), second);
  }

  /**
   * Creates an expression that multiplies a numeric field with a constant.
   *
   * @param fieldName Numeric field to multiply.
   * @param second Constant to multiply.
   * @return A new {@link Expr} representing the multiplication operation.
   */
  @BetaApi
  public static Expr multiply(String fieldName, Number second) {
    return multiply(field(fieldName), constant(second));
  }

  /**
   * Creates an expression that divides two numeric expressions.
   *
   * @param dividend The numeric expression to be divided.
   * @param divisor The numeric expression to divide by.
   * @return A new {@link Expr} representing the division operation.
   */
  @BetaApi
  public static Expr divide(Expr dividend, Expr divisor) {
    return new FunctionExpr("divide", ImmutableList.of(dividend, divisor));
  }

  /**
   * Creates an expression that divides a numeric expression by a constant.
   *
   * @param dividend The numeric expression to be divided.
   * @param divisor The constant to divide by.
   * @return A new {@link Expr} representing the division operation.
   */
  @BetaApi
  public static Expr divide(Expr dividend, Number divisor) {
    return divide(dividend, constant(divisor));
  }

  /**
   * Creates an expression that divides numeric field by a numeric expression.
   *
   * @param fieldName The numeric field name to be divided.
   * @param divisor The numeric expression to divide by.
   * @return A new {@link Expr} representing the divide operation.
   */
  @BetaApi
  public static Expr divide(String fieldName, Expr divisor) {
    return divide(field(fieldName), divisor);
  }

  /**
   * Creates an expression that divides a numeric field by a constant.
   *
   * @param fieldName The numeric field name to be divided.
   * @param divisor The constant to divide by.
   * @return A new {@link Expr} representing the divide operation.
   */
  @BetaApi
  public static Expr divide(String fieldName, Number divisor) {
    return divide(field(fieldName), constant(divisor));
  }

  /**
   * Creates an expression that calculates the modulo (remainder) of dividing two numeric
   * expressions.
   *
   * @param dividend The numeric expression to be divided.
   * @param divisor The numeric expression to divide by.
   * @return A new {@link Expr} representing the modulo operation.
   */
  @BetaApi
  public static Expr mod(Expr dividend, Expr divisor) {
    return new FunctionExpr("mod", ImmutableList.of(dividend, divisor));
  }

  /**
   * Creates an expression that calculates the modulo (remainder) of dividing a numeric expression
   * by a constant.
   *
   * @param dividend The numeric expression to be divided.
   * @param divisor The constant to divide by.
   * @return A new {@link Expr} representing the modulo operation.
   */
  @BetaApi
  public static Expr mod(Expr dividend, Number divisor) {
    return mod(dividend, constant(divisor));
  }

  /**
   * Creates an expression that calculates the modulo (remainder) of dividing a numeric field by a
   * constant.
   *
   * @param fieldName The numeric field name to be divided.
   * @param divisor The numeric expression to divide by.
   * @return A new {@link Expr} representing the modulo operation.
   */
  @BetaApi
  public static Expr mod(String fieldName, Expr divisor) {
    return mod(field(fieldName), divisor);
  }

  /**
   * Creates an expression that calculates the modulo (remainder) of dividing a numeric field by a
   * constant.
   *
   * @param fieldName The numeric field name to be divided.
   * @param divisor The constant to divide by.
   * @return A new {@link Expr} representing the modulo operation.
   */
  @BetaApi
  public static Expr mod(String fieldName, Number divisor) {
    return mod(field(fieldName), constant(divisor));
  }

  // Comparison Operators
  /**
   * Creates an expression that checks if two expressions are equal.
   *
   * @param left The first expression.
   * @param right The second expression.
   * @return A new {@link BooleanExpr} representing the equality comparison.
   */
  @BetaApi
  public static BooleanExpr eq(Expr left, Expr right) {
    return new BooleanExpr("eq", left, right);
  }

  /**
   * Creates an expression that checks if an expression is equal to a constant value.
   *
   * @param left The expression.
   * @param right The constant value.
   * @return A new {@link BooleanExpr} representing the equality comparison.
   */
  @BetaApi
  public static BooleanExpr eq(Expr left, Object right) {
    return new BooleanExpr("eq", left, toExprOrConstant(right));
  }

  /**
   * Creates an expression that checks if a field is equal to an expression.
   *
   * @param fieldName The field name.
   * @param right The expression.
   * @return A new {@link BooleanExpr} representing the equality comparison.
   */
  @BetaApi
  public static BooleanExpr eq(String fieldName, Expr right) {
    return eq(field(fieldName), right);
  }

  /**
   * Creates an expression that checks if a field is equal to a constant value.
   *
   * @param fieldName The field name.
   * @param right The constant value.
   * @return A new {@link BooleanExpr} representing the equality comparison.
   */
  @BetaApi
  public static BooleanExpr eq(String fieldName, Object right) {
    return eq(field(fieldName), toExprOrConstant(right));
  }

  /**
   * Creates an expression that checks if two expressions are not equal.
   *
   * @param left The first expression.
   * @param right The second expression.
   * @return A new {@link BooleanExpr} representing the inequality comparison.
   */
  @BetaApi
  public static BooleanExpr neq(Expr left, Expr right) {
    return new BooleanExpr("neq", left, right);
  }

  /**
   * Creates an expression that checks if an expression is not equal to a constant value.
   *
   * @param left The expression.
   * @param right The constant value.
   * @return A new {@link BooleanExpr} representing the inequality comparison.
   */
  @BetaApi
  public static BooleanExpr neq(Expr left, Object right) {
    return new BooleanExpr("neq", left, toExprOrConstant(right));
  }

  /**
   * Creates an expression that checks if a field is not equal to an expression.
   *
   * @param fieldName The field name.
   * @param right The expression.
   * @return A new {@link BooleanExpr} representing the inequality comparison.
   */
  @BetaApi
  public static BooleanExpr neq(String fieldName, Expr right) {
    return neq(field(fieldName), right);
  }

  /**
   * Creates an expression that checks if a field is not equal to a constant value.
   *
   * @param fieldName The field name.
   * @param right The constant value.
   * @return A new {@link BooleanExpr} representing the inequality comparison.
   */
  @BetaApi
  public static BooleanExpr neq(String fieldName, Object right) {
    return neq(field(fieldName), toExprOrConstant(right));
  }

  /**
   * Creates an expression that checks if the first expression is greater than the second
   * expression.
   *
   * @param left The first expression.
   * @param right The second expression.
   * @return A new {@link BooleanExpr} representing the greater than comparison.
   */
  @BetaApi
  public static BooleanExpr gt(Expr left, Expr right) {
    return new BooleanExpr("gt", left, right);
  }

  /**
   * Creates an expression that checks if an expression is greater than a constant value.
   *
   * @param left The expression.
   * @param right The constant value.
   * @return A new {@link BooleanExpr} representing the greater than comparison.
   */
  @BetaApi
  public static BooleanExpr gt(Expr left, Object right) {
    return new BooleanExpr("gt", left, toExprOrConstant(right));
  }

  /**
   * Creates an expression that checks if a field is greater than an expression.
   *
   * @param fieldName The field name.
   * @param right The expression.
   * @return A new {@link BooleanExpr} representing the greater than comparison.
   */
  @BetaApi
  public static BooleanExpr gt(String fieldName, Expr right) {
    return gt(field(fieldName), right);
  }

  /**
   * Creates an expression that checks if a field is greater than a constant value.
   *
   * @param fieldName The field name.
   * @param right The constant value.
   * @return A new {@link BooleanExpr} representing the greater than comparison.
   */
  @BetaApi
  public static BooleanExpr gt(String fieldName, Object right) {
    return gt(field(fieldName), toExprOrConstant(right));
  }

  /**
   * Creates an expression that checks if the first expression is greater than or equal to the
   * second expression.
   *
   * @param left The first expression.
   * @param right The second expression.
   * @return A new {@link BooleanExpr} representing the greater than or equal to comparison.
   */
  @BetaApi
  public static BooleanExpr gte(Expr left, Expr right) {
    return new BooleanExpr("gte", left, right);
  }

  /**
   * Creates an expression that checks if an expression is greater than or equal to a constant
   * value.
   *
   * @param left The expression.
   * @param right The constant value.
   * @return A new {@link BooleanExpr} representing the greater than or equal to comparison.
   */
  @BetaApi
  public static BooleanExpr gte(Expr left, Object right) {
    return new BooleanExpr("gte", left, toExprOrConstant(right));
  }

  /**
   * Creates an expression that checks if a field is greater than or equal to an expression.
   *
   * @param fieldName The field name.
   * @param right The expression.
   * @return A new {@link BooleanExpr} representing the greater than or equal to comparison.
   */
  @BetaApi
  public static BooleanExpr gte(String fieldName, Expr right) {
    return gte(field(fieldName), right);
  }

  /**
   * Creates an expression that checks if a field is greater than or equal to a constant value.
   *
   * @param fieldName The field name.
   * @param right The constant value.
   * @return A new {@link BooleanExpr} representing the greater than or equal to comparison.
   */
  @BetaApi
  public static BooleanExpr gte(String fieldName, Object right) {
    return gte(field(fieldName), toExprOrConstant(right));
  }

  /**
   * Creates an expression that checks if the first expression is less than the second expression.
   *
   * @param left The first expression.
   * @param right The second expression.
   * @return A new {@link BooleanExpr} representing the less than comparison.
   */
  @BetaApi
  public static BooleanExpr lt(Expr left, Expr right) {
    return new BooleanExpr("lt", left, right);
  }

  /**
   * Creates an expression that checks if an expression is less than a constant value.
   *
   * @param left The expression.
   * @param right The constant value.
   * @return A new {@link BooleanExpr} representing the less than comparison.
   */
  @BetaApi
  public static BooleanExpr lt(Expr left, Object right) {
    return new BooleanExpr("lt", left, toExprOrConstant(right));
  }

  /**
   * Creates an expression that checks if a field is less than an expression.
   *
   * @param fieldName The field name.
   * @param right The expression.
   * @return A new {@link BooleanExpr} representing the less than comparison.
   */
  @BetaApi
  public static BooleanExpr lt(String fieldName, Expr right) {
    return lt(field(fieldName), right);
  }

  /**
   * Creates an expression that checks if a field is less than a constant value.
   *
   * @param fieldName The field name.
   * @param right The constant value.
   * @return A new {@link BooleanExpr} representing the less than comparison.
   */
  @BetaApi
  public static BooleanExpr lt(String fieldName, Object right) {
    return lt(field(fieldName), toExprOrConstant(right));
  }

  /**
   * Creates an expression that checks if the first expression is less than or equal to the second
   * expression.
   *
   * @param left The first expression.
   * @param right The second expression.
   * @return A new {@link BooleanExpr} representing the less than or equal to comparison.
   */
  @BetaApi
  public static BooleanExpr lte(Expr left, Expr right) {
    return new BooleanExpr("lte", left, right);
  }

  /**
   * Creates an expression that checks if an expression is less than or equal to a constant value.
   *
   * @param left The expression.
   * @param right The constant value.
   * @return A new {@link BooleanExpr} representing the less than or equal to comparison.
   */
  @BetaApi
  public static BooleanExpr lte(Expr left, Object right) {
    return new BooleanExpr("lte", left, toExprOrConstant(right));
  }

  /**
   * Creates an expression that checks if a field is less than or equal to an expression.
   *
   * @param fieldName The field name.
   * @param right The expression.
   * @return A new {@link BooleanExpr} representing the less than or equal to comparison.
   */
  @BetaApi
  public static BooleanExpr lte(String fieldName, Expr right) {
    return lte(field(fieldName), right);
  }

  /**
   * Creates an expression that checks if a field is less than or equal to a constant value.
   *
   * @param fieldName The field name.
   * @param right The constant value.
   * @return A new {@link BooleanExpr} representing the less than or equal to comparison.
   */
  @BetaApi
  public static BooleanExpr lte(String fieldName, Object right) {
    return lte(field(fieldName), toExprOrConstant(right));
  }

  /**
   * Creates an expression that checks if an {@code expression}, when evaluated, is equal to any of
   * the provided {@code values}.
   *
   * @param expression The expression whose results to compare.
   * @param values The values to check against.
   * @return A new {@link BooleanExpr} representing the 'IN' comparison.
   */
  @BetaApi
  public static BooleanExpr eqAny(Expr expression, List<Object> values) {
    return new BooleanExpr(
        "eq_any", expression, new FunctionExpr("array", toArrayOfExprOrConstant(values.toArray())));
  }

  /**
   * Creates an expression that checks if an {@code expression}, when evaluated, is equal to any of
   * the elements of {@code arrayExpression}.
   *
   * @param expression The expression whose results to compare.
   * @param arrayExpression An expression that evaluates to an array, whose elements to check for
   *     equality to the input.
   * @return A new {@link BooleanExpr} representing the 'IN' comparison.
   */
  @BetaApi
  public static BooleanExpr eqAny(Expr expression, Expr arrayExpression) {
    return new BooleanExpr("eq_any", expression, arrayExpression);
  }

  /**
   * Creates an expression that checks if a field's value is equal to any of the provided {@code
   * values}.
   *
   * @param fieldName The field to compare.
   * @param values The values to check against.
   * @return A new {@link BooleanExpr} representing the 'IN' comparison.
   */
  @BetaApi
  public static BooleanExpr eqAny(String fieldName, List<Object> values) {
    return eqAny(
        field(fieldName), new FunctionExpr("array", toArrayOfExprOrConstant(values.toArray())));
  }

  /**
   * Creates an expression that checks if a field's value is equal to any of the elements of {@code
   * arrayExpression}.
   *
   * @param fieldName The field to compare.
   * @param arrayExpression An expression that evaluates to an array, whose elements to check for
   *     equality to the input.
   * @return A new {@link BooleanExpr} representing the 'IN' comparison.
   */
  @BetaApi
  public static BooleanExpr eqAny(String fieldName, Expr arrayExpression) {
    return eqAny(field(fieldName), arrayExpression);
  }

  /**
   * Creates an expression that checks if an {@code expression}, when evaluated, is not equal to all
   * the provided {@code values}.
   *
   * @param expression The expression whose results to compare.
   * @param values The values to check against.
   * @return A new {@link BooleanExpr} representing the 'NOT IN' comparison.
   */
  @BetaApi
  public static BooleanExpr notEqAny(Expr expression, List<Object> values) {
    return new BooleanExpr(
        "not_eq_any",
        expression,
        new FunctionExpr("array", toArrayOfExprOrConstant(values.toArray())));
  }

  /**
   * Creates an expression that checks if an {@code expression}, when evaluated, is not equal to all
   * the elements of {@code arrayExpression}.
   *
   * @param expression The expression whose results to compare.
   * @param arrayExpression An expression that evaluates to an array, whose elements to check for
   *     equality to the input.
   * @return A new {@link BooleanExpr} representing the 'NOT IN' comparison.
   */
  @BetaApi
  public static BooleanExpr notEqAny(Expr expression, Expr arrayExpression) {
    return new BooleanExpr("not_eq_any", expression, arrayExpression);
  }

  /**
   * Creates an expression that checks if a field's value is not equal to all of the provided {@code
   * values}.
   *
   * @param fieldName The field to compare.
   * @param values The values to check against.
   * @return A new {@link BooleanExpr} representing the 'NOT IN' comparison.
   */
  @BetaApi
  public static BooleanExpr notEqAny(String fieldName, List<Object> values) {
    return notEqAny(
        field(fieldName), new FunctionExpr("array", toArrayOfExprOrConstant(values.toArray())));
  }

  /**
   * Creates an expression that checks if a field's value is not equal to all of the elements of
   * {@code arrayExpression}.
   *
   * @param fieldName The field to compare.
   * @param arrayExpression An expression that evaluates to an array, whose elements to check for
   *     equality to the input.
   * @return A new {@link BooleanExpr} representing the 'NOT IN' comparison.
   */
  @BetaApi
  public static BooleanExpr notEqAny(String fieldName, Expr arrayExpression) {
    return notEqAny(field(fieldName), arrayExpression);
  }

  // String Functions
  /**
   * Creates an expression that calculates the character length of a string expression in UTF8.
   *
   * @param string The expression representing the string.
   * @return A new {@link Expr} representing the charLength operation.
   */
  @BetaApi
  public static Expr charLength(Expr string) {
    return new FunctionExpr("char_length", ImmutableList.of(string));
  }

  /**
   * Creates an expression that calculates the character length of a string field in UTF8.
   *
   * @param fieldName The name of the field containing the string.
   * @return A new {@link Expr} representing the charLength operation.
   */
  @BetaApi
  public static Expr charLength(String fieldName) {
    return charLength(field(fieldName));
  }

  /**
   * Creates an expression that calculates the length of a string in UTF-8 bytes, or just the length
   * of a Blob.
   *
   * @param string The expression representing the string.
   * @return A new {@link Expr} representing the length of the string in bytes.
   */
  @BetaApi
  public static Expr byteLength(Expr string) {
    return new FunctionExpr("byte_length", ImmutableList.of(string));
  }

  /**
   * Creates an expression that calculates the length of a string represented by a field in UTF-8
   * bytes, or just the length of a Blob.
   *
   * @param fieldName The name of the field containing the string.
   * @return A new {@link Expr} representing the length of the string in bytes.
   */
  @BetaApi
  public static Expr byteLength(String fieldName) {
    return byteLength(field(fieldName));
  }

  /**
   * Creates an expression that calculates the length of string, array, map, vector, or Blob.
   *
   * @param string The expression representing the value to calculate the length of.
   * @return A new {@link Expr} representing the length of the value.
   */
  @BetaApi
  public static Expr length(Expr string) {
    return new FunctionExpr("length", ImmutableList.of(string));
  }

  /**
   * Creates an expression that calculates the length of string, array, map, vector, or Blob.
   *
   * @param fieldName The name of the field containing the value.
   * @return A new {@link Expr} representing the length of the value.
   */
  @BetaApi
  public static Expr length(String fieldName) {
    return byteLength(field(fieldName));
  }

  /**
   * Creates an expression that performs a case-sensitive wildcard string comparison.
   *
   * @param string The expression representing the string to perform the comparison on.
   * @param pattern The pattern to search for. You can use "%" as a wildcard character.
   * @return A new {@link BooleanExpr} representing the like operation.
   */
  @BetaApi
  public static BooleanExpr like(Expr string, Expr pattern) {
    return new BooleanExpr("like", string, pattern);
  }

  /**
   * Creates an expression that performs a case-sensitive wildcard string comparison.
   *
   * @param string The expression representing the string to perform the comparison on.
   * @param pattern The pattern to search for. You can use "%" as a wildcard character.
   * @return A new {@link BooleanExpr} representing the like operation.
   */
  @BetaApi
  public static BooleanExpr like(Expr string, String pattern) {
    return like(string, constant(pattern));
  }

  /**
   * Creates an expression that performs a case-sensitive wildcard string comparison against a
   * field.
   *
   * @param fieldName The name of the field containing the string.
   * @param pattern The pattern to search for. You can use "%" as a wildcard character.
   * @return A new {@link BooleanExpr} representing the like comparison.
   */
  @BetaApi
  public static BooleanExpr like(String fieldName, Expr pattern) {
    return like(field(fieldName), pattern);
  }

  /**
   * Creates an expression that performs a case-sensitive wildcard string comparison against a
   * field.
   *
   * @param fieldName The name of the field containing the string.
   * @param pattern The pattern to search for. You can use "%" as a wildcard character.
   * @return A new {@link BooleanExpr} representing the like comparison.
   */
  @BetaApi
  public static BooleanExpr like(String fieldName, String pattern) {
    return like(field(fieldName), constant(pattern));
  }

  /**
   * Creates an expression that checks if a string expression contains a specified regular
   * expression as a substring.
   *
   * @param string The expression representing the string to perform the comparison on.
   * @param pattern The regular expression to use for the search.
   * @return A new {@link BooleanExpr} representing the contains regular expression comparison.
   */
  @BetaApi
  public static BooleanExpr regexContains(Expr string, Expr pattern) {
    return new BooleanExpr("regex_contains", string, pattern);
  }

  /**
   * Creates an expression that checks if a string expression contains a specified regular
   * expression as a substring.
   *
   * @param string The expression representing the string to perform the comparison on.
   * @param pattern The regular expression to use for the search.
   * @return A new {@link BooleanExpr} representing the contains regular expression comparison.
   */
  @BetaApi
  public static BooleanExpr regexContains(Expr string, String pattern) {
    return regexContains(string, constant(pattern));
  }

  /**
   * Creates an expression that checks if a string field contains a specified regular expression as
   * a substring.
   *
   * @param fieldName The name of the field containing the string.
   * @param pattern The regular expression to use for the search.
   * @return A new {@link BooleanExpr} representing the contains regular expression comparison.
   */
  @BetaApi
  public static BooleanExpr regexContains(String fieldName, Expr pattern) {
    return regexContains(field(fieldName), pattern);
  }

  /**
   * Creates an expression that checks if a string field contains a specified regular expression as
   * a substring.
   *
   * @param fieldName The name of the field containing the string.
   * @param pattern The regular expression to use for the search.
   * @return A new {@link BooleanExpr} representing the contains regular expression comparison.
   */
  @BetaApi
  public static BooleanExpr regexContains(String fieldName, String pattern) {
    return regexContains(field(fieldName), constant(pattern));
  }

  /**
   * Creates an expression that checks if a string field matches a specified regular expression.
   *
   * @param string The expression representing the string to match against.
   * @param pattern The regular expression to use for the match.
   * @return A new {@link BooleanExpr} representing the regular expression match comparison.
   */
  @BetaApi
  public static BooleanExpr regexMatch(Expr string, Expr pattern) {
    return new BooleanExpr("regex_match", string, pattern);
  }

  /**
   * Creates an expression that checks if a string field matches a specified regular expression.
   *
   * @param string The expression representing the string to match against.
   * @param pattern The regular expression to use for the match.
   * @return A new {@link BooleanExpr} representing the regular expression match comparison.
   */
  @BetaApi
  public static BooleanExpr regexMatch(Expr string, String pattern) {
    return regexMatch(string, constant(pattern));
  }

  /**
   * Creates an expression that checks if a string field matches a specified regular expression.
   *
   * @param fieldName The name of the field containing the string.
   * @param pattern The regular expression to use for the match.
   * @return A new {@link BooleanExpr} representing the regular expression match comparison.
   */
  @BetaApi
  public static BooleanExpr regexMatch(String fieldName, Expr pattern) {
    return regexMatch(field(fieldName), pattern);
  }

  /**
   * Creates an expression that checks if a string field matches a specified regular expression.
   *
   * @param fieldName The name of the field containing the string.
   * @param pattern The regular expression to use for the match.
   * @return A new {@link BooleanExpr} representing the regular expression match comparison.
   */
  @BetaApi
  public static BooleanExpr regexMatch(String fieldName, String pattern) {
    return regexMatch(field(fieldName), constant(pattern));
  }

  /**
   * Creates an expression that reverses a string.
   *
   * @param string An expression evaluating to a string value, which will be reversed.
   * @return A new {@link Expr} representing the reversed string.
   */
  @BetaApi
  public static Expr strReverse(Expr string) {
    return new FunctionExpr("str_reverse", ImmutableList.of(string));
  }

  /**
   * Creates an expression that reverses a string value from the specified field.
   *
   * @param fieldName The name of the field that contains the string to reverse.
   * @return A new {@link Expr} representing the reversed string.
   */
  @BetaApi
  public static Expr strReverse(String fieldName) {
    return strReverse(field(fieldName));
  }

  /**
   * Creates an expression that checks if a string expression contains a specified substring.
   *
   * @param string The expression representing the string to perform the comparison on.
   * @param substring The expression representing the substring to search for.
   * @return A new {@link BooleanExpr} representing the contains comparison.
   */
  @BetaApi
  public static BooleanExpr strContains(Expr string, Expr substring) {
    return new BooleanExpr("str_contains", string, substring);
  }

  /**
   * Creates an expression that checks if a string expression contains a specified substring.
   *
   * @param string The expression representing the string to perform the comparison on.
   * @param substring The substring to search for.
   * @return A new {@link BooleanExpr} representing the contains comparison.
   */
  @BetaApi
  public static BooleanExpr strContains(Expr string, String substring) {
    return strContains(string, constant(substring));
  }

  /**
   * Creates an expression that checks if a string field contains a specified substring.
   *
   * @param fieldName The name of the field to perform the comparison on.
   * @param substring The expression representing the substring to search for.
   * @return A new {@link BooleanExpr} representing the contains comparison.
   */
  @BetaApi
  public static BooleanExpr strContains(String fieldName, Expr substring) {
    return strContains(field(fieldName), substring);
  }

  /**
   * Creates an expression that checks if a string field contains a specified substring.
   *
   * @param fieldName The name of the field to perform the comparison on.
   * @param substring The substring to search for.
   * @return A new {@link BooleanExpr} representing the contains comparison.
   */
  @BetaApi
  public static BooleanExpr strContains(String fieldName, String substring) {
    return strContains(field(fieldName), constant(substring));
  }

  /**
   * Creates an expression that checks if a string expression starts with a given {@code prefix}.
   *
   * @param string The expression to check.
   * @param prefix The prefix string expression to check for.
   * @return A new {@link BooleanExpr} representing the 'starts with' comparison.
   */
  @BetaApi
  public static BooleanExpr startsWith(Expr string, Expr prefix) {
    return new BooleanExpr("starts_with", string, prefix);
  }

  /**
   * Creates an expression that checks if a string expression starts with a given {@code prefix}.
   *
   * @param string The expression to check.
   * @param prefix The prefix string to check for.
   * @return A new {@link BooleanExpr} representing the 'starts with' comparison.
   */
  @BetaApi
  public static BooleanExpr startsWith(Expr string, String prefix) {
    return startsWith(string, constant(prefix));
  }

  /**
   * Creates an expression that checks if a string expression starts with a given {@code prefix}.
   *
   * @param fieldName The name of field that contains a string to check.
   * @param prefix The prefix string expression to check for.
   * @return A new {@link BooleanExpr} representing the 'starts with' comparison.
   */
  @BetaApi
  public static BooleanExpr startsWith(String fieldName, Expr prefix) {
    return startsWith(field(fieldName), prefix);
  }

  /**
   * Creates an expression that checks if a string expression starts with a given {@code prefix}.
   *
   * @param fieldName The name of field that contains a string to check.
   * @param prefix The prefix string to check for.
   * @return A new {@link BooleanExpr} representing the 'starts with' comparison.
   */
  @BetaApi
  public static BooleanExpr startsWith(String fieldName, String prefix) {
    return startsWith(field(fieldName), constant(prefix));
  }

  /**
   * Creates an expression that checks if a string expression ends with a given {@code suffix}.
   *
   * @param string The expression to check.
   * @param suffix The suffix string expression to check for.
   * @return A new {@link BooleanExpr} representing the 'ends with' comparison.
   */
  @BetaApi
  public static BooleanExpr endsWith(Expr string, Expr suffix) {
    return new BooleanExpr("ends_with", string, suffix);
  }

  /**
   * Creates an expression that checks if a string expression ends with a given {@code suffix}.
   *
   * @param string The expression to check.
   * @param suffix The suffix string to check for.
   * @return A new {@link BooleanExpr} representing the 'ends with' comparison.
   */
  @BetaApi
  public static BooleanExpr endsWith(Expr string, String suffix) {
    return endsWith(string, constant(suffix));
  }

  /**
   * Creates an expression that checks if a string expression ends with a given {@code suffix}.
   *
   * @param fieldName The name of field that contains a string to check.
   * @param suffix The suffix string expression to check for.
   * @return A new {@link BooleanExpr} representing the 'ends with' comparison.
   */
  @BetaApi
  public static BooleanExpr endsWith(String fieldName, Expr suffix) {
    return endsWith(field(fieldName), suffix);
  }

  /**
   * Creates an expression that checks if a string expression ends with a given {@code suffix}.
   *
   * @param fieldName The name of field that contains a string to check.
   * @param suffix The suffix string to check for.
   * @return A new {@link BooleanExpr} representing the 'ends with' comparison.
   */
  @BetaApi
  public static BooleanExpr endsWith(String fieldName, String suffix) {
    return endsWith(field(fieldName), constant(suffix));
  }

  /**
   * Creates an expression that returns a substring of the given string.
   *
   * @param string The expression representing the string to get a substring from.
   * @param index The starting index of the substring.
   * @param length The length of the substring.
   * @return A new {@link Expr} representing the substring.
   */
  @BetaApi
  public static Expr substring(Expr string, Expr index, Expr length) {
    return new FunctionExpr("substr", ImmutableList.of(string, index, length));
  }

  /**
   * Creates an expression that returns a substring of the given string.
   *
   * @param fieldName The name of the field containing the string to get a substring from.
   * @param index The starting index of the substring.
   * @param length The length of the substring.
   * @return A new {@link Expr} representing the substring.
   */
  @BetaApi
  public static Expr substring(String fieldName, int index, int length) {
    return substring(field(fieldName), constant(index), constant(length));
  }

  /**
   * Creates an expression that converts a string expression to lowercase.
   *
   * @param string The expression representing the string to convert to lowercase.
   * @return A new {@link Expr} representing the lowercase string.
   */
  @BetaApi
  public static Expr toLower(Expr string) {
    return new FunctionExpr("to_lower", ImmutableList.of(string));
  }

  /**
   * Creates an expression that converts a string field to lowercase.
   *
   * @param fieldName The name of the field containing the string to convert to lowercase.
   * @return A new {@link Expr} representing the lowercase string.
   */
  @BetaApi
  public static Expr toLower(String fieldName) {
    return toLower(field(fieldName));
  }

  /**
   * Creates an expression that converts a string expression to uppercase.
   *
   * @param string The expression representing the string to convert to uppercase.
   * @return A new {@link Expr} representing the lowercase string.
   */
  @BetaApi
  public static Expr toUpper(Expr string) {
    return new FunctionExpr("to_upper", ImmutableList.of(string));
  }

  /**
   * Creates an expression that converts a string field to uppercase.
   *
   * @param fieldName The name of the field containing the string to convert to uppercase.
   * @return A new {@link Expr} representing the lowercase string.
   */
  @BetaApi
  public static Expr toUpper(String fieldName) {
    return toUpper(field(fieldName));
  }

  /**
   * Creates an expression that removes leading and trailing whitespace from a string expression.
   *
   * @param string The expression representing the string to trim.
   * @return A new {@link Expr} representing the trimmed string.
   */
  @BetaApi
  public static Expr trim(Expr string) {
    return new FunctionExpr("trim", ImmutableList.of(string));
  }

  /**
   * Creates an expression that removes leading and trailing whitespace from a string field.
   *
   * @param fieldName The name of the field containing the string to trim.
   * @return A new {@link Expr} representing the trimmed string.
   */
  @BetaApi
  public static Expr trim(String fieldName) {
    return trim(field(fieldName));
  }

  /**
   * Creates an expression that concatenates string expressions together.
   *
   * @param firstString The expression representing the initial string value.
   * @param otherStrings Optional additional string expressions or string constants to concatenate.
   * @return A new {@link Expr} representing the concatenated string.
   */
  @BetaApi
  public static Expr strConcat(Expr firstString, Object... otherStrings) {
    ImmutableList.Builder<Expr> builder = ImmutableList.builder();
    builder.add(firstString);
    builder.addAll(toArrayOfExprOrConstant(otherStrings));
    return new FunctionExpr("str_concat", builder.build());
  }

  /**
   * Creates an expression that concatenates string expressions together.
   *
   * @param fieldName The field name containing the initial string value.
   * @param otherStrings Optional additional string expressions or string constants to concatenate.
   * @return A new {@link Expr} representing the concatenated string.
   */
  @BetaApi
  public static Expr strConcat(String fieldName, Object... otherStrings) {
    return strConcat(field(fieldName), otherStrings);
  }

  // Map Functions
  /**
   * Creates an expression that creates a Firestore map value from an input object.
   *
   * @param elements The input map to evaluate in the expression.
   * @return A new {@link Expr} representing the map function.
   */
  @BetaApi
  public static Expr map(Map<String, Object> elements) {
    ImmutableList<Expr> params =
        elements.entrySet().stream()
            .flatMap(
                e -> Arrays.asList(constant(e.getKey()), toExprOrConstant(e.getValue())).stream())
            .collect(ImmutableList.toImmutableList());
    return new FunctionExpr("map", params);
  }

  /**
   * Accesses a value from a map (object) field using the provided {@code keyExpression}.
   *
   * @param map The expression representing the map.
   * @param key The key to access in the map.
   * @return A new {@link Expr} representing the value associated with the given key in the map.
   */
  @BetaApi
  public static Expr mapGet(Expr map, Expr key) {
    return new FunctionExpr("map_get", ImmutableList.of(map, key));
  }

  /**
   * Accesses a value from a map (object) field using the provided {@code key}.
   *
   * @param map The expression representing the map.
   * @param key The key to access in the map.
   * @return A new {@link Expr} representing the value associated with the given key in the map.
   */
  @BetaApi
  public static Expr mapGet(Expr map, String key) {
    return mapGet(map, constant(key));
  }

  /**
   * Accesses a value from a map (object) field using the provided {@code key}.
   *
   * @param fieldName The field name of the map field.
   * @param key The key to access in the map.
   * @return A new {@link Expr} representing the value associated with the given key in the map.
   */
  @BetaApi
  public static Expr mapGet(String fieldName, String key) {
    return mapGet(field(fieldName), constant(key));
  }

  /**
   * Accesses a value from a map (object) field using the provided {@code keyExpression}.
   *
   * @param fieldName The field name of the map field.
   * @param key The key to access in the map.
   * @return A new {@link Expr} representing the value associated with the given key in the map.
   */
  @BetaApi
  public static Expr mapGet(String fieldName, Expr key) {
    return mapGet(field(fieldName), key);
  }

  /**
   * Creates an expression that merges multiple maps into a single map. If multiple maps have the
   * same key, the later value is used.
   *
   * @param firstMap First map expression that will be merged.
   * @param secondMap Second map expression that will be merged.
   * @param otherMaps Additional maps to merge.
   * @return A new {@link Expr} representing the mapMerge operation.
   */
  @BetaApi
  public static Expr mapMerge(Expr firstMap, Expr secondMap, Expr... otherMaps) {
    ImmutableList.Builder<Expr> builder = ImmutableList.builder();
    builder.add(firstMap);
    builder.add(secondMap);
    builder.add(otherMaps);
    return new FunctionExpr("map_merge", builder.build());
  }

  /**
   * Creates an expression that merges multiple maps into a single map. If multiple maps have the
   * same key, the later value is used.
   *
   * @param firstMapFieldName Field name of the first map expression that will be merged.
   * @param secondMap Second map expression that will be merged.
   * @param otherMaps Additional maps to merge.
   * @return A new {@link Expr} representing the mapMerge operation.
   */
  @BetaApi
  public static Expr mapMerge(String firstMapFieldName, Expr secondMap, Expr... otherMaps) {
    return mapMerge(field(firstMapFieldName), secondMap, otherMaps);
  }

  /**
   * Creates an expression that removes a key from a map.
   *
   * @param mapExpr The expression representing the map.
   * @param key The key to remove from the map.
   * @return A new {@link Expr} representing the map with the key removed.
   */
  @BetaApi
  public static Expr mapRemove(Expr mapExpr, Expr key) {
    return new FunctionExpr("map_remove", ImmutableList.of(mapExpr, key));
  }

  /**
   * Creates an expression that removes a key from a map.
   *
   * @param mapField The field name of the map.
   * @param key The key to remove from the map.
   * @return A new {@link Expr} representing the map with the key removed.
   */
  @BetaApi
  public static Expr mapRemove(String mapField, Expr key) {
    return mapRemove(field(mapField), key);
  }

  /**
   * Creates an expression that removes a key from a map.
   *
   * @param mapExpr The expression representing the map.
   * @param key The key to remove from the map.
   * @return A new {@link Expr} representing the map with the key removed.
   */
  @BetaApi
  public static Expr mapRemove(Expr mapExpr, String key) {
    return mapRemove(mapExpr, constant(key));
  }

  /**
   * Creates an expression that removes a key from a map.
   *
   * @param mapField The field name of the map.
   * @param key The key to remove from the map.
   * @return A new {@link Expr} representing the map with the key removed.
   */
  @BetaApi
  public static Expr mapRemove(String mapField, String key) {
    return mapRemove(field(mapField), key);
  }

  // Array Functions
  /**
   * Creates an expression that creates a Firestore array value from an input object.
   *
   * @param elements The input elements to evaluate in the expression.
   * @return A new {@link Expr} representing the array function.
   */
  @BetaApi
  public static Expr array(Object... elements) {
    return new FunctionExpr("array", toArrayOfExprOrConstant(elements));
  }

  /**
   * Creates an expression that creates a Firestore array value from an input object.
   *
   * @param elements The input elements to evaluate in the expression.
   * @return A new {@link Expr} representing the array function.
   */
  @BetaApi
  public static Expr array(List<Object> elements) {
    return new FunctionExpr("array", toArrayOfExprOrConstant(elements.toArray()));
  }

  /**
   * Creates an expression that concatenates multiple arrays into a single array.
   *
   * @param firstArray The first array expression to concatenate.
   * @param otherArrays Additional arrays to concatenate.
   * @return A new {@link Expr} representing the concatenated array.
   */
  @BetaApi
  public static Expr arrayConcat(Expr firstArray, Object... otherArrays) {
    ImmutableList.Builder<Expr> builder = ImmutableList.builder();
    builder.add(firstArray);
    builder.addAll(toArrayOfExprOrConstant(otherArrays));
    return new FunctionExpr("array_concat", builder.build());
  }

  /**
   * Creates an expression that concatenates multiple arrays into a single array.
   *
   * @param firstArrayField The field name of the first array to concatenate.
   * @param otherArrays Additional arrays to concatenate.
   * @return A new {@link Expr} representing the concatenated array.
   */
  @BetaApi
  public static Expr arrayConcat(String firstArrayField, Object... otherArrays) {
    return arrayConcat(field(firstArrayField), otherArrays);
  }

  /**
   * Creates an expression that reverses an array.
   *
   * @param array The expression representing the array to reverse.
   * @return A new {@link Expr} representing the reversed array.
   */
  @BetaApi
  public static Expr arrayReverse(Expr array) {
    return new FunctionExpr("array_reverse", ImmutableList.of(array));
  }

  /**
   * Creates an expression that reverses an array.
   *
   * @param arrayFieldName The field name of the array to reverse.
   * @return A new {@link Expr} representing the reversed array.
   */
  @BetaApi
  public static Expr arrayReverse(String arrayFieldName) {
    return arrayReverse(field(arrayFieldName));
  }

  /**
   * Creates an expression that checks if an array contains a specified element.
   *
   * @param array The expression representing the array.
   * @param element The element to check for.
   * @return A new {@link BooleanExpr} representing the array contains comparison.
   */
  @BetaApi
  public static BooleanExpr arrayContains(Expr array, Expr element) {
    return new BooleanExpr("array_contains", array, element);
  }

  /**
   * Creates an expression that checks if an array contains a specified element.
   *
   * @param arrayFieldName The field name of the array.
   * @param element The element to check for.
   * @return A new {@link BooleanExpr} representing the array contains comparison.
   */
  @BetaApi
  public static BooleanExpr arrayContains(String arrayFieldName, Expr element) {
    return arrayContains(field(arrayFieldName), element);
  }

  /**
   * Creates an expression that checks if an array contains a specified element.
   *
   * @param array The expression representing the array.
   * @param element The element to check for.
   * @return A new {@link BooleanExpr} representing the array contains comparison.
   */
  @BetaApi
  public static BooleanExpr arrayContains(Expr array, Object element) {
    return arrayContains(array, toExprOrConstant(element));
  }

  /**
   * Creates an expression that checks if an array contains a specified element.
   *
   * @param arrayFieldName The field name of the array.
   * @param element The element to check for.
   * @return A new {@link BooleanExpr} representing the array contains comparison.
   */
  @BetaApi
  public static BooleanExpr arrayContains(String arrayFieldName, Object element) {
    return arrayContains(field(arrayFieldName), toExprOrConstant(element));
  }

  /**
   * Creates an expression that checks if an array contains all of the provided values.
   *
   * @param array The expression representing the array.
   * @param values The values to check for.
   * @return A new {@link BooleanExpr} representing the array contains all comparison.
   */
  @BetaApi
  public static BooleanExpr arrayContainsAll(Expr array, List<Object> values) {
    return arrayContainsAll(array, array(values));
  }

  /**
   * Creates an expression that checks if an array contains all of the elements of another array.
   *
   * @param array The expression representing the array.
   * @param arrayExpression The expression representing the array of values to check for.
   * @return A new {@link BooleanExpr} representing the array contains all comparison.
   */
  @BetaApi
  public static BooleanExpr arrayContainsAll(Expr array, Expr arrayExpression) {
    return new BooleanExpr("array_contains_all", array, arrayExpression);
  }

  /**
   * Creates an expression that checks if an array contains all of the provided values.
   *
   * @param arrayFieldName The field name of the array.
   * @param values The values to check for.
   * @return A new {@link BooleanExpr} representing the array contains all comparison.
   */
  @BetaApi
  public static BooleanExpr arrayContainsAll(String arrayFieldName, List<Object> values) {
    return arrayContainsAll(field(arrayFieldName), array(values));
  }

  /**
   * Creates an expression that checks if an array contains all of the elements of another array.
   *
   * @param arrayFieldName The field name of the array.
   * @param arrayExpression The expression representing the array of values to check for.
   * @return A new {@link BooleanExpr} representing the array contains all comparison.
   */
  @BetaApi
  public static BooleanExpr arrayContainsAll(String arrayFieldName, Expr arrayExpression) {
    return arrayContainsAll(field(arrayFieldName), arrayExpression);
  }

  /**
   * Creates an expression that checks if an array contains any of the provided values.
   *
   * @param array The expression representing the array.
   * @param values The values to check for.
   * @return A new {@link BooleanExpr} representing the array contains any comparison.
   */
  @BetaApi
  public static BooleanExpr arrayContainsAny(Expr array, List<Object> values) {
    return new BooleanExpr("array_contains_any", array, array(values));
  }

  /**
   * Creates an expression that checks if an array contains any of the elements of another array.
   *
   * @param array The expression representing the array.
   * @param arrayExpression The expression representing the array of values to check for.
   * @return A new {@link BooleanExpr} representing the array contains any comparison.
   */
  @BetaApi
  public static BooleanExpr arrayContainsAny(Expr array, Expr arrayExpression) {
    return new BooleanExpr("array_contains_any", array, arrayExpression);
  }

  /**
   * Creates an expression that checks if an array contains any of the provided values.
   *
   * @param arrayFieldName The field name of the array.
   * @param values The values to check for.
   * @return A new {@link BooleanExpr} representing the array contains any comparison.
   */
  @BetaApi
  public static BooleanExpr arrayContainsAny(String arrayFieldName, List<Object> values) {
    return arrayContainsAny(field(arrayFieldName), array(values));
  }

  /**
   * Creates an expression that checks if an array contains any of the elements of another array.
   *
   * @param arrayFieldName The field name of the array.
   * @param arrayExpression The expression representing the array of values to check for.
   * @return A new {@link BooleanExpr} representing the array contains any comparison.
   */
  @BetaApi
  public static BooleanExpr arrayContainsAny(String arrayFieldName, Expr arrayExpression) {
    return arrayContainsAny(field(arrayFieldName), arrayExpression);
  }

  /**
   * Creates an expression that returns the length of an array.
   *
   * @param array The expression representing the array.
   * @return A new {@link Expr} representing the length of the array.
   */
  @BetaApi
  public static Expr arrayLength(Expr array) {
    return new FunctionExpr("array_length", ImmutableList.of(array));
  }

  /**
   * Creates an expression that returns the length of an array.
   *
   * @param arrayFieldName The field name of the array.
   * @return A new {@link Expr} representing the length of the array.
   */
  @BetaApi
  public static Expr arrayLength(String arrayFieldName) {
    return arrayLength(field(arrayFieldName));
  }

  /**
   * Creates an expression that returns an element from an array at a specified index.
   *
   * @param array The expression representing the array.
   * @param offset The index of the element to return.
   * @return A new {@link Expr} representing the element at the specified index.
   */
  @BetaApi
  public static Expr arrayGet(Expr array, Expr offset) {
    return new FunctionExpr("array_get", ImmutableList.of(array, offset));
  }

  /**
   * Creates an expression that returns an element from an array at a specified index.
   *
   * @param array The expression representing the array.
   * @param offset The index of the element to return.
   * @return A new {@link Expr} representing the element at the specified index.
   */
  @BetaApi
  public static Expr arrayGet(Expr array, int offset) {
    return arrayGet(array, constant(offset));
  }

  /**
   * Creates an expression that returns an element from an array at a specified index.
   *
   * @param arrayFieldName The field name of the array.
   * @param offset The index of the element to return.
   * @return A new {@link Expr} representing the element at the specified index.
   */
  @BetaApi
  public static Expr arrayGet(String arrayFieldName, Expr offset) {
    return arrayGet(field(arrayFieldName), offset);
  }

  /**
   * Creates an expression that returns an element from an array at a specified index.
   *
   * @param arrayFieldName The field name of the array.
   * @param offset The index of the element to return.
   * @return A new {@link Expr} representing the element at the specified index.
   */
  @BetaApi
  public static Expr arrayGet(String arrayFieldName, int offset) {
    return arrayGet(field(arrayFieldName), constant(offset));
  }

  // Vector Functions
  /**
   * Creates an expression that calculates the cosine distance between two vectors.
   *
   * @param vector1 The first vector.
   * @param vector2 The second vector.
   * @return A new {@link Expr} representing the cosine distance.
   */
  @BetaApi
  public static Expr cosineDistance(Expr vector1, Expr vector2) {
    return new FunctionExpr("cosine_distance", ImmutableList.of(vector1, vector2));
  }

  /**
   * Creates an expression that calculates the cosine distance between two vectors.
   *
   * @param vector1 The first vector.
   * @param vector2 The second vector.
   * @return A new {@link Expr} representing the cosine distance.
   */
  @BetaApi
  public static Expr cosineDistance(Expr vector1, double[] vector2) {
    return cosineDistance(vector1, vector(vector2));
  }

  /**
   * Creates an expression that calculates the cosine distance between two vectors.
   *
   * @param vectorFieldName The field name of the first vector.
   * @param vector The second vector.
   * @return A new {@link Expr} representing the cosine distance.
   */
  @BetaApi
  public static Expr cosineDistance(String vectorFieldName, Expr vector) {
    return cosineDistance(field(vectorFieldName), vector);
  }

  /**
   * Creates an expression that calculates the cosine distance between two vectors.
   *
   * @param vectorFieldName The field name of the first vector.
   * @param vector The second vector.
   * @return A new {@link Expr} representing the cosine distance.
   */
  @BetaApi
  public static Expr cosineDistance(String vectorFieldName, double[] vector) {
    return cosineDistance(field(vectorFieldName), vector(vector));
  }

  /**
   * Creates an expression that calculates the dot product of two vectors.
   *
   * @param vector1 The first vector.
   * @param vector2 The second vector.
   * @return A new {@link Expr} representing the dot product.
   */
  @BetaApi
  public static Expr dotProduct(Expr vector1, Expr vector2) {
    return new FunctionExpr("dot_product", ImmutableList.of(vector1, vector2));
  }

  /**
   * Creates an expression that calculates the dot product of two vectors.
   *
   * @param vector1 The first vector.
   * @param vector2 The second vector.
   * @return A new {@link Expr} representing the dot product.
   */
  @BetaApi
  public static Expr dotProduct(Expr vector1, double[] vector2) {
    return dotProduct(vector1, vector(vector2));
  }

  /**
   * Creates an expression that calculates the dot product of two vectors.
   *
   * @param vectorFieldName The field name of the first vector.
   * @param vector The second vector.
   * @return A new {@link Expr} representing the dot product.
   */
  @BetaApi
  public static Expr dotProduct(String vectorFieldName, Expr vector) {
    return dotProduct(field(vectorFieldName), vector);
  }

  /**
   * Creates an expression that calculates the dot product of two vectors.
   *
   * @param vectorFieldName The field name of the first vector.
   * @param vector The second vector.
   * @return A new {@link Expr} representing the dot product.
   */
  @BetaApi
  public static Expr dotProduct(String vectorFieldName, double[] vector) {
    return dotProduct(field(vectorFieldName), vector(vector));
  }

  /**
   * Creates an expression that calculates the Euclidean distance between two vectors.
   *
   * @param vector1 The first vector.
   * @param vector2 The second vector.
   * @return A new {@link Expr} representing the Euclidean distance.
   */
  @BetaApi
  public static Expr euclideanDistance(Expr vector1, Expr vector2) {
    return new FunctionExpr("euclidean_distance", ImmutableList.of(vector1, vector2));
  }

  /**
   * Creates an expression that calculates the Euclidean distance between two vectors.
   *
   * @param vector1 The first vector.
   * @param vector2 The second vector.
   * @return A new {@link Expr} representing the Euclidean distance.
   */
  @BetaApi
  public static Expr euclideanDistance(Expr vector1, double[] vector2) {
    return euclideanDistance(vector1, vector(vector2));
  }

  /**
   * Creates an expression that calculates the Euclidean distance between two vectors.
   *
   * @param vectorFieldName The field name of the first vector.
   * @param vector The second vector.
   * @return A new {@link Expr} representing the Euclidean distance.
   */
  @BetaApi
  public static Expr euclideanDistance(String vectorFieldName, Expr vector) {
    return euclideanDistance(field(vectorFieldName), vector);
  }

  /**
   * Creates an expression that calculates the Euclidean distance between two vectors.
   *
   * @param vectorFieldName The field name of the first vector.
   * @param vector The second vector.
   * @return A new {@link Expr} representing the Euclidean distance.
   */
  @BetaApi
  public static Expr euclideanDistance(String vectorFieldName, double[] vector) {
    return euclideanDistance(field(vectorFieldName), vector(vector));
  }

  /**
   * Creates an expression that calculates the length of a vector.
   *
   * @param vectorExpression The expression representing the vector.
   * @return A new {@link Expr} representing the length of the vector.
   */
  @BetaApi
  public static Expr vectorLength(Expr vectorExpression) {
    return new FunctionExpr("vector_length", ImmutableList.of(vectorExpression));
  }

  /**
   * Creates an expression that calculates the length of a vector.
   *
   * @param fieldName The field name of the vector.
   * @return A new {@link Expr} representing the length of the vector.
   */
  @BetaApi
  public static Expr vectorLength(String fieldName) {
    return vectorLength(field(fieldName));
  }

  // Timestamp Functions
  /**
   * Creates an expression that converts a Unix timestamp in microseconds to a Firestore timestamp.
   *
   * @param expr The expression representing the Unix timestamp in microseconds.
   * @return A new {@link Expr} representing the Firestore timestamp.
   */
  @BetaApi
  public static Expr unixMicrosToTimestamp(Expr expr) {
    return new FunctionExpr("unix_micros_to_timestamp", ImmutableList.of(expr));
  }

  /**
   * Creates an expression that interprets a field's value as the number of microseconds since the
   * Unix epoch (1970-01-01 00:00:00 UTC) and returns a timestamp.
   *
   * @param fieldName The name of the field containing the number of microseconds since epoch.
   * @return A new {@link Expr} representing the timestamp.
   */
  @BetaApi
  public static Expr unixMicrosToTimestamp(String fieldName) {
    return unixMicrosToTimestamp(field(fieldName));
  }

  /**
   * Creates an expression that converts a timestamp expression to the number of microseconds since
   * the Unix epoch (1970-01-01 00:00:00 UTC).
   *
   * @param expr The expression representing the timestamp.
   * @return A new {@link Expr} representing the number of microseconds since epoch.
   */
  @BetaApi
  public static Expr timestampToUnixMicros(Expr expr) {
    return new FunctionExpr("timestamp_to_unix_micros", ImmutableList.of(expr));
  }

  /**
   * Creates an expression that converts a timestamp field to the number of microseconds since the
   * Unix epoch (1970-01-01 00:00:00 UTC).
   *
   * @param fieldName The name of the field that contains the timestamp.
   * @return A new {@link Expr} representing the number of microseconds since epoch.
   */
  @BetaApi
  public static Expr timestampToUnixMicros(String fieldName) {
    return timestampToUnixMicros(field(fieldName));
  }

  /**
   * Creates an expression that interprets an expression as the number of milliseconds since the
   * Unix epoch (1970-01-01 00:00:00 UTC) and returns a timestamp.
   *
   * @param expr The expression representing the number of milliseconds since epoch.
   * @return A new {@link Expr} representing the timestamp.
   */
  @BetaApi
  public static Expr unixMillisToTimestamp(Expr expr) {
    return new FunctionExpr("unix_millis_to_timestamp", ImmutableList.of(expr));
  }

  /**
   * Creates an expression that interprets a field's value as the number of milliseconds since the
   * Unix epoch (1970-01-01 00:00:00 UTC) and returns a timestamp.
   *
   * @param fieldName The name of the field containing the number of milliseconds since epoch.
   * @return A new {@link Expr} representing the timestamp.
   */
  @BetaApi
  public static Expr unixMillisToTimestamp(String fieldName) {
    return unixMillisToTimestamp(field(fieldName));
  }

  /**
   * Creates an expression that converts a timestamp expression to the number of milliseconds since
   * the Unix epoch (1970-01-01 00:00:00 UTC).
   *
   * @param expr The expression representing the timestamp.
   * @return A new {@link Expr} representing the number of milliseconds since epoch.
   */
  @BetaApi
  public static Expr timestampToUnixMillis(Expr expr) {
    return new FunctionExpr("timestamp_to_unix_millis", ImmutableList.of(expr));
  }

  /**
   * Creates an expression that converts a timestamp field to the number of milliseconds since the
   * Unix epoch (1970-01-01 00:00:00 UTC).
   *
   * @param fieldName The name of the field that contains the timestamp.
   * @return A new {@link Expr} representing the number of milliseconds since epoch.
   */
  @BetaApi
  public static Expr timestampToUnixMillis(String fieldName) {
    return timestampToUnixMillis(field(fieldName));
  }

  /**
   * Creates an expression that interprets an expression as the number of seconds since the Unix
   * epoch (1970-01-01 00:00:00 UTC) and returns a timestamp.
   *
   * @param expr The expression representing the number of seconds since epoch.
   * @return A new {@link Expr} representing the timestamp.
   */
  @BetaApi
  public static Expr unixSecondsToTimestamp(Expr expr) {
    return new FunctionExpr("unix_seconds_to_timestamp", ImmutableList.of(expr));
  }

  /**
   * Creates an expression that interprets a field's value as the number of seconds since the Unix
   * epoch (1970-01-01 00:00:00 UTC) and returns a timestamp.
   *
   * @param fieldName The name of the field containing the number of seconds since epoch.
   * @return A new {@link Expr} representing the timestamp.
   */
  @BetaApi
  public static Expr unixSecondsToTimestamp(String fieldName) {
    return unixSecondsToTimestamp(field(fieldName));
  }

  /**
   * Creates an expression that converts a timestamp expression to the number of seconds since the
   * Unix epoch (1970-01-01 00:00:00 UTC).
   *
   * @param expr The expression representing the timestamp.
   * @return A new {@link Expr} representing the number of seconds since epoch.
   */
  @BetaApi
  public static Expr timestampToUnixSeconds(Expr expr) {
    return new FunctionExpr("timestamp_to_unix_seconds", ImmutableList.of(expr));
  }

  /**
   * Creates an expression that converts a timestamp field to the number of seconds since the Unix
   * epoch (1970-01-01 00:00:00 UTC).
   *
   * @param fieldName The name of the field that contains the timestamp.
   * @return A new {@link Expr} representing the number of seconds since epoch.
   */
  @BetaApi
  public static Expr timestampToUnixSeconds(String fieldName) {
    return timestampToUnixSeconds(field(fieldName));
  }

  /**
   * Creates an expression that adds a specified amount of time to a timestamp.
   *
   * @param timestamp The expression representing the timestamp.
   * @param unit The expression representing the unit of time to add. Valid units include
   *     "microsecond", "millisecond", "second", "minute", "hour" and "day".
   * @param amount The expression representing the amount of time to add.
   * @return A new {@link Expr} representing the resulting timestamp.
   */
  @BetaApi
  public static Expr timestampAdd(Expr timestamp, Expr unit, Expr amount) {
    return new FunctionExpr("timestamp_add", ImmutableList.of(timestamp, unit, amount));
  }

  /**
   * Creates an expression that adds a specified amount of time to a timestamp.
   *
   * @param timestamp The expression representing the timestamp.
   * @param unit The unit of time to add. Valid units include "microsecond", "millisecond",
   *     "second", "minute", "hour" and "day".
   * @param amount The amount of time to add.
   * @return A new {@link Expr} representing the resulting timestamp.
   */
  @BetaApi
  public static Expr timestampAdd(Expr timestamp, String unit, long amount) {
    return timestampAdd(timestamp, constant(unit), constant(amount));
  }

  /**
   * Creates an expression that adds a specified amount of time to a timestamp.
   *
   * @param fieldName The name of the field that contains the timestamp.
   * @param unit The expression representing the unit of time to add. Valid units include
   *     "microsecond", "millisecond", "second", "minute", "hour" and "day".
   * @param amount The expression representing the amount of time to add.
   * @return A new {@link Expr} representing the resulting timestamp.
   */
  @BetaApi
  public static Expr timestampAdd(String fieldName, Expr unit, Expr amount) {
    return timestampAdd(field(fieldName), unit, amount);
  }

  /**
   * Creates an expression that adds a specified amount of time to a timestamp.
   *
   * @param fieldName The name of the field that contains the timestamp.
   * @param unit The unit of time to add. Valid units include "microsecond", "millisecond",
   *     "second", "minute", "hour" and "day".
   * @param amount The amount of time to add.
   * @return A new {@link Expr} representing the resulting timestamp.
   */
  @BetaApi
  public static Expr timestampAdd(String fieldName, String unit, long amount) {
    return timestampAdd(field(fieldName), constant(unit), constant(amount));
  }

  /**
   * Creates an expression that subtracts a specified amount of time to a timestamp.
   *
   * @param timestamp The expression representing the timestamp.
   * @param unit The expression representing the unit of time to subtract. Valid units include
   *     "microsecond", "millisecond", "second", "minute", "hour" and "day".
   * @param amount The expression representing the amount of time to subtract.
   * @return A new {@link Expr} representing the resulting timestamp.
   */
  @BetaApi
  public static Expr timestampSub(Expr timestamp, Expr unit, Expr amount) {
    return new FunctionExpr("timestamp_sub", ImmutableList.of(timestamp, unit, amount));
  }

  /**
   * Creates an expression that subtracts a specified amount of time to a timestamp.
   *
   * @param timestamp The expression representing the timestamp.
   * @param unit The unit of time to subtract. Valid units include "microsecond", "millisecond",
   *     "second", "minute", "hour" and "day".
   * @param amount The amount of time to subtract.
   * @return A new {@link Expr} representing the resulting timestamp.
   */
  @BetaApi
  public static Expr timestampSub(Expr timestamp, String unit, long amount) {
    return timestampSub(timestamp, constant(unit), constant(amount));
  }

  /**
   * Creates an expression that subtracts a specified amount of time to a timestamp.
   *
   * @param fieldName The name of the field that contains the timestamp.
   * @param unit The unit of time to subtract. Valid units include "microsecond", "millisecond",
   *     "second", "minute", "hour" and "day".
   * @param amount The amount of time to subtract.
   * @return A new {@link Expr} representing the resulting timestamp.
   */
  @BetaApi
  public static Expr timestampSub(String fieldName, Expr unit, Expr amount) {
    return timestampSub(field(fieldName), unit, amount);
  }

  /**
   * Creates an expression that subtracts a specified amount of time to a timestamp.
   *
   * @param fieldName The name of the field that contains the timestamp.
   * @param unit The unit of time to subtract. Valid units include "microsecond", "millisecond",
   *     "second", "minute", "hour" and "day".
   * @param amount The amount of time to subtract.
   * @return A new {@link Expr} representing the resulting timestamp.
   */
  @BetaApi
  public static Expr timestampSub(String fieldName, String unit, long amount) {
    return timestampSub(field(fieldName), constant(unit), constant(amount));
  }

  // Conditional Functions
  /**
   * Creates a conditional expression that evaluates to a {@code thenExpr} expression if a condition
   * is true or an {@code elseExpr} expression if the condition is false.
   *
   * @param condition The condition to evaluate.
   * @param thenExpr The expression to evaluate if the condition is true.
   * @param elseExpr The expression to evaluate if the condition is false.
   * @return A new {@link Expr} representing the conditional operation.
   */
  @BetaApi
  public static Expr cond(BooleanExpr condition, Expr thenExpr, Expr elseExpr) {
    return new FunctionExpr("cond", ImmutableList.of(condition, thenExpr, elseExpr));
  }

  /**
   * Creates a conditional expression that evaluates to a {@code thenValue} if a condition is true
   * or an {@code elseValue} if the condition is false.
   *
   * @param condition The condition to evaluate.
   * @param thenValue Value if the condition is true.
   * @param elseValue Value if the condition is false.
   * @return A new {@link Expr} representing the conditional operation.
   */
  @BetaApi
  public static Expr cond(BooleanExpr condition, Object thenValue, Object elseValue) {
    return cond(condition, toExprOrConstant(thenValue), toExprOrConstant(elseValue));
  }

  // Error Handling Functions
  /**
   * Creates an expression that returns the {@code catchExpr} argument if there is an error, else
   * return the result of the {@code tryExpr} argument evaluation.
   *
   * @param tryExpr The try expression.
   * @param catchExpr The catch expression that will be evaluated and returned if the {@code
   *     tryExpr} produces an error.
   * @return A new {@link Expr} representing the ifError operation.
   */
  @BetaApi
  public static Expr ifError(Expr tryExpr, Expr catchExpr) {
    return new FunctionExpr("if_error", ImmutableList.of(tryExpr, catchExpr));
  }

  /**
   * Creates an expression that returns the {@code catchExpr} argument if there is an error, else
   * return the result of the {@code tryExpr} argument evaluation.
   *
   * <p>This overload will return {@link BooleanExpr} when both parameters are also {@link
   * BooleanExpr}.
   *
   * @param tryExpr The try boolean expression.
   * @param catchExpr The catch boolean expression that will be evaluated and returned if the {@code
   *     tryExpr} produces an error.
   * @return A new {@link BooleanExpr} representing the ifError operation.
   */
  @BetaApi
  public static BooleanExpr ifError(BooleanExpr tryExpr, BooleanExpr catchExpr) {
    return new BooleanExpr("if_error", tryExpr, catchExpr);
  }

  /**
   * Creates an expression that returns the {@code catchValue} argument if there is an error, else
   * return the result of the {@code tryExpr} argument evaluation.
   *
   * @param tryExpr The try expression.
   * @param catchValue The value that will be returned if the {@code tryExpr} produces an error.
   * @return A new {@link Expr} representing the ifError operation.
   */
  @BetaApi
  public static Expr ifError(Expr tryExpr, Object catchValue) {
    return ifError(tryExpr, toExprOrConstant(catchValue));
  }

  /**
   * Creates an expression that checks if a given expression produces an error.
   *
   * @param expr The expression to check.
   * @return A new {@link BooleanExpr} representing the `isError` check.
   */
  @BetaApi
  public static BooleanExpr isError(Expr expr) {
    return new BooleanExpr("is_error", expr);
  }

  // Other Utility Functions
  /**
   * Creates an expression that returns the document ID from a path.
   *
   * @param documentPath An expression the evaluates to document path.
   * @return A new {@link Expr} representing the documentId operation.
   */
  @BetaApi
  public static Expr documentId(Expr documentPath) {
    return new FunctionExpr("document_id", ImmutableList.of(documentPath));
  }

  /**
   * Creates an expression that returns the document ID from a path.
   *
   * @param documentPath The string representation of the document path.
   * @return A new {@link Expr} representing the documentId operation.
   */
  @BetaApi
  public static Expr documentId(String documentPath) {
    return documentId(constant(documentPath));
  }

  /**
   * Creates an expression that returns the document ID from a {@link DocumentReference}.
   *
   * @param docRef The {@link DocumentReference}.
   * @return A new {@link Expr} representing the documentId operation.
   */
  @BetaApi
  public static Expr documentId(DocumentReference docRef) {
    return documentId(constant(docRef));
  }

  /**
   * Creates an expression that returns the collection ID from a path.
   *
   * @param path An expression the evaluates to document path.
   * @return A new {@link Expr} representing the collectionId operation.
   */
  @BetaApi
  public static Expr collectionId(Expr path) {
    return new FunctionExpr("collection_id", ImmutableList.of(path));
  }

  /**
   * Creates an expression that returns the collection ID from a path.
   *
   * @param pathFieldName The field name of the path.
   * @return A new {@link Expr} representing the collectionId operation.
   */
  @BetaApi
  public static Expr collectionId(String pathFieldName) {
    return collectionId(field(pathFieldName));
  }

  // Type Checking Functions
  /**
   * Creates an expression that checks if a field exists.
   *
   * @param value An expression evaluates to the name of the field to check.
   * @return A new {@link Expr} representing the exists check.
   */
  @BetaApi
  public static BooleanExpr exists(Expr value) {
    return new BooleanExpr("exists", value);
  }

  /**
   * Creates an expression that checks if a field exists.
   *
   * @param fieldName The field name to check.
   * @return A new {@link Expr} representing the exists check.
   */
  @BetaApi
  public static BooleanExpr exists(String fieldName) {
    return exists(field(fieldName));
  }

  /**
   * Creates an expression that returns true if a value is absent. Otherwise, returns false even if
   * the value is null.
   *
   * @param value The expression to check.
   * @return A new {@link BooleanExpr} representing the isAbsent operation.
   */
  @BetaApi
  public static BooleanExpr isAbsent(Expr value) {
    return new BooleanExpr("is_absent", value);
  }

  /**
   * Creates an expression that returns true if a field is absent. Otherwise, returns false even if
   * the field value is null.
   *
   * @param fieldName The field to check.
   * @return A new {@link BooleanExpr} representing the isAbsent operation.
   */
  @BetaApi
  public static BooleanExpr isAbsent(String fieldName) {
    return isAbsent(field(fieldName));
  }

  /**
   * Creates an expression that checks if an expression evaluates to 'NaN' (Not a Number).
   *
   * @param value The expression to check.
   * @return A new {@link BooleanExpr} representing the isNan operation.
   */
  @BetaApi
  public static BooleanExpr isNaN(Expr value) {
    return new BooleanExpr("is_nan", value);
  }

  /**
   * Creates an expression that checks if a field's value evaluates to 'NaN' (Not a Number).
   *
   * @param fieldName The field to check.
   * @return A new {@link BooleanExpr} representing the isNan operation.
   */
  @BetaApi
  public static BooleanExpr isNaN(String fieldName) {
    return isNaN(field(fieldName));
  }

  /**
   * Creates an expression that checks if the result of an expression is null.
   *
   * @param value The expression to check.
   * @return A new {@link BooleanExpr} representing the isNull operation.
   */
  @BetaApi
  public static BooleanExpr isNull(Expr value) {
    return new BooleanExpr("is_null", value);
  }

  /**
   * Creates an expression that checks if the value of a field is null.
   *
   * @param fieldName The field to check.
   * @return A new {@link BooleanExpr} representing the isNull operation.
   */
  @BetaApi
  public static BooleanExpr isNull(String fieldName) {
    return isNull(field(fieldName));
  }

  /**
   * Creates an expression that checks if the result of an expression is not null.
   *
   * @param value The expression to check.
   * @return A new {@link BooleanExpr} representing the isNotNull operation.
   */
  @BetaApi
  public static BooleanExpr isNotNull(Expr value) {
    return new BooleanExpr("is_not_null", value);
  }

  /**
   * Creates an expression that checks if the value of a field is not null.
   *
   * @param fieldName The field to check.
   * @return A new {@link BooleanExpr} representing the isNotNull operation.
   */
  @BetaApi
  public static BooleanExpr isNotNull(String fieldName) {
    return isNotNull(field(fieldName));
  }

  // Numeric Operations
  /**
   * Creates an expression that rounds {@code numericExpr} to nearest integer.
   *
   * <p>Rounds away from zero in halfway cases.
   *
   * @param numericExpr An expression that returns number when evaluated.
   * @return A new {@link Expr} representing an integer result from the round operation.
   */
  @BetaApi
  public static Expr round(Expr numericExpr) {
    return new FunctionExpr("round", ImmutableList.of(numericExpr));
  }

  /**
   * Creates an expression that rounds {@code numericField} to nearest integer.
   *
   * <p>Rounds away from zero in halfway cases.
   *
   * @param numericField Name of field that returns number when evaluated.
   * @return A new {@link Expr} representing an integer result from the round operation.
   */
  @BetaApi
  public static Expr round(String numericField) {
    return round(field(numericField));
  }

  /**
   * Creates an expression that rounds off {@code numericExpr} to {@code decimalPlace} decimal
   * places if {@code decimalPlace} is positive, rounds off digits to the left of the decimal point
   * if {@code decimalPlace} is negative. Rounds away from zero in halfway cases.
   *
   * @param numericExpr An expression that returns number when evaluated.
   * @param decimalPlace The number of decimal places to round.
   * @return A new {@link Expr} representing the round operation.
   */
  @BetaApi
  public static Expr roundToPrecision(Expr numericExpr, int decimalPlace) {
    return new FunctionExpr("round", ImmutableList.of(numericExpr, constant(decimalPlace)));
  }

  /**
   * Creates an expression that rounds off {@code numericField} to {@code decimalPlace} decimal
   * places if {@code decimalPlace} is positive, rounds off digits to the left of the decimal point
   * if {@code decimalPlace} is negative. Rounds away from zero in halfway cases.
   *
   * @param numericField Name of field that returns number when evaluated.
   * @param decimalPlace The number of decimal places to round.
   * @return A new {@link Expr} representing the round operation.
   */
  @BetaApi
  public static Expr roundToPrecision(String numericField, int decimalPlace) {
    return roundToPrecision(field(numericField), decimalPlace);
  }

  /**
   * Creates an expression that rounds off {@code numericExpr} to {@code decimalPlace} decimal
   * places if {@code decimalPlace} is positive, rounds off digits to the left of the decimal point
   * if {@code decimalPlace} is negative. Rounds away from zero in halfway cases.
   *
   * @param numericExpr An expression that returns number when evaluated.
   * @param decimalPlace The number of decimal places to round.
   * @return A new {@link Expr} representing the round operation.
   */
  @BetaApi
  public static Expr roundToPrecision(Expr numericExpr, Expr decimalPlace) {
    return new FunctionExpr("round", ImmutableList.of(numericExpr, decimalPlace));
  }

  /**
   * Creates an expression that rounds off {@code numericField} to {@code decimalPlace} decimal
   * places if {@code decimalPlace} is positive, rounds off digits to the left of the decimal point
   * if {@code decimalPlace} is negative. Rounds away from zero in halfway cases.
   *
   * @param numericField Name of field that returns number when evaluated.
   * @param decimalPlace The number of decimal places to round.
   * @return A new {@link Expr} representing the round operation.
   */
  @BetaApi
  public static Expr roundToPrecision(String numericField, Expr decimalPlace) {
    return roundToPrecision(field(numericField), decimalPlace);
  }

  /**
   * Creates an expression that returns the smallest integer that isn't less than {@code
   * numericExpr}.
   *
   * @param numericExpr An expression that returns number when evaluated.
   * @return A new {@link Expr} representing an integer result from the ceil operation.
   */
  @BetaApi
  public static Expr ceil(Expr numericExpr) {
    return new FunctionExpr("ceil", ImmutableList.of(numericExpr));
  }

  /**
   * Creates an expression that returns the smallest integer that isn't less than {@code
   * numericField}.
   *
   * @param numericField Name of field that returns number when evaluated.
   * @return A new {@link Expr} representing an integer result from the ceil operation.
   */
  @BetaApi
  public static Expr ceil(String numericField) {
    return ceil(field(numericField));
  }

  /**
   * Creates an expression that returns the largest integer that isn't less than {@code
   * numericExpr}.
   *
   * @param numericExpr An expression that returns number when evaluated.
   * @return A new {@link Expr} representing an integer result from the floor operation.
   */
  @BetaApi
  public static Expr floor(Expr numericExpr) {
    return new FunctionExpr("floor", ImmutableList.of(numericExpr));
  }

  /**
   * Creates an expression that returns the largest integer that isn't less than {@code
   * numericField}.
   *
   * @param numericField Name of field that returns number when evaluated.
   * @return A new {@link Expr} representing an integer result from the floor operation.
   */
  @BetaApi
  public static Expr floor(String numericField) {
    return floor(field(numericField));
  }

  /**
   * Creates an expression that returns the {@code numericExpr} raised to the power of the {@code
   * exponent}. Returns infinity on overflow and zero on underflow.
   *
   * @param numericExpr An expression that returns number when evaluated.
   * @param exponent The numeric power to raise the {@code numericExpr}.
   * @return A new {@link Expr} representing a numeric result from raising {@code numericExpr} to
   *     the power of {@code exponent}.
   */
  @BetaApi
  public static Expr pow(Expr numericExpr, Number exponent) {
    return new FunctionExpr("pow", ImmutableList.of(numericExpr, constant(exponent)));
  }

  /**
   * Creates an expression that returns the {@code numericField} raised to the power of the {@code
   * exponent}. Returns infinity on overflow and zero on underflow.
   *
   * @param numericField Name of field that returns number when evaluated.
   * @param exponent The numeric power to raise the {@code numericField}.
   * @return A new {@link Expr} representing a numeric result from raising {@code numericField} to
   *     the power of {@code exponent}.
   */
  @BetaApi
  public static Expr pow(String numericField, Number exponent) {
    return pow(field(numericField), exponent);
  }

  /**
   * Creates an expression that returns the {@code numericExpr} raised to the power of the {@code
   * exponent}. Returns infinity on overflow and zero on underflow.
   *
   * @param numericExpr An expression that returns number when evaluated.
   * @param exponent The numeric power to raise the {@code numericExpr}.
   * @return A new {@link Expr} representing a numeric result from raising {@code numericExpr} to
   *     the power of {@code exponent}.
   */
  @BetaApi
  public static Expr pow(Expr numericExpr, Expr exponent) {
    return new FunctionExpr("pow", ImmutableList.of(numericExpr, exponent));
  }

  /**
   * Creates an expression that returns the {@code numericField} raised to the power of the {@code
   * exponent}. Returns infinity on overflow and zero on underflow.
   *
   * @param numericField Name of field that returns number when evaluated.
   * @param exponent The numeric power to raise the {@code numericField}.
   * @return A new {@link Expr} representing a numeric result from raising {@code numericField} to
   *     the power of {@code exponent}.
   */
  @BetaApi
  public static Expr pow(String numericField, Expr exponent) {
    return pow(field(numericField), exponent);
  }

  /**
   * Creates an expression that returns the absolute value of {@code numericExpr}.
   *
   * @param numericExpr An expression that returns number when evaluated.
   * @return A new {@link Expr} representing the numeric result of the absolute value operation.
   */
  @BetaApi
  public static Expr abs(Expr numericExpr) {
    return new FunctionExpr("abs", ImmutableList.of(numericExpr));
  }

  /**
   * Creates an expression that returns the absolute value of {@code numericField}.
   *
   * @param numericField Name of field that returns number when evaluated.
   * @return A new {@link Expr} representing the numeric result of the absolute value operation.
   */
  @BetaApi
  public static Expr abs(String numericField) {
    return abs(field(numericField));
  }

  /**
   * Creates an expression that returns Euler's number e raised to the power of {@code numericExpr}.
   *
   * @param numericExpr An expression that returns number when evaluated.
   * @return A new {@link Expr} representing the numeric result of the exponentiation.
   */
  @BetaApi
  public static Expr exp(Expr numericExpr) {
    return new FunctionExpr("exp", ImmutableList.of(numericExpr));
  }

  /**
   * Creates an expression that returns Euler's number e raised to the power of {@code
   * numericField}.
   *
   * @param numericField Name of field that returns number when evaluated.
   * @return A new {@link Expr} representing the numeric result of the exponentiation.
   */
  @BetaApi
  public static Expr exp(String numericField) {
    return exp(field(numericField));
  }

  /**
   * Creates an expression that returns the natural logarithm (base e) of {@code numericExpr}.
   *
   * @param numericExpr An expression that returns number when evaluated.
   * @return A new {@link Expr} representing the numeric result of the natural logarithm.
   */
  @BetaApi
  public static Expr ln(Expr numericExpr) {
    return new FunctionExpr("ln", ImmutableList.of(numericExpr));
  }

  /**
   * Creates an expression that returns the natural logarithm (base e) of {@code numericField}.
   *
   * @param numericField Name of field that returns number when evaluated.
   * @return A new {@link Expr} representing the numeric result of the natural logarithm.
   */
  @BetaApi
  public static Expr ln(String numericField) {
    return ln(field(numericField));
  }

  /**
   * Creates an expression that returns the logarithm of {@code numericExpr} with a given {@code
   * base}.
   *
   * @param numericExpr An expression that returns number when evaluated.
   * @param base The base of the logarithm.
   * @return A new {@link Expr} representing a numeric result from the logarithm of {@code
   *     numericExpr} with a given {@code base}.
   */
  @BetaApi
  public static Expr log(Expr numericExpr, Number base) {
    return new FunctionExpr("log", ImmutableList.of(numericExpr, constant(base)));
  }

  /**
   * Creates an expression that returns the logarithm of {@code numericField} with a given {@code
   * base}.
   *
   * @param numericField Name of field that returns number when evaluated.
   * @param base The base of the logarithm.
   * @return A new {@link Expr} representing a numeric result from the logarithm of {@code
   *     numericField} with a given {@code base}.
   */
  @BetaApi
  public static Expr log(String numericField, Number base) {
    return log(field(numericField), base);
  }

  /**
   * Creates an expression that returns the logarithm of {@code numericExpr} with a given {@code
   * base}.
   *
   * @param numericExpr An expression that returns number when evaluated.
   * @param base The base of the logarithm.
   * @return A new {@link Expr} representing a numeric result from the logarithm of {@code
   *     numericExpr} with a given {@code base}.
   */
  @BetaApi
  public static Expr log(Expr numericExpr, Expr base) {
    return new FunctionExpr("log", ImmutableList.of(numericExpr, base));
  }

  /**
   * Creates an expression that returns the logarithm of {@code numericField} with a given {@code
   * base}.
   *
   * @param numericField Name of field that returns number when evaluated.
   * @param base The base of the logarithm.
   * @return A new {@link Expr} representing a numeric result from the logarithm of {@code
   *     numericField} with a given {@code base}.
   */
  @BetaApi
  public static Expr log(String numericField, Expr base) {
    return log(field(numericField), base);
  }

  /**
   * Creates an expression that returns the base 10 logarithm of {@code numericExpr}.
   *
   * @param numericExpr An expression that returns number when evaluated.
   * @return A new {@link Expr} representing the numeric result of the base 10 logarithm.
   */
  @BetaApi
  public static Expr log10(Expr numericExpr) {
    return new FunctionExpr("log10", ImmutableList.of(numericExpr));
  }

  /**
   * Creates an expression that returns the base 10 logarithm of {@code numericField}.
   *
   * @param numericField Name of field that returns number when evaluated.
   * @return A new {@link Expr} representing the numeric result of the base 10 logarithm.
   */
  @BetaApi
  public static Expr log10(String numericField) {
    return log10(field(numericField));
  }

  /**
   * Creates an expression that returns the square root of {@code numericExpr}.
   *
   * @param numericExpr An expression that returns number when evaluated.
   * @return A new {@link Expr} representing the numeric result of the square root operation.
   */
  @BetaApi
  public static Expr sqrt(Expr numericExpr) {
    return new FunctionExpr("sqrt", ImmutableList.of(numericExpr));
  }

  /**
   * Creates an expression that returns the square root of {@code numericField}.
   *
   * @param numericField Name of field that returns number when evaluated.
   * @return A new {@link Expr} representing the numeric result of the square root operation.
   */
  @BetaApi
  public static Expr sqrt(String numericField) {
    return sqrt(field(numericField));
  }

  // String Operations
  /**
   * Creates an expression that return a pseudo-random number of type double in the range of [0, 1),
   * inclusive of 0 and exclusive of 1.
   *
   * @return A new {@link Expr} representing the random number operation.
   */
  @BetaApi
  public static Expr rand() {
    return new FunctionExpr("rand", ImmutableList.of());
  }

  // Logical/Comparison Operations
  /**
   * Creates an expression that checks if the results of {@code expr} is NOT 'NaN' (Not a Number).
   *
   * @param expr The expression to check.
   * @return A new {@link BooleanExpr} representing the isNotNan operation.
   */
  @BetaApi
  public static BooleanExpr isNotNan(Expr expr) {
    return new BooleanExpr("is_not_nan", expr);
  }

  /**
   * Creates an expression that checks if the results of this expression is NOT 'NaN' (Not a
   * Number).
   *
   * @param fieldName The field to check.
   * @return A new {@link BooleanExpr} representing the isNotNan operation.
   */
  @BetaApi
  public static BooleanExpr isNotNan(String fieldName) {
    return isNotNan(field(fieldName));
  }

  /**
   * Creates an expression that returns the largest value between multiple input expressions or
   * literal values. Based on Firestore's value type ordering.
   *
   * @param expr The first operand expression.
   * @param others Optional additional expressions or literals.
   * @return A new {@link Expr} representing the logical maximum operation.
   */
  @BetaApi
  public static Expr logicalMaximum(Expr expr, Object... others) {
    ImmutableList.Builder<Expr> builder = ImmutableList.builder();
    builder.add(expr);
    builder.addAll(toArrayOfExprOrConstant(others));
    return new FunctionExpr("logical_max", builder.build());
  }

  /**
   * Creates an expression that returns the largest value between multiple input expressions or
   * literal values. Based on Firestore's value type ordering.
   *
   * @param fieldName The first operand field name.
   * @param others Optional additional expressions or literals.
   * @return A new {@link Expr} representing the logical maximum operation.
   */
  @BetaApi
  public static Expr logicalMaximum(String fieldName, Object... others) {
    return logicalMaximum(field(fieldName), others);
  }

  /**
   * Creates an expression that returns the smallest value between multiple input expressions or
   * literal values. Based on Firestore's value type ordering.
   *
   * @param expr The first operand expression.
   * @param others Optional additional expressions or literals.
   * @return A new {@link Expr} representing the logical minimum operation.
   */
  @BetaApi
  public static Expr logicalMinimum(Expr expr, Object... others) {
    ImmutableList.Builder<Expr> builder = ImmutableList.builder();
    builder.add(expr);
    builder.addAll(toArrayOfExprOrConstant(others));
    return new FunctionExpr("logical_min", builder.build());
  }

  /**
   * Creates an expression that returns the smallest value between multiple input expressions or
   * literal values. Based on Firestore's value type ordering.
   *
   * @param fieldName The first operand field name.
   * @param others Optional additional expressions or literals.
   * @return A new {@link Expr} representing the logical minimum operation.
   */
  @BetaApi
  public static Expr logicalMinimum(String fieldName, Object... others) {
    return logicalMinimum(field(fieldName), others);
  }

  /**
   * Creates an expression that checks if the results of this expression is NOT 'NaN' (Not a
   * Number).
   *
   * @return A new {@link BooleanExpr} representing the isNotNan operation.
   */
  @BetaApi
  public final BooleanExpr isNotNan() {
    return isNotNan(this);
  }

  /**
   * Creates an expression that returns the largest value between multiple input expressions or
   * literal values. Based on Firestore's value type ordering.
   *
   * @param others Optional additional expressions or literals.
   * @return A new {@link Expr} representing the logical maximum operation.
   */
  @BetaApi
  public final Expr logicalMaximum(Object... others) {
    return logicalMaximum(this, others);
  }

  /**
   * Creates an expression that returns the smallest value between multiple input expressions or
   * literal values. Based on Firestore's value type ordering.
   *
   * @param others Optional additional expressions or literals.
   * @return A new {@link Expr} representing the logical minimum operation.
   */
  @BetaApi
  public final Expr logicalMinimum(Object... others) {
    return logicalMinimum(this, others);
  }

  /**
   * Creates an expression that rounds this numeric expression to nearest integer.
   *
   * <p>Rounds away from zero in halfway cases.
   *
   * @return A new {@link Expr} representing an integer result from the round operation.
   */
  @BetaApi
  public final Expr round() {
    return round(this);
  }

  /**
   * Creates an expression that rounds off this numeric expression to {@code decimalPlace} decimal
   * places if {@code decimalPlace} is positive, rounds off digits to the left of the decimal point
   * if {@code decimalPlace} is negative. Rounds away from zero in halfway cases.
   *
   * @param decimalPlace The number of decimal places to round.
   * @return A new {@link Expr} representing the round operation.
   */
  @BetaApi
  public final Expr roundToPrecision(int decimalPlace) {
    return roundToPrecision(this, decimalPlace);
  }

  /**
   * Creates an expression that rounds off this numeric expression to {@code decimalPlace} decimal
   * places if {@code decimalPlace} is positive, rounds off digits to the left of the decimal point
   * if {@code decimalPlace} is negative. Rounds away from zero in halfway cases.
   *
   * @param decimalPlace The number of decimal places to round.
   * @return A new {@link Expr} representing the round operation.
   */
  @BetaApi
  public final Expr roundToPrecision(Expr decimalPlace) {
    return roundToPrecision(this, decimalPlace);
  }

  /**
   * Creates an expression that returns the smallest integer that isn't less than this numeric
   * expression.
   *
   * @return A new {@link Expr} representing an integer result from the ceil operation.
   */
  @BetaApi
  public final Expr ceil() {
    return ceil(this);
  }

  /**
   * Creates an expression that returns the largest integer that isn't less than this numeric
   * expression.
   *
   * @return A new {@link Expr} representing an integer result from the floor operation.
   */
  @BetaApi
  public final Expr floor() {
    return floor(this);
  }

  /**
   * Creates an expression that returns this numeric expression raised to the power of the {@code
   * exponent}. Returns infinity on overflow and zero on underflow.
   *
   * @param exponent The numeric power to raise this numeric expression.
   * @return A new {@link Expr} representing a numeric result from raising this numeric expression
   *     to the power of {@code exponent}.
   */
  @BetaApi
  public final Expr pow(Number exponent) {
    return pow(this, exponent);
  }

  /**
   * Creates an expression that returns this numeric expression raised to the power of the {@code
   * exponent}. Returns infinity on overflow and zero on underflow.
   *
   * @param exponent The numeric power to raise this numeric expression.
   * @return A new {@link Expr} representing a numeric result from raising this numeric expression
   *     to the power of {@code exponent}.
   */
  @BetaApi
  public final Expr pow(Expr exponent) {
    return pow(this, exponent);
  }

  /**
   * Creates an expression that returns the absolute value of this numeric expression.
   *
   * @return A new {@link Expr} representing the numeric result of the absolute value operation.
   */
  @BetaApi
  public final Expr abs() {
    return abs(this);
  }

  /**
   * Creates an expression that returns Euler's number e raised to the power of this numeric
   * expression.
   *
   * @return A new {@link Expr} representing the numeric result of the exponentiation.
   */
  @BetaApi
  public final Expr exp() {
    return exp(this);
  }

  /**
   * Creates an expression that returns the natural logarithm (base e) of this numeric expression.
   *
   * @return A new {@link Expr} representing the numeric result of the natural logarithm.
   */
  @BetaApi
  public final Expr ln() {
    return ln(this);
  }

  /**
   * Creates an expression that returns the logarithm of this numeric expression with a given {@code
   * base}.
   *
   * @param base The base of the logarithm.
   * @return A new {@link Expr} representing a numeric result from the logarithm of this numeric
   *     expression with a given {@code base}.
   */
  @BetaApi
  public final Expr log(Number base) {
    return log(this, base);
  }

  /**
   * Creates an expression that returns the logarithm of this numeric expression with a given {@code
   * base}.
   *
   * @param base The base of the logarithm.
   * @return A new {@link Expr} representing a numeric result from the logarithm of this numeric
   *     expression with a given {@code base}.
   */
  @BetaApi
  public final Expr log(Expr base) {
    return log(this, base);
  }

  /**
   * Creates an expression that returns the base 10 logarithm of this numeric expression.
   *
   * @return A new {@link Expr} representing the numeric result of the base 10 logarithm.
   */
  @BetaApi
  public final Expr log10() {
    return log10(this);
  }

  /**
   * Creates an expression that returns the square root of this numeric expression.
   *
   * @return A new {@link Expr} representing the numeric result of the square root operation.
   */
  @BetaApi
  public final Expr sqrt() {
    return sqrt(this);
  }

  // Fluent API
  /**
   * Creates an expression that adds this numeric expression to another numeric expression.
   *
   * @param other Numeric expression to add.
   * @return A new {@link Expr} representing the addition operation.
   */
  @BetaApi
  public final Expr add(Object other) {
    return add(this, toExprOrConstant(other));
  }

  /**
   * Creates an expression that subtracts a numeric expressions from this numeric expression.
   *
   * @param other Constant to subtract.
   * @return A new {@link Expr} representing the subtract operation.
   */
  @BetaApi
  public final Expr subtract(Object other) {
    return subtract(this, toExprOrConstant(other));
  }

  /**
   * Creates an expression that multiplies this numeric expression with another numeric expression.
   *
   * @param other Numeric expression to multiply.
   * @return A new {@link Expr} representing the multiplication operation.
   */
  @BetaApi
  public final Expr multiply(Object other) {
    return multiply(this, toExprOrConstant(other));
  }

  /**
   * Creates an expression that divides this numeric expression by another numeric expression.
   *
   * @param other Numeric expression to divide this numeric expression by.
   * @return A new {@link Expr} representing the division operation.
   */
  @BetaApi
  public final Expr divide(Object other) {
    return divide(this, toExprOrConstant(other));
  }

  /**
   * Creates an expression that calculates the modulo (remainder) of dividing this numeric
   * expressions by another numeric expression.
   *
   * @param other The numeric expression to divide this expression by.
   * @return A new {@link Expr} representing the modulo operation.
   */
  @BetaApi
  public final Expr mod(Object other) {
    return mod(this, toExprOrConstant(other));
  }

  /**
   * Creates an expression that checks if this expression is equal to a {@code value}.
   *
   * @param other The value to compare to.
   * @return A new {@link BooleanExpr} representing the equality comparison.
   */
  @BetaApi
  public final BooleanExpr eq(Object other) {
    return eq(this, toExprOrConstant(other));
  }

  /**
   * Creates an expression that checks if this expression is not equal to a {@code value}.
   *
   * @param other The value to compare to.
   * @return A new {@link BooleanExpr} representing the inequality comparison.
   */
  @BetaApi
  public final BooleanExpr neq(Object other) {
    return neq(this, toExprOrConstant(other));
  }

  /**
   * Creates an expression that checks if this expression is greater than a {@code value}.
   *
   * @param other The value to compare to.
   * @return A new {@link BooleanExpr} representing the greater than comparison.
   */
  @BetaApi
  public final BooleanExpr gt(Object other) {
    return gt(this, toExprOrConstant(other));
  }

  /**
   * Creates an expression that checks if this expression is greater than or equal to a {@code
   * value}.
   *
   * @param other The value to compare to.
   * @return A new {@link BooleanExpr} representing the greater than or equal to comparison.
   */
  @BetaApi
  public final BooleanExpr gte(Object other) {
    return gte(this, toExprOrConstant(other));
  }

  /**
   * Creates an expression that checks if this expression is less than a value.
   *
   * @param other The value to compare to.
   * @return A new {@link BooleanExpr} representing the less than comparison.
   */
  @BetaApi
  public final BooleanExpr lt(Object other) {
    return lt(this, toExprOrConstant(other));
  }

  /**
   * Creates an expression that checks if this expression is less than or equal to a {@code value}.
   *
   * @param other The value to compare to.
   * @return A new {@link BooleanExpr} representing the less than or equal to comparison.
   */
  @BetaApi
  public final BooleanExpr lte(Object other) {
    return lte(this, toExprOrConstant(other));
  }

  /**
   * Creates an expression that checks if this expression, when evaluated, is equal to any of the
   * provided {@code values}.
   *
   * @param other The values to check against.
   * @return A new {@link BooleanExpr} representing the 'IN' comparison.
   */
  @BetaApi
  public final BooleanExpr eqAny(List<Object> other) {
    return eqAny(this, other);
  }

  /**
   * Creates an expression that checks if this expression, when evaluated, is not equal to all the
   * provided {@code values}.
   *
   * @param other The values to check against.
   * @return A new {@link BooleanExpr} representing the 'NOT IN' comparison.
   */
  @BetaApi
  public final BooleanExpr notEqAny(List<Object> other) {
    return notEqAny(this, other);
  }

  /**
   * Creates an expression that calculates the character length of this string expression in UTF8.
   *
   * @return A new {@link Expr} representing the charLength operation.
   */
  @BetaApi
  public final Expr charLength() {
    return charLength(this);
  }

  /**
   * Creates an expression that calculates the length of a string in UTF-8 bytes, or just the length
   * of a Blob.
   *
   * @return A new {@link Expr} representing the length of the string in bytes.
   */
  @BetaApi
  public final Expr byteLength() {
    return byteLength(this);
  }

  /**
   * Creates an expression that calculates the length of the expression if it is a string, array,
   * map, or Blob.
   *
   * @return A new {@link Expr} representing the length of the expression.
   */
  @BetaApi
  public final Expr length() {
    return length(this);
  }

  /**
   * Creates an expression that performs a case-sensitive wildcard string comparison.
   *
   * @param pattern The pattern to search for. You can use "%" as a wildcard character.
   * @return A new {@link BooleanExpr} representing the like operation.
   */
  @BetaApi
  public final BooleanExpr like(Object pattern) {
    return like(this, toExprOrConstant(pattern));
  }

  /**
   * Creates an expression that checks if this string expression contains a specified regular
   * expression as a substring.
   *
   * @param pattern The regular expression to use for the search.
   * @return A new {@link BooleanExpr} representing the contains regular expression comparison.
   */
  @BetaApi
  public final BooleanExpr regexContains(Object pattern) {
    return regexContains(this, toExprOrConstant(pattern));
  }

  /**
   * Creates an expression that checks if this string expression matches a specified regular
   * expression.
   *
   * @param pattern The regular expression to use for the match.
   * @return A new {@link BooleanExpr} representing the regular expression match comparison.
   */
  @BetaApi
  public final BooleanExpr regexMatch(Object pattern) {
    return regexMatch(this, toExprOrConstant(pattern));
  }

  /**
   * Creates an expression that reverses this string expression.
   *
   * @return A new {@link Expr} representing the reversed string.
   */
  @BetaApi
  public final Expr strReverse() {
    return strReverse(this);
  }

  /**
   * Creates an expression that checks if this string expression contains a specified substring.
   *
   * @param substring The expression representing the substring to search for.
   * @return A new {@link BooleanExpr} representing the contains comparison.
   */
  @BetaApi
  public final BooleanExpr strContains(Object substring) {
    return strContains(this, toExprOrConstant(substring));
  }

  /**
   * Creates an expression that checks if this string expression starts with a given {@code prefix}.
   *
   * @param prefix The prefix string expression to check for.
   * @return A new {@link Expr} representing the the 'starts with' comparison.
   */
  @BetaApi
  public final BooleanExpr startsWith(Object prefix) {
    return startsWith(this, toExprOrConstant(prefix));
  }

  /**
   * Creates an expression that checks if this string expression ends with a given {@code suffix}.
   *
   * @param suffix The suffix string expression to check for.
   * @return A new {@link Expr} representing the 'ends with' comparison.
   */
  @BetaApi
  public final BooleanExpr endsWith(Object suffix) {
    return endsWith(this, toExprOrConstant(suffix));
  }

  /**
   * Creates an expression that returns a substring of the given string.
   *
   * @param index The starting index of the substring.
   * @param length The length of the substring.
   * @return A new {@link Expr} representing the substring.
   */
  @BetaApi
  public final Expr substring(Object index, Object length) {
    return substring(this, toExprOrConstant(index), toExprOrConstant(length));
  }

  /**
   * Creates an expression that converts this string expression to lowercase.
   *
   * @return A new {@link Expr} representing the lowercase string.
   */
  @BetaApi
  public final Expr toLower() {
    return toLower(this);
  }

  /**
   * Creates an expression that converts this string expression to uppercase.
   *
   * @return A new {@link Expr} representing the lowercase string.
   */
  @BetaApi
  public final Expr toUpper() {
    return toUpper(this);
  }

  /**
   * Creates an expression that removes leading and trailing whitespace from this string expression.
   *
   * @return A new {@link Expr} representing the trimmed string.
   */
  @BetaApi
  public final Expr trim() {
    return trim(this);
  }

  /**
   * Creates an expression that concatenates string expressions and string constants together.
   *
   * @param others The string expressions or string constants to concatenate.
   * @return A new {@link Expr} representing the concatenated string.
   */
  @BetaApi
  public final Expr strConcat(Object... others) {
    return strConcat(this, others);
  }

  /**
   * Accesses a map (object) value using the provided {@code key}.
   *
   * @param key The key to access in the map.
   * @return A new {@link Expr} representing the value associated with the given key in the map.
   */
  @BetaApi
  public final Expr mapGet(Object key) {
    return mapGet(this, toExprOrConstant(key));
  }

  /**
   * Creates an expression that returns true if yhe result of this expression is absent. Otherwise,
   * returns false even if the value is null.
   *
   * @return A new {@link BooleanExpr} representing the isAbsent operation.
   */
  @BetaApi
  public final BooleanExpr isAbsent() {
    return isAbsent(this);
  }

  /**
   * Creates an expression that checks if this expression evaluates to 'NaN' (Not a Number).
   *
   * @return A new {@link BooleanExpr} representing the isNan operation.
   */
  @BetaApi
  public final BooleanExpr isNaN() {
    return isNaN(this);
  }

  /**
   * Creates an expression that checks if tbe result of this expression is null.
   *
   * @return A new {@link BooleanExpr} representing the isNull operation.
   */
  @BetaApi
  public final BooleanExpr isNull() {
    return isNull(this);
  }

  /**
   * Creates an expression that checks if tbe result of this expression is not null.
   *
   * @return A new {@link BooleanExpr} representing the isNotNull operation.
   */
  @BetaApi
  public final BooleanExpr isNotNull() {
    return isNotNull(this);
  }

  /**
   * Creates an aggregation that calculates the sum of this numeric expression across multiple stage
   * inputs.
   *
   * @return A new {@link AggregateFunction} representing the sum aggregation.
   */
  @BetaApi
  public final AggregateFunction sum() {
    return AggregateFunction.sum(this);
  }

  /**
   * Creates an aggregation that calculates the average (mean) of this numeric expression across
   * multiple stage inputs.
   *
   * @return A new {@link AggregateFunction} representing the average aggregation.
   */
  @BetaApi
  public final AggregateFunction avg() {
    return AggregateFunction.avg(this);
  }

  /**
   * Creates an aggregation that finds the minimum value of this expression across multiple stage
   * inputs.
   *
   * @return A new {@link AggregateFunction} representing the minimum aggregation.
   */
  @BetaApi
  public final AggregateFunction minimum() {
    return AggregateFunction.minimum(this);
  }

  /**
   * Creates an aggregation that finds the maximum value of this expression across multiple stage
   * inputs.
   *
   * @return A new {@link AggregateFunction} representing the maximum aggregation.
   */
  @BetaApi
  public final AggregateFunction maximum() {
    return AggregateFunction.maximum(this);
  }

  /**
   * Creates an aggregation that counts the number of stage inputs with valid evaluations of the
   * this expression.
   *
   * @return A new {@link AggregateFunction} representing the count aggregation.
   */
  @BetaApi
  public final AggregateFunction count() {
    return AggregateFunction.count(this);
  }

  /**
   * Creates an aggregation that counts the number of distinct values of this expression.
   *
   * @return A new {@link AggregateFunction} representing the count distinct aggregation.
   */
  @BetaApi
  public final AggregateFunction countDistinct() {
    return AggregateFunction.countDistinct(this);
  }

  /**
   * Create an {@link Ordering} that sorts documents in ascending order based on value of this
   * expression
   *
   * @return A new {@link Ordering} object with ascending sort by this expression.
   */
  @BetaApi
  public final Ordering ascending() {
    return Ordering.ascending(this);
  }

  /**
   * Create an {@link Ordering} that sorts documents in descending order based on value of this
   * expression
   *
   * @return A new {@link Ordering} object with descending sort by this expression.
   */
  @BetaApi
  public final Ordering descending() {
    return Ordering.descending(this);
  }

  /**
   * Assigns an alias to this expression.
   *
   * <p>Aliases are useful for renaming fields in the output of a stage or for giving meaningful
   * names to calculated values.
   *
   * @param alias The alias to assign to this expression.
   * @return A new {@link Selectable} (typically an {@link AliasedExpr}) that wraps this expression
   *     and associates it with the provided alias.
   */
  @BetaApi
  public Selectable as(String alias) {
    return new AliasedExpr<>(this, alias);
  }

  // Fluent API for new functions
  /**
   * Creates an expression that merges multiple maps into a single map. If multiple maps have the
   * same key, the later value is used.
   *
   * @param secondMap Map expression that will be merged.
   * @param otherMaps Additional maps to merge.
   * @return A new {@link Expr} representing the mapMerge operation.
   */
  @BetaApi
  public final Expr mapMerge(Expr secondMap, Expr... otherMaps) {
    return mapMerge(this, secondMap, otherMaps);
  }

  /**
   * Creates an expression that removes a key from this map expression.
   *
   * @param key The name of the key to remove from this map expression.
   * @return A new {@link Expr} that evaluates to a modified map.
   */
  @BetaApi
  public final Expr mapRemove(Expr key) {
    return mapRemove(this, key);
  }

  /**
   * Creates an expression that removes a key from this map expression.
   *
   * @param key The name of the key to remove from this map expression.
   * @return A new {@link Expr} that evaluates to a modified map.
   */
  @BetaApi
  public final Expr mapRemove(String key) {
    return mapRemove(this, key);
  }

  /**
   * Creates an expression that concatenates a field's array value with other arrays.
   *
   * @param otherArrays Optional additional array expressions or array literals to concatenate.
   * @return A new {@link Expr} representing the arrayConcat operation.
   */
  @BetaApi
  public final Expr arrayConcat(Object... otherArrays) {
    return arrayConcat(this, otherArrays);
  }

  /**
   * Reverses the order of elements in the array.
   *
   * @return A new {@link Expr} representing the arrayReverse operation.
   */
  @BetaApi
  public final Expr arrayReverse() {
    return arrayReverse(this);
  }

  /**
   * Creates an expression that checks if array contains a specific {@code element}.
   *
   * @param element The element to search for in the array.
   * @return A new {@link BooleanExpr} representing the arrayContains operation.
   */
  @BetaApi
  public final BooleanExpr arrayContains(Object element) {
    return arrayContains(this, element);
  }

  /**
   * Creates an expression that checks if array contains all the specified {@code values}.
   *
   * @param values The elements to check for in the array.
   * @return A new {@link BooleanExpr} representing the arrayContainsAll operation.
   */
  @BetaApi
  public final BooleanExpr arrayContainsAll(List<Object> values) {
    return arrayContainsAll(this, values);
  }

  /**
   * Creates an expression that checks if array contains all elements of {@code arrayExpression}.
   *
   * @param arrayExpression The elements to check for in the array.
   * @return A new {@link BooleanExpr} representing the arrayContainsAll operation.
   */
  @BetaApi
  public final BooleanExpr arrayContainsAll(Expr arrayExpression) {
    return arrayContainsAll(this, arrayExpression);
  }

  /**
   * Creates an expression that checks if array contains any of the specified {@code values}.
   *
   * @param values The elements to check for in the array.
   * @return A new {@link BooleanExpr} representing the arrayContainsAny operation.
   */
  @BetaApi
  public final BooleanExpr arrayContainsAny(List<Object> values) {
    return arrayContainsAny(this, values);
  }

  /**
   * Creates an expression that checks if array contains any elements of {@code arrayExpression}.
   *
   * @param arrayExpression The elements to check for in the array.
   * @return A new {@link BooleanExpr} representing the arrayContainsAny operation.
   */
  @BetaApi
  public final BooleanExpr arrayContainsAny(Expr arrayExpression) {
    return arrayContainsAny(this, arrayExpression);
  }

  /**
   * Creates an expression that calculates the length of an array expression.
   *
   * @return A new {@link Expr} representing the length of the array.
   */
  @BetaApi
  public final Expr arrayLength() {
    return arrayLength(this);
  }

  /**
   * Creates an expression that indexes into an array from the beginning or end and return the
   * element. If the offset exceeds the array length, an error is returned. A negative offset,
   * starts from the end.
   *
   * @param offset An Expr evaluating to the index of the element to return.
   * @return A new {@link Expr} representing the arrayGet operation.
   */
  @BetaApi
  public final Expr arrayGet(Expr offset) {
    return arrayGet(this, offset);
  }

  /**
   * Creates an expression that indexes into an array from the beginning or end and return the
   * element. If the offset exceeds the array length, an error is returned. A negative offset,
   * starts from the end.
   *
   * @param offset An Expr evaluating to the index of the element to return.
   * @return A new {@link Expr} representing the arrayOffset operation.
   */
  @BetaApi
  public final Expr arrayGet(int offset) {
    return arrayGet(this, offset);
  }

  /**
   * Calculates the Cosine distance between this and another vector expressions.
   *
   * @param vector The other vector (represented as an Expr) to compare against.
   * @return A new {@link Expr} representing the cosine distance between the two vectors.
   */
  @BetaApi
  public final Expr cosineDistance(Expr vector) {
    return cosineDistance(this, vector);
  }

  /**
   * Calculates the Cosine distance between this vector expression and a vector literal.
   *
   * @param vector The other vector (as an array of doubles) to compare against.
   * @return A new {@link Expr} representing the cosine distance between the two vectors.
   */
  @BetaApi
  public final Expr cosineDistance(double[] vector) {
    return cosineDistance(this, vector);
  }

  /**
   * Calculates the dot product distance between this and another vector expression.
   *
   * @param vector The other vector (represented as an Expr) to compare against.
   * @return A new {@link Expr} representing the dot product distance between the two vectors.
   */
  @BetaApi
  public final Expr dotProduct(Expr vector) {
    return dotProduct(this, vector);
  }

  /**
   * Calculates the dot product distance between this vector expression and a vector literal.
   *
   * @param vector The other vector (as an array of doubles) to compare against.
   * @return A new {@link Expr} representing the dot product distance between the two vectors.
   */
  @BetaApi
  public final Expr dotProduct(double[] vector) {
    return dotProduct(this, vector);
  }

  /**
   * Calculates the Euclidean distance between this and another vector expression.
   *
   * @param vector The other vector (represented as an Expr) to compare against.
   * @return A new {@link Expr} representing the Euclidean distance between the two vectors.
   */
  @BetaApi
  public final Expr euclideanDistance(Expr vector) {
    return euclideanDistance(this, vector);
  }

  /**
   * Calculates the Euclidean distance between this vector expression and a vector literal.
   *
   * @param vector The other vector (as an array of doubles) to compare against.
   * @return A new {@link Expr} representing the Euclidean distance between the two vectors.
   */
  @BetaApi
  public final Expr euclideanDistance(double[] vector) {
    return euclideanDistance(this, vector);
  }

  /**
   * Creates an expression that calculates the length (dimension) of a Firestore Vector.
   *
   * @return A new {@link Expr} representing the length (dimension) of the vector.
   */
  @BetaApi
  public final Expr vectorLength() {
    return vectorLength(this);
  }

  /**
   * Creates an expression that interprets this expression as the number of microseconds since the
   * Unix epoch (1970-01-01 00:00:00 UTC) and returns a timestamp.
   *
   * @return A new {@link Expr} representing the timestamp.
   */
  @BetaApi
  public final Expr unixMicrosToTimestamp() {
    return unixMicrosToTimestamp(this);
  }

  /**
   * Creates an expression that converts this timestamp expression to the number of microseconds
   * since the Unix epoch (1970-01-01 00:00:00 UTC).
   *
   * @return A new {@link Expr} representing the number of microseconds since epoch.
   */
  @BetaApi
  public final Expr timestampToUnixMicros() {
    return timestampToUnixMicros(this);
  }

  /**
   * Creates an expression that interprets this expression as the number of milliseconds since the
   * Unix epoch (1970-01-01 00:00:00 UTC) and returns a timestamp.
   *
   * @return A new {@link Expr} representing the timestamp.
   */
  @BetaApi
  public final Expr unixMillisToTimestamp() {
    return unixMillisToTimestamp(this);
  }

  /**
   * Creates an expression that converts this timestamp expression to the number of milliseconds
   * since the Unix epoch (1970-01-01 00:00:00 UTC).
   *
   * @return A new {@link Expr} representing the number of milliseconds since epoch.
   */
  @BetaApi
  public final Expr timestampToUnixMillis() {
    return timestampToUnixMillis(this);
  }

  /**
   * Creates an expression that interprets this expression as the number of seconds since the Unix
   * epoch (1970-01-01 00:00:00 UTC) and returns a timestamp.
   *
   * @return A new {@link Expr} representing the timestamp.
   */
  @BetaApi
  public final Expr unixSecondsToTimestamp() {
    return unixSecondsToTimestamp(this);
  }

  /**
   * Creates an expression that converts this timestamp expression to the number of seconds since
   * the Unix epoch (1970-01-01 00:00:00 UTC).
   *
   * @return A new {@link Expr} representing the number of seconds since epoch.
   */
  @BetaApi
  public final Expr timestampToUnixSeconds() {
    return timestampToUnixSeconds(this);
  }

  /**
   * Creates an expression that adds a specified amount of time to this timestamp expression.
   *
   * @param unit The expression representing the unit of time to add. Valid units include
   *     "microsecond", "millisecond", "second", "minute", "hour" and "day".
   * @param amount The expression representing the amount of time to add.
   * @return A new {@link Expr} representing the resulting timestamp.
   */
  @BetaApi
  public final Expr timestampAdd(Expr unit, Expr amount) {
    return timestampAdd(this, unit, amount);
  }

  /**
   * Creates an expression that adds a specified amount of time to this timestamp expression.
   *
   * @param unit The unit of time to add. Valid units include "microsecond", "millisecond",
   *     "second", "minute", "hour" and "day".
   * @param amount The amount of time to add.
   * @return A new {@link Expr} representing the resulting timestamp.
   */
  @BetaApi
  public final Expr timestampAdd(String unit, long amount) {
    return timestampAdd(this, unit, amount);
  }

  /**
   * Creates an expression that subtracts a specified amount of time to this timestamp expression.
   *
   * @param unit The expression representing the unit of time to subtract. Valid units include
   *     "microsecond", "millisecond", "second", "minute", "hour" and "day".
   * @param amount The expression representing the amount of time to subtract.
   * @return A new {@link Expr} representing the resulting timestamp.
   */
  @BetaApi
  public final Expr timestampSub(Expr unit, Expr amount) {
    return timestampSub(this, unit, amount);
  }

  /**
   * Creates an expression that subtracts a specified amount of time to this timestamp expression.
   *
   * @param unit The unit of time to subtract. Valid units include "microsecond", "millisecond",
   *     "second", "minute", "hour" and "day".
   * @param amount The amount of time to subtract.
   * @return A new {@link Expr} representing the resulting timestamp.
   */
  @BetaApi
  public final Expr timestampSub(String unit, long amount) {
    return timestampSub(this, unit, amount);
  }

  /**
   * Creates an expression that checks if this expression evaluates to a name of the field that
   * exists.
   *
   * @return A new {@link Expr} representing the exists check.
   */
  @BetaApi
  public final BooleanExpr exists() {
    return exists(this);
  }

  /**
   * Creates a conditional expression that evaluates to a {@code thenExpr} expression if this
   * condition is true or an {@code elseExpr} expression if the condition is false.
   *
   * @param thenExpr The expression to evaluate if the condition is true.
   * @param elseExpr The expression to evaluate if the condition is false.
   * @return A new {@link Expr} representing the conditional operation.
   */
  @BetaApi
  public final Expr cond(Expr thenExpr, Expr elseExpr) {
    return cond((BooleanExpr) this, thenExpr, elseExpr);
  }

  /**
   * Creates a conditional expression that evaluates to a {@code thenValue} if this condition is
   * true or an {@code elseValue} if the condition is false.
   *
   * @param thenValue Value if the condition is true.
   * @param elseValue Value if the condition is false.
   * @return A new {@link Expr} representing the conditional operation.
   */
  @BetaApi
  public final Expr cond(Object thenValue, Object elseValue) {
    return cond((BooleanExpr) this, thenValue, elseValue);
  }

  /**
   * Creates an expression that returns the {@code catchExpr} argument if there is an error, else
   * return the result of this expression.
   *
   * @param catchExpr The catch expression that will be evaluated and returned if the this
   *     expression produces an error.
   * @return A new {@link Expr} representing the ifError operation.
   */
  @BetaApi
  public final Expr ifError(Expr catchExpr) {
    return ifError(this, catchExpr);
  }

  /**
   * Creates an expression that returns the {@code catchValue} argument if there is an error, else
   * return the result of this expression.
   *
   * @param catchValue The value that will be returned if this expression produces an error.
   * @return A new {@link Expr} representing the ifError operation.
   */
  @BetaApi
  public final Expr ifError(Object catchValue) {
    return ifError(this, catchValue);
  }

  /**
   * Creates an expression that checks if this expression produces an error.
   *
   * @return A new {@link BooleanExpr} representing the `isError` check.
   */
  @BetaApi
  public final BooleanExpr isError() {
    return isError(this);
  }

  /**
   * Creates an expression that returns the document ID from this path expression.
   *
   * @return A new {@link Expr} representing the documentId operation.
   */
  @BetaApi
  public final Expr documentId() {
    return documentId(this);
  }

  /**
   * Creates an expression that returns the collection ID from this path expression.
   *
   * @return A new {@link Expr} representing the collectionId operation.
   */
  @BetaApi
  public final Expr collectionId() {
    return collectionId(this);
  }
}
