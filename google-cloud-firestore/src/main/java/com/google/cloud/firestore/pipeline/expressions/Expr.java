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

import static com.google.cloud.firestore.pipeline.expressions.FunctionUtils.toExprList;

import com.google.api.core.BetaApi;
import java.util.Arrays;
import java.util.List;

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
 *   <li>**Aggregations:** Calculate aggregate values (e.g., sum, average) over a set of documents.
 * </ul>
 *
 * <p>The `Expr` interface provides a fluent API for building expressions. You can chain together
 * method calls to create complex expressions.
 */
@BetaApi
public interface Expr {

  // Arithmetic Operators

  /**
   * Creates an expression that adds this expression to another expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Add the value of the 'quantity' field and the 'reserve' field.
   * Field.of("quantity").add(Field.of("reserve"));
   * }</pre>
   *
   * @param other The expression to add to this expression.
   * @return A new {@code Expr} representing the addition operation.
   */
  @BetaApi
  default Add add(Expr other) {
    return new Add(this, other);
  }

  /**
   * Creates an expression that adds this expression to a constant value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Add 5 to the value of the 'age' field
   * Field.of("age").add(5);
   * }</pre>
   *
   * @param other The constant value to add.
   * @return A new {@code Expr} representing the addition operation.
   */
  @BetaApi
  default Add add(Object other) {
    return new Add(this, Constant.of(other));
  }

  /**
   * Creates an expression that subtracts another expression from this expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Subtract the 'discount' field from the 'price' field
   * Field.of("price").subtract(Field.of("discount"));
   * }</pre>
   *
   * @param other The expression to subtract from this expression.
   * @return A new {@code Expr} representing the subtraction operation.
   */
  @BetaApi
  default Subtract subtract(Expr other) {
    return new Subtract(this, other);
  }

  /**
   * Creates an expression that subtracts a constant value from this expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Subtract 20 from the value of the 'total' field
   * Field.of("total").subtract(20);
   * }</pre>
   *
   * @param other The constant value to subtract.
   * @return A new {@code Expr} representing the subtraction operation.
   */
  @BetaApi
  default Subtract subtract(Object other) {
    return new Subtract(this, Constant.of(other));
  }

  /**
   * Creates an expression that multiplies this expression by another expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Multiply the 'quantity' field by the 'price' field
   * Field.of("quantity").multiply(Field.of("price"));
   * }</pre>
   *
   * @param other The expression to multiply by.
   * @return A new {@code Expr} representing the multiplication operation.
   */
  @BetaApi
  default Multiply multiply(Expr other) {
    return new Multiply(this, other);
  }

  /**
   * Creates an expression that multiplies this expression by a constant value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Multiply the 'value' field by 2
   * Field.of("value").multiply(2);
   * }</pre>
   *
   * @param other The constant value to multiply by.
   * @return A new {@code Expr} representing the multiplication operation.
   */
  @BetaApi
  default Multiply multiply(Object other) {
    return new Multiply(this, Constant.of(other));
  }

  /**
   * Creates an expression that divides this expression by another expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Divide the 'total' field by the 'count' field
   * Field.of("total").divide(Field.of("count"));
   * }</pre>
   *
   * @param other The expression to divide by.
   * @return A new {@code Expr} representing the division operation.
   */
  @BetaApi
  default Divide divide(Expr other) {
    return new Divide(this, other);
  }

  /**
   * Creates an expression that divides this expression by a constant value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Divide the 'value' field by 10
   * Field.of("value").divide(10);
   * }</pre>
   *
   * @param other The constant value to divide by.
   * @return A new {@code Expr} representing the division operation.
   */
  @BetaApi
  default Divide divide(Object other) {
    return new Divide(this, Constant.of(other));
  }

  /**
   * Creates an expression that calculates the modulo (remainder) to another expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the remainder of dividing the 'value' field by field 'divisor'.
   * Field.of("value").mod(Field.of("divisor"));
   * }</pre>
   *
   * @param other The divisor expression.
   * @return A new {@code Expr} representing the modulo operation.
   */
  @BetaApi
  default Mod mod(Expr other) {
    return new Mod(this, other);
  }

  /**
   * Creates an expression that calculates the modulo (remainder) to another constant.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the remainder of dividing the 'value' field by 5.
   * Field.of("value").mod(5);
   * }</pre>
   *
   * @param other The divisor constant.
   * @return A new {@code Expr} representing the modulo operation.
   */
  @BetaApi
  default Mod mod(Object other) {
    return new Mod(this, Constant.of(other));
  }

  // /**
  //  * Creates an expression that applies an AND (&) operation with another expression.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the AND operation result from field 'flag' and 'mask'.
  //  * Field.of("flag").bitAnd(Field.of("mask"));
  //  * }</pre>
  //  *
  //  * @param other The expression to divide by.
  //  * @return A new {@code Expr} representing the division operation.
  //  */
  // @BetaApi
  // default BitAnd bitAnd(Expr other) {
  //   return new BitAnd(this, other);
  // }
  //
  // /**
  //  * Creates an expression that applies an AND (&) operation with a constant.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the AND operation result of field 'flag' and 0xff.
  //  * Field.of("flag").bigAnd(0xff);
  //  * }</pre>
  //  *
  //  * @param other The constant value to divide by.
  //  * @return A new {@code Expr} representing the division operation.
  //  */
  // @BetaApi
  // default BitAnd bitAnd(Object other) {
  //   return new BitAnd(this, Constant.of(other));
  // }
  //
  // /**
  //  * Creates an expression that applies an OR (|) operation with another expression.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the OR operation result from field 'flag' and 'mask'.
  //  * Field.of("flag").bitOr(Field.of("mask"));
  //  * }</pre>
  //  *
  //  * @param other The expression to apply OR with.
  //  * @return A new {@code Expr} representing the OR operation.
  //  */
  // @BetaApi
  // default BitOr bitOr(Expr other) {
  //   return new BitOr(this, other);
  // }
  //
  // /**
  //  * Creates an expression that applies an OR (|) operation with a constant.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the OR operation result of field 'flag' and 0xff.
  //  * Field.of("flag").bitOr(0xff);
  //  * }</pre>
  //  *
  //  * @param other The constant value to apply OR with.
  //  * @return A new {@code Expr} representing the OR operation.
  //  */
  // @BetaApi
  // default BitOr bitOr(Object other) {
  //   return new BitOr(this, Constant.of(other));
  // }
  //
  // /**
  //  * Creates an expression that applies an XOR (^) operation with another expression.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the XOR operation result from field 'flag' and 'mask'.
  //  * Field.of("flag").bitXor(Field.of("mask"));
  //  * }</pre>
  //  *
  //  * @param other The expression to apply XOR with.
  //  * @return A new {@code Expr} representing the XOR operation.
  //  */
  // @BetaApi
  // default BitXor bitXor(Expr other) {
  //   return new BitXor(this, other);
  // }
  //
  // /**
  //  * Creates an expression that applies an XOR (^) operation with a constant.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the XOR operation result of field 'flag' and 0xff.
  //  * Field.of("flag").bitXor(0xff);
  //  * }</pre>
  //  *
  //  * @param other The constant value to apply XOR with.
  //  * @return A new {@code Expr} representing the XOR operation.
  //  */
  // @BetaApi
  // default BitXor bitXor(Object other) {
  //   return new BitXor(this, Constant.of(other));
  // }
  //
  // /**
  //  * Creates an expression that applies a NOT (~) operation.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the NOT operation result of field 'flag'.
  //  * Field.of("flag").bitNot();
  //  * }</pre>
  //  *
  //  * @return A new {@code Expr} representing the NOT operation.
  //  */
  // @BetaApi
  // default BitNot bitNot() {
  //   return new BitNot(this);
  // }
  //
  // /**
  //  * Creates an expression that applies a left shift (<<) operation with another expression.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the left shift operation result from field 'flag' by 'shift' bits.
  //  * Field.of("flag").bitLeftShift(Field.of("shift"));
  //  * }</pre>
  //  *
  //  * @param other The expression representing the number of bits to shift left by.
  //  * @return A new {@code Expr} representing the left shift operation.
  //  */
  // @BetaApi
  // default BitLeftShift bitLeftShift(Expr other) {
  //   return new BitLeftShift(this, other);
  // }
  //
  // /**
  //  * Creates an expression that applies a left shift (<<) operation with a constant.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the left shift operation result of field 'flag' by 2 bits.
  //  * Field.of("flag").bitLeftShift(2);
  //  * }</pre>
  //  *
  //  * @param other The constant number of bits to shift left by.
  //  * @return A new {@code Expr} representing the left shift operation.
  //  */
  // @BetaApi
  // default BitLeftShift bitLeftShift(Object other) {
  //   return new BitLeftShift(this, Constant.of(other));
  // }
  //
  // /**
  //  * Creates an expression that applies a right shift (>>) operation with another expression.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the right shift operation result from field 'flag' by 'shift' bits.
  //  * Field.of("flag").bitRightShift(Field.of("shift"));
  //  * }</pre>
  //  *
  //  * @param other The expression representing the number of bits to shift right by.
  //  * @return A new {@code Expr} representing the right shift operation.
  //  */
  // @BetaApi
  // default BitRightShift bitRightShift(Expr other) {
  //   return new BitRightShift(this, other);
  // }
  //
  // /**
  //  * Creates an expression that applies a right shift (>>) operation with a constant.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the right shift operation result of field 'flag' by 2 bits.
  //  * Field.of("flag").bitRightShift(2);
  //  * }</pre>
  //  *
  //  * @param other The constant number of bits to shift right by.
  //  * @return A new {@code Expr} representing the right shift operation.
  //  */
  // @BetaApi
  // default BitRightShift bitRightShift(Object other) {
  //   return new BitRightShift(this, Constant.of(other));
  // }

  // Logical Functions

  /**
   * Creates an expression that returns the larger value between this expression and another
   * expression, based on Firestore's value type ordering.
   *
   * <p>Firestore's value type ordering is described here: <a
   * href="https://cloud.google.com/firestore/docs/concepts/data-types#value_type_ordering">...</a>
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Returns the larger value between the 'discount' field and the 'cap' field.
   * Field.of("discount").logicalMax(Field.of("cap"));
   * }</pre>
   *
   * @param other The other expression to compare with.
   * @return A new {@code Expr} representing the logical max operation.
   */
  default LogicalMax logicalMax(Expr other) {
    return new LogicalMax(this, other);
  }

  /**
   * Creates an expression that returns the larger value between this expression and a constant
   * value, based on Firestore's value type ordering.
   *
   * <p>Firestore's value type ordering is described here: <a
   * href="https://cloud.google.com/firestore/docs/concepts/data-types#value_type_ordering">...</a>
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Returns the larger value between the 'value' field and 10.
   * Field.of("value").logicalMax(10);
   * }</pre>
   *
   * @param other The constant value to compare with.
   * @return A new {@code Expr} representing the logical max operation.
   */
  default LogicalMax logicalMax(Object other) {
    return new LogicalMax(this, Constant.of(other));
  }

  /**
   * Creates an expression that returns the smaller value between this expression and another
   * expression, based on Firestore's value type ordering.
   *
   * <p>Firestore's value type ordering is described here: <a
   * href="https://cloud.google.com/firestore/docs/concepts/data-types#value_type_ordering">...</a>
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Returns the smaller value between the 'discount' field and the 'floor' field.
   * Field.of("discount").logicalMin(Field.of("floor"));
   * }</pre>
   *
   * @param other The other expression to compare with.
   * @return A new {@code Expr} representing the logical min operation.
   */
  default LogicalMin logicalMin(Expr other) {
    return new LogicalMin(this, other);
  }

  /**
   * Creates an expression that returns the smaller value between this expression and a constant
   * value, based on Firestore's value type ordering.
   *
   * <p>Firestore's value type ordering is described here: <a
   * href="https://cloud.google.com/firestore/docs/concepts/data-types#value_type_ordering">...</a>
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Returns the smaller value between the 'value' field and 10.
   * Field.of("value").logicalMin(10);
   * }</pre>
   *
   * @param other The constant value to compare with.
   * @return A new {@code Expr} representing the logical min operation.
   */
  default LogicalMin logicalMin(Object other) {
    return new LogicalMin(this, Constant.of(other));
  }

  // Comparison Operators

  /**
   * Creates an expression that checks if this expression is equal to another expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'age' field is equal to 21
   * Field.of("age").eq(21);
   * }</pre>
   *
   * @param other The expression to compare for equality.
   * @return A new {@code Expr} representing the equality comparison.
   */
  @BetaApi
  default Eq eq(Expr other) {
    return new Eq(this, other);
  }

  /**
   * Creates an expression that checks if this expression is equal to a constant value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'city' field is equal to "London"
   * Field.of("city").eq("London");
   * }</pre>
   *
   * @param other The constant value to compare for equality.
   * @return A new {@code Expr} representing the equality comparison.
   */
  @BetaApi
  default Eq eq(Object other) {
    return new Eq(this, Constant.of(other));
  }

  /**
   * Creates an expression that checks if this expression is not equal to another expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'status' field is not equal to "completed"
   * Field.of("status").neq("completed");
   * }</pre>
   *
   * @param other The expression to compare for inequality.
   * @return A new {@code Expr} representing the inequality comparison.
   */
  @BetaApi
  default Neq neq(Expr other) {
    return new Neq(this, other);
  }

  /**
   * Creates an expression that checks if this expression is not equal to a constant value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'country' field is not equal to "USA"
   * Field.of("country").neq("USA");
   * }</pre>
   *
   * @param other The constant value to compare for inequality.
   * @return A new {@code Expr} representing the inequality comparison.
   */
  @BetaApi
  default Neq neq(Object other) {
    return new Neq(this, Constant.of(other));
  }

  /**
   * Creates an expression that checks if this expression is greater than another expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'age' field is greater than the 'limit' field
   * Field.of("age").gt(Field.of("limit"));
   * }</pre>
   *
   * @param other The expression to compare for greater than.
   * @return A new {@code Expr} representing the greater than comparison.
   */
  @BetaApi
  default Gt gt(Expr other) {
    return new Gt(this, other);
  }

  /**
   * Creates an expression that checks if this expression is greater than a constant value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'price' field is greater than 100
   * Field.of("price").gt(100);
   * }</pre>
   *
   * @param other The constant value to compare for greater than.
   * @return A new {@code Expr} representing the greater than comparison.
   */
  @BetaApi
  default Gt gt(Object other) {
    return new Gt(this, Constant.of(other));
  }

  /**
   * Creates an expression that checks if this expression is greater than or equal to another
   * expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'quantity' field is greater than or equal to field 'requirement' plus 1
   * Field.of("quantity").gte(Field.of('requirement').add(1));
   * }</pre>
   *
   * @param other The expression to compare for greater than or equal to.
   * @return A new {@code Expr} representing the greater than or equal to comparison.
   */
  @BetaApi
  default Gte gte(Expr other) {
    return new Gte(this, other);
  }

  /**
   * Creates an expression that checks if this expression is greater than or equal to a constant
   * value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'score' field is greater than or equal to 80
   * Field.of("score").gte(80);
   * }</pre>
   *
   * @param other The constant value to compare for greater than or equal to.
   * @return A new {@code Expr} representing the greater than or equal to comparison.
   */
  @BetaApi
  default Gte gte(Object other) {
    return new Gte(this, Constant.of(other));
  }

  /**
   * Creates an expression that checks if this expression is less than another expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'age' field is less than 'limit'
   * Field.of("age").lt(Field.of('limit'));
   * }</pre>
   *
   * @param other The expression to compare for less than.
   * @return A new {@code Expr} representing the less than comparison.
   */
  @BetaApi
  default Lt lt(Expr other) {
    return new Lt(this, other);
  }

  /**
   * Creates an expression that checks if this expression is less than a constant value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'price' field is less than 50
   * Field.of("price").lt(50);
   * }</pre>
   *
   * @param other The constant value to compare for less than.
   * @return A new {@code Expr} representing the less than comparison.
   */
  @BetaApi
  default Lt lt(Object other) {
    return new Lt(this, Constant.of(other));
  }

  /**
   * Creates an expression that checks if this expression is less than or equal to another
   * expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'quantity' field is less than or equal to 20
   * Field.of("quantity").lte(Constant.of(20));
   * }</pre>
   *
   * @param other The expression to compare for less than or equal to.
   * @return A new {@code Expr} representing the less than or equal to comparison.
   */
  @BetaApi
  default Lte lte(Expr other) {
    return new Lte(this, other);
  }

  /**
   * Creates an expression that checks if this expression is less than or equal to a constant value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'score' field is less than or equal to 70
   * Field.of("score").lte(70);
   * }</pre>
   *
   * @param other The constant value to compare for less than or equal to.
   * @return A new {@code Expr} representing the less than or equal to comparison.
   */
  @BetaApi
  default Lte lte(Object other) {
    return new Lte(this, Constant.of(other));
  }

  // IN operator
  /**
   * Creates an expression that checks if this expression is equal to any of the provided values or
   * expressions.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'category' field is either "Electronics" or value of field 'primaryType'
   * Field.of("category").in("Electronics", Field.of("primaryType"));
   * }</pre>
   *
   * @param other The values or expressions to check against.
   * @return A new {@code Expr} representing the 'IN' comparison.
   */
  @BetaApi
  default In inAny(Object... other) {
    return new In(this, toExprList(other));
  }

  /**
   * Creates an expression that checks if this expression is not equal to any of the provided values
   * or expressions.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'status' field is neither "pending" nor "cancelled"
   * Field.of("status").notIn("pending", "cancelled");
   * }</pre>
   *
   * @param other The values or expressions to check against.
   * @return A new {@code Expr} representing the 'NOT IN' comparison.
   */
  @BetaApi
  default Not notInAny(Object... other) {
    return new Not(inAny(other));
  }

  // Array Functions

  /**
   * Creates an expression that concatenates an array expression with another array.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Combine the 'tags' array with a new array and an array field
   * Field.of("tags").arrayConcat(Arrays.asList("newTag1", "newTag2"), Field.of("otherTag"));
   * }</pre>
   *
   * @param array The array of constants or expressions to concat with.
   * @return A new {@code Expr} representing the concatenated array.
   */
  @BetaApi
  default ArrayConcat arrayConcat(List<Object> array) {
    return new ArrayConcat(this, toExprList(array.toArray()));
  }

  /**
   * Creates an expression that checks if an array contains a specific element.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'sizes' array contains the value from the 'selectedSize' field
   * Field.of("sizes").arrayContains(Field.of("selectedSize"));
   * }</pre>
   *
   * @param element The element to search for in the array.
   * @return A new {@code Expr} representing the 'array_contains' comparison.
   */
  @BetaApi
  default ArrayContains arrayContains(Expr element) {
    return new ArrayContains(this, element);
  }

  /**
   * Creates an expression that checks if an array contains a specific value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'colors' array contains "red"
   * Field.of("colors").arrayContains("red");
   * }</pre>
   *
   * @param element The element to search for in the array.
   * @return A new {@code Expr} representing the 'array_contains' comparison.
   */
  @BetaApi
  default ArrayContains arrayContains(Object element) {
    return new ArrayContains(this, Constant.of(element));
  }

  /**
   * Creates an expression that checks if an array contains all the specified elements.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'tags' array contains both "news" and "sports"
   * Field.of("tags").arrayContainsAll(Field.of("tag1"), Field.of("tag2"));
   * }</pre>
   *
   * @param elements The elements to check for in the array.
   * @return A new {@code Expr} representing the 'array_contains_all' comparison.
   */
  @BetaApi
  default ArrayContainsAll arrayContainsAll(Expr... elements) {
    return new ArrayContainsAll(this, Arrays.asList(elements));
  }

  /**
   * Creates an expression that checks if an array contains all the specified elements.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'tags' array contains both of the values from field 'tag1' and "tag2"
   * Field.of("tags").arrayContainsAll(Field.of("tag1"), Field.of("tag2"));
   * }</pre>
   *
   * @param elements The elements to check for in the array.
   * @return A new {@code Expr} representing the 'array_contains_all' comparison.
   */
  @BetaApi
  default ArrayContainsAll arrayContainsAll(Object... elements) {
    return new ArrayContainsAll(this, toExprList(elements));
  }

  /**
   * Creates an expression that checks if an array contains any of the specified elements.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'categories' array contains either values from field "cate1" or "cate2"
   * Field.of("categories").arrayContainsAny(Field.of("cate1"), Field.of("cate2"));
   * }</pre>
   *
   * @param elements The elements to check for in the array.
   * @return A new {@code Expr} representing the 'array_contains_any' comparison.
   */
  @BetaApi
  default ArrayContainsAny arrayContainsAny(Expr... elements) {
    return new ArrayContainsAny(this, Arrays.asList(elements));
  }

  /**
   * Creates an expression that checks if an array contains any of the specified elements.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'groups' array contains either the value from the 'userGroup' field
   * // or the value "guest"
   * Field.of("groups").arrayContainsAny(Field.of("userGroup"), "guest");
   * }</pre>
   *
   * @param elements The elements to check for in the array.
   * @return A new {@code Expr} representing the 'array_contains_any' comparison.
   */
  @BetaApi
  default ArrayContainsAny arrayContainsAny(Object... elements) {
    return new ArrayContainsAny(this, toExprList(elements));
  }

  /**
   * Creates an expression that calculates the length of an array.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Get the number of items in the 'cart' array
   * Field.of("cart").arrayLength();
   * }</pre>
   *
   * @return A new {@code Expr} representing the length of the array.
   */
  @BetaApi
  default ArrayLength arrayLength() {
    return new ArrayLength(this);
  }

  /**
   * Creates an expression that returns the reversed content of an array.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Get the 'preferences' array in reversed order.
   * Field.of("preferences").arrayReverse();
   * }</pre>
   *
   * @return A new {@code Expr} representing the length of the array.
   */
  @BetaApi
  default ArrayReverse arrayReverse() {
    return new ArrayReverse(this);
  }

  // Other Functions

  /**
   * Creates an expression that checks if this expression evaluates to 'NaN' (Not a Number).
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the result of a calculation is NaN
   * Field.of("value").divide(0).isNaN();
   * }</pre>
   *
   * @return A new {@code Expr} representing the 'isNaN' check.
   */
  @BetaApi
  default IsNaN isNaN() {
    return new IsNaN(this);
  }

  /**
   * Creates an expression that checks if a field exists in the document.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the document has a field named "phoneNumber"
   * Field.of("phoneNumber").exists();
   * }</pre>
   *
   * @return A new {@code Expr} representing the 'exists' check.
   */
  @BetaApi
  default Exists exists() {
    return new Exists(this);
  }

  // Aggregate Functions

  /**
   * Creates an aggregation that calculates the sum of a numeric field across multiple stage inputs.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the total revenue from a set of orders
   * Field.of("orderAmount").sum().as("totalRevenue");
   * }</pre>
   *
   * @return A new {@code Accumulator} representing the 'sum' aggregation.
   */
  @BetaApi
  default Sum sum() {
    return new Sum(this, false);
  }

  /**
   * Creates an aggregation that calculates the average (mean) of a numeric field across multiple
   * stage inputs.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the average age of users
   * Field.of("age").avg().as("averageAge");
   * }</pre>
   *
   * @return A new {@code Accumulator} representing the 'avg' aggregation.
   */
  @BetaApi
  default Avg avg() {
    return new Avg(this, false);
  }

  /**
   * Creates an aggregation that counts the number of stage inputs with valid evaluations of the
   * expression or field.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Count the total number of products
   * Field.of("productId").count().as("totalProducts");
   * }</pre>
   *
   * @return A new {@code Accumulator} representing the 'count' aggregation.
   */
  @BetaApi
  default Count count() {
    return new Count(this);
  }

  /**
   * Creates an aggregation that finds the minimum value of a field across multiple stage inputs.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Find the lowest price of all products
   * Field.of("price").min().as("lowestPrice");
   * }</pre>
   *
   * @return A new {@code Accumulator} representing the 'min' aggregation.
   */
  @BetaApi
  default Min min() {
    return new Min(this, false);
  }

  /**
   * Creates an aggregation that finds the maximum value of a field across multiple stage inputs.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Find the highest score in a leaderboard
   * Field.of("score").max().as("highestScore");
   * }</pre>
   *
   * @return A new {@code Accumulator} representing the 'max' aggregation.
   */
  @BetaApi
  default Max max() {
    return new Max(this, false);
  }

  // String Functions

  /**
   * Creates an expression that calculates the character length of a string.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Get the character length of the 'name' field
   * Field.of("name").charLength();
   * }</pre>
   *
   * @return A new {@code Expr} representing the length of the string.
   */
  @BetaApi
  default CharLength charLength() {
    return new CharLength(this);
  }

  /**
   * Creates an expression that calculates the byte length of a string in its UTF-8 form.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Get the byte length of the 'name' field
   * Field.of("name").byteLength();
   * }</pre>
   *
   * @return A new {@code Expr} representing the byte length of the string.
   */
  @BetaApi
  default ByteLength byteLength() {
    return new ByteLength(this);
  }

  /**
   * Creates an expression that performs a case-sensitive string comparison.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'title' field contains the word "guide" (case-sensitive)
   * Field.of("title").like("%guide%");
   * }</pre>
   *
   * @param pattern The pattern to search for. You can use "%" as a wildcard character.
   * @return A new {@code Expr} representing the 'like' comparison.
   */
  @BetaApi
  default Like like(String pattern) {
    return new Like(this, Constant.of(pattern));
  }

  /**
   * Creates an expression that performs a case-sensitive string comparison.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'title' field matches the pattern specified in field 'pattern'.
   * Field.of("title").like(Field.of("pattern"));
   * }</pre>
   *
   * @param pattern The expression evaluates to a pattern.
   * @return A new {@code Expr} representing the 'like' comparison.
   */
  @BetaApi
  default Like like(Expr pattern) {
    return new Like(this, pattern);
  }

  /**
   * Creates an expression that checks if a string contains a specified regular expression as a
   * substring.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'description' field contains "example" (case-insensitive)
   * Field.of("description").regexContains("(?i)example");
   * }</pre>
   *
   * @param regex The regular expression to use for the search.
   * @return A new {@code Expr} representing the 'contains' comparison.
   */
  @BetaApi
  default RegexContains regexContains(String regex) {
    return new RegexContains(this, Constant.of(regex));
  }

  /**
   * Creates an expression that checks if a string contains a specified regular expression as a
   * substring.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'description' field contains the regular expression stored in field 'regex'
   * Field.of("description").regexContains(Field.of("regex"));
   * }</pre>
   *
   * @param regex The regular expression to use for the search.
   * @return A new {@code Expr} representing the 'contains' comparison.
   */
  @BetaApi
  default RegexContains regexContains(Expr regex) {
    return new RegexContains(this, regex);
  }

  /**
   * Creates an expression that checks if a string matches a specified regular expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'email' field matches a valid email pattern
   * Field.of("email").regexMatches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");
   * }</pre>
   *
   * @param regex The regular expression to use for the match.
   * @return A new {@code Expr} representing the regular expression match.
   */
  @BetaApi
  default RegexMatch regexMatches(String regex) {
    return new RegexMatch(this, Constant.of(regex));
  }

  /**
   * Creates an expression that checks if a string matches a specified regular expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'email' field matches a regular expression stored in field 'regex'
   * Field.of("email").regexMatches(Field.of("regex"));
   * }</pre>
   *
   * @param regex The regular expression to use for the match.
   * @return A new {@code Expr} representing the regular expression match.
   */
  @BetaApi
  default RegexMatch regexMatches(Expr regex) {
    return new RegexMatch(this, regex);
  }

  /**
   * Creates an expression that checks if this string expression contains a specified substring.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'description' field contains "example".
   * Field.of("description").strContains("example");
   * }</pre>
   *
   * @param substring The substring to use for the search.
   * @return A new {@code Expr} representing the 'contains' comparison.
   */
  @BetaApi
  default StrContains strContains(String substring) {
    return new StrContains(this, Constant.of(substring));
  }

  /**
   * Creates an expression that checks if this string expression contains the string represented by
   * another expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'description' field contains the value of the 'keyword' field.
   * Field.of("description").strContains(Field.of("keyword"));
   * }</pre>
   *
   * @param expr The expression representing the substring to search for.
   * @return A new {@code Expr} representing the 'contains' comparison.
   */
  @BetaApi
  default StrContains strContains(Expr expr) {
    return new StrContains(this, expr);
  }

  /**
   * Creates an expression that checks if a string starts with a given prefix.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'name' field starts with "Mr."
   * Field.of("name").startsWith("Mr.");
   * }</pre>
   *
   * @param prefix The prefix to check for.
   * @return A new {@code Expr} representing the 'starts with' comparison.
   */
  @BetaApi
  default StartsWith startsWith(String prefix) {
    return new StartsWith(this, Constant.of(prefix));
  }

  /**
   * Creates an expression that checks if a string starts with a given prefix (represented as an
   * expression).
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'fullName' field starts with the value of the 'firstName' field
   * Field.of("fullName").startsWith(Field.of("firstName"));
   * }</pre>
   *
   * @param prefix The prefix expression to check for.
   * @return A new {@code Expr} representing the 'starts with' comparison.
   */
  @BetaApi
  default StartsWith startsWith(Expr prefix) {
    return new StartsWith(this, prefix);
  }

  /**
   * Creates an expression that checks if a string ends with a given postfix.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'filename' field ends with ".txt"
   * Field.of("filename").endsWith(".txt");
   * }</pre>
   *
   * @param postfix The postfix to check for.
   * @return A new {@code Expr} representing the 'ends with' comparison.
   */
  @BetaApi
  default EndsWith endsWith(String postfix) {
    return new EndsWith(this, Constant.of(postfix));
  }

  /**
   * Creates an expression that checks if a string ends with a given postfix (represented as an
   * expression).
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'url' field ends with the value of the 'extension' field
   * Field.of("url").endsWith(Field.of("extension"));
   * }</pre>
   *
   * @param postfix The postfix expression to check for.
   * @return A new {@code Expr} representing the 'ends with' comparison.
   */
  @BetaApi
  default EndsWith endsWith(Expr postfix) {
    return new EndsWith(this, postfix);
  }

  /**
   * Creates an expression that concatenates string expressions together.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Combine the 'firstName', " ", and 'lastName' fields into a single string
   * Field.of("firstName").strConcat(Constant.of(" "), Field.of("lastName"));
   * }</pre>
   *
   * @param elements The expressions (typically strings) to concatenate.
   * @return A new {@code Expr} representing the concatenated string.
   */
  @BetaApi
  default StrConcat strConcat(Expr... elements) {
    return new StrConcat(this, Arrays.asList(elements));
  }

  /**
   * Creates an expression that concatenates string functions, fields or constants together.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Combine the 'firstName', " ", and 'lastName' fields into a single string
   * Field.of("firstName").strConcat(" ", Field.of("lastName"));
   * }</pre>
   *
   * @param elements The expressions (typically strings) to concatenate.
   * @return A new {@code Expr} representing the concatenated string.
   */
  @BetaApi
  default StrConcat strConcat(Object... elements) {
    return new StrConcat(this, toExprList(elements));
  }

  /**
   * Creates an expression that converts a string to lowercase.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Convert the 'name' field to lowercase
   * Field.of("name").toLowerCase();
   * }</pre>
   *
   * @return A new {@code Expr} representing the lowercase string.
   */
  @BetaApi
  default ToLower toLower() {
    return new ToLower(this);
  }

  /**
   * Creates an expression that converts a string to uppercase.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Convert the 'title' field to uppercase
   * Field.of("title").toUpper();
   * }</pre>
   *
   * @return A new {@code Expr} representing the uppercase string.
   */
  @BetaApi
  default ToUpper toUpper() {
    return new ToUpper(this);
  }

  /**
   * Creates an expression that removes leading and trailing whitespace from a string.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Trim whitespace from the 'userInput' field
   * Field.of("userInput").trim();
   * }</pre>
   *
   * @return A new {@code Expr} representing the trimmed string.
   */
  @BetaApi
  default Trim trim() {
    return new Trim(this);
  }

  /**
   * Creates an expression that reverses a string.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Reverse the 'userInput' field
   * Field.of("userInput").reverse();
   * }</pre>
   *
   * @return A new {@code Expr} representing the reversed string.
   */
  @BetaApi
  default Reverse reverse() {
    return new Reverse(this);
  }

  /**
   * Creates an expression that replaces the first occurrence of a substring within a string with
   * another substring.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Replace the first occurrence of "hello" with "hi" in the 'message' field
   * Field.of("message").replaceFirst("hello", "hi");
   * }</pre>
   *
   * @param find The substring to search for.
   * @param replace The substring to replace the first occurrence of 'find' with.
   * @return A new {@code Expr} representing the string with the first occurrence replaced.
   */
  @BetaApi
  default ReplaceFirst replaceFirst(String find, String replace) {
    return new ReplaceFirst(this, Constant.of(find), Constant.of(replace));
  }

  /**
   * Creates an expression that replaces the first occurrence of a substring within a string with
   * another substring, where the substring to find and the replacement substring are specified by
   * expressions.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Replace the first occurrence of the value in 'findField' with the value in 'replaceField' in the 'message' field
   * Field.of("message").replaceFirst(Field.of("findField"), Field.of("replaceField"));
   * }</pre>
   *
   * @param find The expression representing the substring to search for.
   * @param replace The expression representing the substring to replace the first occurrence of
   *     'find' with.
   * @return A new {@code Expr} representing the string with the first occurrence replaced.
   */
  @BetaApi
  default ReplaceFirst replaceFirst(Expr find, Expr replace) {
    return new ReplaceFirst(this, find, replace);
  }

  /**
   * Creates an expression that replaces all occurrences of a substring within a string with another
   * substring.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Replace all occurrences of "hello" with "hi" in the 'message' field
   * Field.of("message").replaceAll("hello", "hi");
   * }</pre>
   *
   * @param find The substring to search for.
   * @param replace The substring to replace all occurrences of 'find' with.
   * @return A new {@code Expr} representing the string with all occurrences replaced.
   */
  @BetaApi
  default ReplaceAll replaceAll(String find, String replace) {
    return new ReplaceAll(this, Constant.of(find), Constant.of(replace));
  }

  /**
   * Creates an expression that replaces all occurrences of a substring within a string with another
   * substring, where the substring to find and the replacement substring are specified by
   * expressions.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Replace all occurrences of the value in 'findField' with the value in 'replaceField' in the 'message' field
   * Field.of("message").replaceAll(Field.of("findField"), Field.of("replaceField"));
   * }</pre>
   *
   * @param find The expression representing the substring to search for.
   * @param replace The expression representing the substring to replace all occurrences of 'find'
   *     with.
   * @return A new {@code Expr} representing the string with all occurrences replaced.
   */
  @BetaApi
  default ReplaceAll replaceAll(Expr find, Expr replace) {
    return new ReplaceAll(this, find, replace);
  }

  // map functions

  /**
   * Accesses a value from a map (object) field using the provided key.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Get the 'city' value from
   * // the 'address' map field
   * Field.of("address").mapGet("city");
   * }</pre>
   *
   * @param key The key to access in the map.
   * @return A new {@code Expr} representing the value associated with the given key in the map.
   */
  @BetaApi
  default MapGet mapGet(String key) {
    return new MapGet(this, key);
  }

  /**
   * Calculates the cosine distance between two vectors.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the cosine distance between the 'userVector' field and the 'itemVector' field
   * Field.of("userVector").cosineDistance(Field.of("itemVector"));
   * }</pre>
   *
   * @param other The other vector (represented as an Expr) to compare against.
   * @return A new {@code Expr} representing the cosine distance between the two vectors.
   */
  @BetaApi
  default CosineDistance cosineDistance(Expr other) {
    return new CosineDistance(this, other);
  }

  /**
   * Calculates the Cosine distance between two vectors.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the Cosine distance between the 'location' field and a target location
   * Field.of("location").cosineDistance(new double[] {37.7749, -122.4194});
   * }</pre>
   *
   * @param other The other vector (as an array of doubles) to compare against.
   * @return A new {@code Expr} representing the Cosine distance between the two vectors.
   */
  @BetaApi
  default CosineDistance cosineDistance(double[] other) {
    return new CosineDistance(this, Constant.vector(other));
  }

  /**
   * Calculates the Euclidean distance between two vectors.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the Euclidean distance between the 'location' field and a target location
   * Field.of("location").euclideanDistance(new double[] {37.7749, -122.4194});
   * }</pre>
   *
   * @param other The other vector (as an array of doubles) to compare against.
   * @return A new {@code Expr} representing the Euclidean distance between the two vectors.
   */
  @BetaApi
  default EuclideanDistance euclideanDistance(double[] other) {
    return new EuclideanDistance(this, Constant.vector(other));
  }

  /**
   * Calculates the Euclidean distance between two vectors.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the Euclidean distance between two vector fields: 'pointA' and 'pointB'
   * Field.of("pointA").euclideanDistance(Field.of("pointB"));
   * }</pre>
   *
   * @param other The other vector (represented as an Expr) to compare against.
   * @return A new {@code Expr} representing the Euclidean distance between the two vectors.
   */
  @BetaApi
  default EuclideanDistance euclideanDistance(Expr other) {
    return new EuclideanDistance(this, other);
  }

  /**
   * Calculates the dot product between two vectors.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the dot product between a feature vector and a target vector
   * Field.of("features").dotProduct(new double[] {0.5, 0.8, 0.2});
   * }</pre>
   *
   * @param other The other vector (represented as an Expr) to calculate dot product with.
   * @return A new {@code Expr} representing the dot product between the two vectors.
   */
  @BetaApi
  default DotProduct dotProduct(double[] other) {
    return new DotProduct(this, Constant.vector(other));
  }

  /**
   * Calculates the dot product between two vectors.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the dot product between two document vectors: 'docVector1' and 'docVector2'
   * Field.of("docVector1").dotProduct(Field.of("docVector2"));
   * }</pre>
   *
   * @param other The other vector (represented as an Expr) to calculate dot product with.
   * @return A new {@code Expr} representing the dot product between the two vectors.
   */
  @BetaApi
  default DotProduct dotProduct(Expr other) {
    return new DotProduct(this, other);
  }

  /**
   * Creates an expression that calculates the length of a Firestore Vector.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Get the vector length (dimension) of the field 'embedding'.
   * Field.of("embedding").vectorLength();
   * }</pre>
   *
   * @return A new {@code Expr} representing the length of the array.
   */
  @BetaApi
  default VectorLength vectorLength() {
    return new VectorLength(this);
  }

  // Timestamps

  /**
   * Creates an expression that converts a timestamp to the number of microseconds since the epoch
   * (1970-01-01 00:00:00 UTC).
   *
   * <p>Truncates higher levels of precision by rounding down to the beginning of the microsecond.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Convert the 'timestamp' field to microseconds since the epoch.
   * Field.of("timestamp").timestampToUnixMicros();
   * }</pre>
   *
   * @return A new {@code Expr} representing the number of microseconds since the epoch.
   */
  @BetaApi
  default TimestampToUnixMicros timestampToUnixMicros() {
    return new TimestampToUnixMicros(this);
  }

  /**
   * Creates an expression that converts a number of microseconds since the epoch (1970-01-01
   * 00:00:00 UTC) to a timestamp.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Convert the 'microseconds' field to a timestamp.
   * Field.of("microseconds").unixMicrosToTimestamp();
   * }</pre>
   *
   * @return A new {@code Expr} representing the timestamp.
   */
  @BetaApi
  default UnixMicrosToTimestamp unixMicrosToTimestamp() {
    return new UnixMicrosToTimestamp(this);
  }

  /**
   * Creates an expression that converts a timestamp to the number of milliseconds since the epoch
   * (1970-01-01 00:00:00 UTC).
   *
   * <p>Truncates higher levels of precision by rounding down to the beginning of the millisecond.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Convert the 'timestamp' field to milliseconds since the epoch.
   * Field.of("timestamp").timestampToUnixMillis();
   * }</pre>
   *
   * @return A new {@code Expr} representing the number of milliseconds since the epoch.
   */
  @BetaApi
  default TimestampToUnixMillis timestampToUnixMillis() {
    return new TimestampToUnixMillis(this);
  }

  /**
   * Creates an expression that converts a number of milliseconds since the epoch (1970-01-01
   * 00:00:00 UTC) to a timestamp.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Convert the 'milliseconds' field to a timestamp.
   * Field.of("milliseconds").unixMillisToTimestamp();
   * }</pre>
   *
   * @return A new {@code Expr} representing the timestamp.
   */
  @BetaApi
  default UnixMillisToTimestamp unixMillisToTimestamp() {
    return new UnixMillisToTimestamp(this);
  }

  /**
   * Creates an expression that converts a timestamp to the number of seconds since the epoch
   * (1970-01-01 00:00:00 UTC).
   *
   * <p>Truncates higher levels of precision by rounding down to the beginning of the second.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Convert the 'timestamp' field to seconds since the epoch.
   * Field.of("timestamp").timestampToUnixSeconds();
   * }</pre>
   *
   * @return A new {@code Expr} representing the number of seconds since the epoch.
   */
  @BetaApi
  default TimestampToUnixSeconds timestampToUnixSeconds() {
    return new TimestampToUnixSeconds(this);
  }

  /**
   * Creates an expression that converts a number of seconds since the epoch (1970-01-01 00:00:00
   * UTC) to a timestamp.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Convert the 'seconds' field to a timestamp.
   * Field.of("seconds").unixSecondsToTimestamp();
   * }</pre>
   *
   * @return A new {@code Expr} representing the timestamp.
   */
  @BetaApi
  default UnixSecondsToTimestamp unixSecondsToTimestamp() {
    return new UnixSecondsToTimestamp(this);
  }

  /**
   * Creates an expression that adds a specified amount of time to this timestamp expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Add a duration specified by the 'unit' and 'amount' fields to the 'timestamp' field.
   * Field.of("timestamp").timestampAdd(Field.of("unit"), Field.of("amount"));
   * }</pre>
   *
   * @param unit The expression evaluating to the unit of time to add, must be one of 'microsecond',
   *     'millisecond', 'second', 'minute', 'hour', 'day'.
   * @param amount The expression representing the amount of time to add.
   * @return A new {@code Expr} representing the resulting timestamp.
   */
  @BetaApi
  default TimestampAdd timestampAdd(Expr unit, Expr amount) {
    return new TimestampAdd(this, unit, amount);
  }

  /**
   * Creates an expression that adds a specified amount of time to this timestamp expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Add 1.5 days to the 'timestamp' field.
   * Field.of("timestamp").timestampAdd("day", 1.5);
   * }</pre>
   *
   * @param unit The unit of time to add, must be one of 'microsecond', 'millisecond', 'second',
   *     'minute', 'hour', 'day'.
   * @param amount The amount of time to add.
   * @return A new {@code Expr} representing the resulting timestamp.
   */
  @BetaApi
  default TimestampAdd timestampAdd(String unit, Double amount) {
    return new TimestampAdd(this, Constant.of(unit), Constant.of(amount));
  }

  /**
   * Creates an expression that subtracts a specified amount of time from this timestamp expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Subtract a duration specified by the 'unit' and 'amount' fields from the 'timestamp' field.
   * Field.of("timestamp").timestampSub(Field.of("unit"), Field.of("amount"));
   * }</pre>
   *
   * @param unit The expression evaluating to the unit of time to add, must be one of 'microsecond',
   *     'millisecond', 'second', 'minute', 'hour', 'day'.
   * @param amount The expression representing the amount of time to subtract.
   * @return A new {@code Expr} representing the resulting timestamp.
   */
  @BetaApi
  default TimestampSub timestampSub(Expr unit, Expr amount) {
    return new TimestampSub(this, unit, amount);
  }

  /**
   * Creates an expression that subtracts a specified amount of time from this timestamp expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Subtract 2.5 hours from the 'timestamp' field.
   * Field.of("timestamp").timestampSub("hour", 2.5);
   * }</pre>
   *
   * @param unit The unit of time to subtract must be one of 'microsecond', 'millisecond', 'second',
   *     'minute', 'hour', 'day'.
   * @param amount The amount of time to subtract.
   * @return A new {@code Expr} representing the resulting timestamp.
   */
  @BetaApi
  default TimestampSub timestampSub(String unit, Double amount) {
    return new TimestampSub(this, Constant.of(unit), Constant.of(amount));
  }

  // Ordering

  /**
   * Creates an {@link Ordering} that sorts documents in ascending order based on this expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Sort documents by the 'name' field in ascending order
   * firestore.pipeline().collection("users")
   *   .sort(Field.of("name").ascending());
   * }</pre>
   *
   * @return A new {@code Ordering} for ascending sorting.
   */
  @BetaApi
  default Ordering ascending() {
    return Ordering.ascending(this);
  }

  /**
   * Creates an {@link Ordering} that sorts documents in descending order based on this expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Sort documents by the 'createdAt' field in descending order
   * firestore.pipeline().collection("users")
   *   .sort(Field.of("createdAt").descending());
   * }</pre>
   *
   * @return A new {@code Ordering} for descending sorting.
   */
  @BetaApi
  default Ordering descending() {
    return Ordering.descending(this);
  }

  // Alias

  /**
   * Assigns an alias to this expression.
   *
   * <p>Aliases are useful for renaming fields in the output of a stage or for giving meaningful
   * names to calculated values.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the total price and assign it the alias "totalPrice" and add it to the output.
   * firestore.pipeline().collection("items")
   *   .addFields(Field.of("price").multiply(Field.of("quantity")).as("totalPrice"));
   * }</pre>
   *
   * @param alias The alias to assign to this expression.
   * @return A new {@code Selectable} (typically an {@link ExprWithAlias}) that wraps this
   *     expression and associates it with the provided alias.
   */
  @BetaApi
  default Selectable as(String alias) {
    return new ExprWithAlias<>(this, alias);
  }
}
