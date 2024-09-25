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
import com.google.api.core.InternalApi;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
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

  protected Function(String name, ImmutableList<? extends Expr> params) {
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
   * Creates an expression that calculates the modulo (remainder) of dividing two expressions.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the remainder of dividing the 'value' field by field 'divisor'.
   * Function.mod(Field.of("value"), Field.of("divisor"));
   * }</pre>
   *
   * @param left The dividend expression.
   * @param right The divisor expression.
   * @return A new {@code Expr} representing the modulo operation.
   */
  @BetaApi
  public static Mod mod(Expr left, Expr right) {
    return new Mod(left, right);
  }

  /**
   * Creates an expression that calculates the modulo (remainder) of dividing an expression by a
   * constant value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the remainder of dividing the 'value' field by 5.
   * Function.mod(Field.of("value"), 5);
   * }</pre>
   *
   * @param left The dividend expression.
   * @param right The divisor constant.
   * @return A new {@code Expr} representing the modulo operation.
   */
  @BetaApi
  public static Mod mod(Expr left, Object right) {
    return new Mod(left, Constant.of(right));
  }

  /**
   * Creates an expression that calculates the modulo (remainder) of dividing a field's value by an
   * expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the remainder of dividing the 'value' field by the 'divisor' field.
   * Function.mod("value", Field.of("divisor"));
   * }</pre>
   *
   * @param left The dividend field name.
   * @param right The divisor expression.
   * @return A new {@code Expr} representing the modulo operation.
   */
  @BetaApi
  public static Mod mod(String left, Expr right) {
    return new Mod(Field.of(left), right);
  }

  /**
   * Creates an expression that calculates the modulo (remainder) of dividing a field's value by a
   * constant value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the remainder of dividing the 'value' field by 5.
   * Function.mod("value", 5);
   * }</pre>
   *
   * @param left The dividend field name.
   * @param right The divisor constant.
   * @return A new {@code Expr} representing the modulo operation.
   */
  @BetaApi
  public static Mod mod(String left, Object right) {
    return new Mod(Field.of(left), Constant.of(right));
  }

  // // BitAnd
  //
  // /**
  //  * Creates an expression that applies an AND (&) operation between two expressions.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the AND operation result from field 'flag' and 'mask'.
  //  * Function.bitAnd(Field.of("flag"), Field.of("mask"));
  //  * }</pre>
  //  *
  //  * @param left The left operand expression.
  //  * @param right The right operand expression.
  //  * @return A new {@code Expr} representing the AND operation.
  //  */
  // @BetaApi
  // public static BitAnd bitAnd(Expr left, Expr right) {
  //   return new BitAnd(left, right);
  // }
  //
  // /**
  //  * Creates an expression that applies an AND (&) operation between an expression and a
  // constant.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the AND operation result of field 'flag' and 0xff.
  //  * Function.bitAnd(Field.of("flag"), 0xff);
  //  * }</pre>
  //  *
  //  * @param left The left operand expression.
  //  * @param right The right operand constant.
  //  * @return A new {@code Expr} representing the AND operation.
  //  */
  // @BetaApi
  // public static BitAnd bitAnd(Expr left, Object right) {
  //   return new BitAnd(left, Constant.of(right));
  // }
  //
  // /**
  //  * Creates an expression that applies an AND (&) operation between a field and an expression.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the AND operation result from field 'flag' and 'mask'.
  //  * Function.bitAnd("flag", Field.of("mask"));
  //  * }</pre>
  //  *
  //  * @param left The left operand field name.
  //  * @param right The right operand expression.
  //  * @return A new {@code Expr} representing the AND operation.
  //  */
  // @BetaApi
  // public static BitAnd bitAnd(String left, Expr right) {
  //   return new BitAnd(Field.of(left), right);
  // }
  //
  // /**
  //  * Creates an expression that applies an AND (&) operation between a field and a constant.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the AND operation result of field 'flag' and 0xff.
  //  * Function.bitAnd("flag", 0xff);
  //  * }</pre>
  //  *
  //  * @param left The left operand field name.
  //  * @param right The right operand constant.
  //  * @return A new {@code Expr} representing the AND operation.
  //  */
  // @BetaApi
  // public static BitAnd bitAnd(String left, Object right) {
  //   return new BitAnd(Field.of(left), Constant.of(right));
  // }
  //
  // // BitOr
  //
  // /**
  //  * Creates an expression that applies an OR (|) operation between two expressions.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the OR operation result from field 'flag' and 'mask'.
  //  * Function.bitOr(Field.of("flag"), Field.of("mask"));
  //  * }</pre>
  //  *
  //  * @param left The left operand expression.
  //  * @param right The right operand expression.
  //  * @return A new {@code Expr} representing the OR operation.
  //  */
  // @BetaApi
  // public static BitOr bitOr(Expr left, Expr right) {
  //   return new BitOr(left, right);
  // }
  //
  // /**
  //  * Creates an expression that applies an OR (|) operation between an expression and a constant.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the OR operation result of field 'flag' and 0xff.
  //  * Function.bitOr(Field.of("flag"), 0xff);
  //  * }</pre>
  //  *
  //  * @param left The left operand expression.
  //  * @param right The right operand constant.
  //  * @return A new {@code Expr} representing the OR operation.
  //  */
  // @BetaApi
  // public static BitOr bitOr(Expr left, Object right) {
  //   return new BitOr(left, Constant.of(right));
  // }
  //
  // /**
  //  * Creates an expression that applies an OR (|) operation between a field and an expression.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the OR operation result from field 'flag' and 'mask'.
  //  * Function.bitOr("flag", Field.of("mask"));
  //  * }</pre>
  //  *
  //  * @param left The left operand field name.
  //  * @param right The right operand expression.
  //  * @return A new {@code Expr} representing the OR operation.
  //  */
  // @BetaApi
  // public static BitOr bitOr(String left, Expr right) {
  //   return new BitOr(Field.of(left), right);
  // }
  //
  // /**
  //  * Creates an expression that applies an OR (|) operation between a field and a constant.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the OR operation result of field 'flag' and 0xff.
  //  * Function.bitOr("flag", 0xff);
  //  * }</pre>
  //  *
  //  * @param left The left operand field name.
  //  * @param right The right operand constant.
  //  * @return A new {@code Expr} representing the OR operation.
  //  */
  // @BetaApi
  // public static BitOr bitOr(String left, Object right) {
  //   return new BitOr(Field.of(left), Constant.of(right));
  // }
  //
  // // BitXor
  //
  // /**
  //  * Creates an expression that applies an XOR (^) operation between two expressions.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the XOR operation result from field 'flag' and 'mask'.
  //  * Function.bitXor(Field.of("flag"), Field.of("mask"));
  //  * }</pre>
  //  *
  //  * @param left The left operand expression.
  //  * @param right The right operand expression.
  //  * @return A new {@code Expr} representing the XOR operation.
  //  */
  // @BetaApi
  // public static BitXor bitXor(Expr left, Expr right) {
  //   return new BitXor(left, right);
  // }
  //
  // /**
  //  * Creates an expression that applies an XOR (^) operation between an expression and a
  // constant.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the XOR operation result of field 'flag' and 0xff.
  //  * Function.bitXor(Field.of("flag"), 0xff);
  //  * }</pre>
  //  *
  //  * @param left The left operand expression.
  //  * @param right The right operand constant.
  //  * @return A new {@code Expr} representing the XOR operation.
  //  */
  // @BetaApi
  // public static BitXor bitXor(Expr left, Object right) {
  //   return new BitXor(left, Constant.of(right));
  // }
  //
  // /**
  //  * Creates an expression that applies an XOR (^) operation between a field and an expression.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the XOR operation result from field 'flag' and 'mask'.
  //  * Function.bitXor("flag", Field.of("mask"));
  //  * }</pre>
  //  *
  //  * @param left The left operand field name.
  //  * @param right The right operand expression.
  //  * @return A new {@code Expr} representing the XOR operation.
  //  */
  // @BetaApi
  // public static BitXor bitXor(String left, Expr right) {
  //   return new BitXor(Field.of(left), right);
  // }
  //
  // /**
  //  * Creates an expression that applies an XOR (^) operation between a field and a constant.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the XOR operation result of field 'flag' and 0xff.
  //  * Function.bitXor("flag", 0xff);
  //  * }</pre>
  //  *
  //  * @param left The left operand field name.
  //  * @param right The right operand constant.
  //  * @return A new {@code Expr} representing the XOR operation.
  //  */
  // @BetaApi
  // public static BitXor bitXor(String left, Object right) {
  //   return new BitXor(Field.of(left), Constant.of(right));
  // }
  //
  // // BitNot
  //
  // /**
  //  * Creates an expression that applies a NOT (~) operation to an expression.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the NOT operation result of field 'flag'.
  //  * Function.bitNot(Field.of("flag"));
  //  * }</pre>
  //  *
  //  * @param operand The operand expression.
  //  * @return A new {@code Expr} representing the NOT operation.
  //  */
  // @BetaApi
  // public static BitNot bitNot(Expr operand) {
  //   return new BitNot(operand);
  // }
  //
  // /**
  //  * Creates an expression that applies a NOT (~) operation to a field.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the NOT operation result of field 'flag'.
  //  * Function.bitNot("flag");
  //  * }</pre>
  //  *
  //  * @param operand The operand field name.
  //  * @return A new {@code Expr} representing the NOT operation.
  //  */
  // @BetaApi
  // public static BitNot bitNot(String operand) {
  //   return new BitNot(Field.of(operand));
  // }
  //
  // // BitLeftShift
  //
  // /**
  //  * Creates an expression that applies a left shift (<<) operation between two expressions.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the left shift operation result from field 'flag' by 'shift' bits.
  //  * Function.bitLeftShift(Field.of("flag"), Field.of("shift"));
  //  * }</pre>
  //  *
  //  * @param left The left operand expression.
  //  * @param right The right operand expression representing the number of bits to shift.
  //  * @return A new {@code Expr} representing the left shift operation.
  //  */
  // @BetaApi
  // public static BitLeftShift bitLeftShift(Expr left, Expr right) {
  //   return new BitLeftShift(left, right);
  // }
  //
  // /**
  //  * Creates an expression that applies a left shift (<<) operation between an expression and a
  //  * constant.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the left shift operation result of field 'flag' by 2 bits.
  //  * Function.bitLeftShift(Field.of("flag"), 2);
  //  * }</pre>
  //  *
  //  * @param left The left operand expression.
  //  * @param right The right operand constant representing the number of bits to shift.
  //  * @return A new {@code Expr} representing the left shift operation.
  //  */
  // @BetaApi
  // public static BitLeftShift bitLeftShift(Expr left, Object right) {
  //   return new BitLeftShift(left, Constant.of(right));
  // }
  //
  // /**
  //  * Creates an expression that applies a left shift (<<) operation between a field and an
  //  * expression.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the left shift operation result from field 'flag' by 'shift' bits.
  //  * Function.bitLeftShift("flag", Field.of("shift"));
  //  * }</pre>
  //  *
  //  * @param left The left operand field name.
  //  * @param right The right operand expression representing the number of bits to shift.
  //  * @return A new {@code Expr} representing the left shift operation.
  //  */
  // @BetaApi
  // public static BitLeftShift bitLeftShift(String left, Expr right) {
  //   return new BitLeftShift(Field.of(left), right);
  // }
  //
  // /**
  //  * Creates an expression that applies a left shift (<<) operation between a field and a
  // constant.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the left shift operation result of field 'flag' by 2 bits.
  //  * Function.bitLeftShift("flag", 2);
  //  * }</pre>
  //  *
  //  * @param left The left operand field name.
  //  * @param right The right operand constant representing the number of bits to shift.
  //  * @return A new {@code Expr} representing the left shift operation.
  //  */
  // @BetaApi
  // public static BitLeftShift bitLeftShift(String left, Object right) {
  //   return new BitLeftShift(Field.of(left), Constant.of(right));
  // }
  //
  // // BitRightShift
  //
  // /**
  //  * Creates an expression that applies a right shift (>>) operation between two expressions.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the right shift operation result from field 'flag' by 'shift' bits.
  //  * Function.bitRightShift(Field.of("flag"), Field.of("shift"));
  //  * }</pre>
  //  *
  //  * @param left The left operand expression.
  //  * @param right The right operand expression representing the number of bits to shift.
  //  * @return A new {@code Expr} representing the right shift operation.
  //  */
  // @BetaApi
  // public static BitRightShift bitRightShift(Expr left, Expr right) {
  //   return new BitRightShift(left, right);
  // }
  //
  // /**
  //  * Creates an expression that applies a right shift (>>) operation between an expression and a
  //  * constant.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the right shift operation result of field 'flag' by 2 bits.
  //  * Function.bitRightShift(Field.of("flag"), 2);
  //  * }</pre>
  //  *
  //  * @param left The left operand expression.
  //  * @param right The right operand constant representing the number of bits to shift.
  //  * @return A new {@code Expr} representing the right shift operation.
  //  */
  // @BetaApi
  // public static BitRightShift bitRightShift(Expr left, Object right) {
  //   return new BitRightShift(left, Constant.of(right));
  // }
  //
  // /**
  //  * Creates an expression that applies a right shift (>>) operation between a field and an
  //  * expression.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the right shift operation result from field 'flag' by 'shift' bits.
  //  * Function.bitRightShift("flag", Field.of("shift"));
  //  * }</pre>
  //  *
  //  * @param left The left operand field name.
  //  * @param right The right operand expression representing the number of bits to shift.
  //  * @return A new {@code Expr} representing the right shift operation.
  //  */
  // @BetaApi
  // public static BitRightShift bitRightShift(String left, Expr right) {
  //   return new BitRightShift(Field.of(left), right);
  // }
  //
  // /**
  //  * Creates an expression that applies a right shift (>>) operation between a field and a
  // constant.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Calculates the right shift operation result of field 'flag' by 2 bits.
  //  * Function.bitRightShift("flag", 2);
  //  * }</pre>
  //  *
  //  * @param left The left operand field name.
  //  * @param right The right operand constant representing the number of bits to shift.
  //  * @return A new {@code Expr} representing the right shift operation.
  //  */
  // @BetaApi
  // public static BitRightShift bitRightShift(String left, Object right) {
  //   return new BitRightShift(Field.of(left), Constant.of(right));
  // }

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
   * Creates an expression that returns the larger value between two expressions, based on
   * Firestore's value type ordering.
   *
   * <p>Firestore's value type ordering is described here: <a
   * href="https://cloud.google.com/firestore/docs/concepts/data-types#value_type_ordering">...</a>
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Returns the larger value between the 'timestamp' field and the current timestamp.
   * Function.logicalMax(Field.of("timestamp"), Function.currentTimestamp());
   * }</pre>
   *
   * @param left The left operand expression.
   * @param right The right operand expression.
   * @return A new {@code Expr} representing the logical max operation.
   */
  @BetaApi
  public static LogicalMax logicalMax(Expr left, Expr right) {
    return new LogicalMax(left, right);
  }

  /**
   * Creates an expression that returns the larger value between an expression and a constant value,
   * based on Firestore's value type ordering.
   *
   * <p>Firestore's value type ordering is described here: <a
   * href="https://cloud.google.com/firestore/docs/concepts/data-types#value_type_ordering">...</a>
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Returns the larger value between the 'value' field and 10.
   * Function.logicalMax(Field.of("value"), 10);
   * }</pre>
   *
   * @param left The left operand expression.
   * @param right The right operand constant.
   * @return A new {@code Expr} representing the logical max operation.
   */
  @BetaApi
  public static LogicalMax logicalMax(Expr left, Object right) {
    return new LogicalMax(left, Constant.of(right));
  }

  /**
   * Creates an expression that returns the larger value between a field and an expression, based on
   * Firestore's value type ordering.
   *
   * <p>Firestore's value type ordering is described here: <a
   * href="https://cloud.google.com/firestore/docs/concepts/data-types#value_type_ordering">...</a>
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Returns the larger value between the 'timestamp' field and the current timestamp.
   * Function.logicalMax("timestamp", Function.currentTimestamp());
   * }</pre>
   *
   * @param left The left operand field name.
   * @param right The right operand expression.
   * @return A new {@code Expr} representing the logical max operation.
   */
  @BetaApi
  public static LogicalMax logicalMax(String left, Expr right) {
    return new LogicalMax(Field.of(left), right);
  }

  /**
   * Creates an expression that returns the larger value between a field and a constant value, based
   * on Firestore's value type ordering.
   *
   * <p>Firestore's value type ordering is described here: <a
   * href="https://cloud.google.com/firestore/docs/concepts/data-types#value_type_ordering">...</a>
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Returns the larger value between the 'value' field and 10.
   * Function.logicalMax("value", 10);
   * }</pre>
   *
   * @param left The left operand field name.
   * @param right The right operand constant.
   * @return A new {@code Expr} representing the logical max operation.
   */
  @BetaApi
  public static LogicalMax logicalMax(String left, Object right) {
    return new LogicalMax(Field.of(left), Constant.of(right));
  }

  /**
   * Creates an expression that returns the smaller value between two expressions, based on
   * Firestore's value type ordering.
   *
   * <p>Firestore's value type ordering is described here: <a
   * href="https://cloud.google.com/firestore/docs/concepts/data-types#value_type_ordering">...</a>
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Returns the smaller value between the 'timestamp' field and the current timestamp.
   * Function.logicalMin(Field.of("timestamp"), Function.currentTimestamp());
   * }</pre>
   *
   * @param left The left operand expression.
   * @param right The right operand expression.
   * @return A new {@code Expr} representing the logical min operation.
   */
  @BetaApi
  public static LogicalMin logicalMin(Expr left, Expr right) {
    return new LogicalMin(left, right);
  }

  /**
   * Creates an expression that returns the smaller value between an expression and a constant
   * value, based on Firestore's value type ordering.
   *
   * <p>Firestore's value type ordering is described here: <a
   * href="https://cloud.google.com/firestore/docs/concepts/data-types#value_type_ordering">...</a>
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Returns the smaller value between the 'value' field and 10.
   * Function.logicalMin(Field.of("value"), 10);
   * }</pre>
   *
   * @param left The left operand expression.
   * @param right The right operand constant.
   * @return A new {@code Expr} representing the logical min operation.
   */
  @BetaApi
  public static LogicalMin logicalMin(Expr left, Object right) {
    return new LogicalMin(left, Constant.of(right));
  }

  /**
   * Creates an expression that returns the smaller value between a field and an expression, based
   * on Firestore's value type ordering.
   *
   * <p>Firestore's value type ordering is described here: <a
   * href="https://cloud.google.com/firestore/docs/concepts/data-types#value_type_ordering">...</a>
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Returns the smaller value between the 'timestamp' field and the current timestamp.
   * Function.logicalMin("timestamp", Function.currentTimestamp());
   * }</pre>
   *
   * @param left The left operand field name.
   * @param right The right operand expression.
   * @return A new {@code Expr} representing the logical min operation.
   */
  @BetaApi
  public static LogicalMin logicalMin(String left, Expr right) {
    return new LogicalMin(Field.of(left), right);
  }

  /**
   * Creates an expression that returns the smaller value between a field and a constant value,
   * based on Firestore's value type ordering.
   *
   * <p>Firestore's value type ordering is described here: <a
   * href="https://cloud.google.com/firestore/docs/concepts/data-types#value_type_ordering">...</a>
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Returns the smaller value between the 'value' field and 10.
   * Function.logicalMin("value", 10);
   * }</pre>
   *
   * @param left The left operand field name.
   * @param right The right operand constant.
   * @return A new {@code Expr} representing the logical min operation.
   */
  @BetaApi
  public static LogicalMin logicalMin(String left, Object right) {
    return new LogicalMin(Field.of(left), Constant.of(right));
  }

  /**
   * Creates an expression that concatenates an array expression with another array.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Combine the 'tags' array with a new array
   * Function.arrayConcat(Field.of("tags"), List.newArrayList("newTag1", "newTag2"));
   * }</pre>
   *
   * @param expr The array expression to concatenate to.
   * @param array The array of constants or expressions to concat with.
   * @return A new {@code Expr} representing the concatenated array.
   */
  @BetaApi
  public static ArrayConcat arrayConcat(Expr expr, List<Object> array) {
    return new ArrayConcat(expr, toExprList(array.toArray()));
  }

  /**
   * Creates an expression that concatenates a field's array value with another array.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Combine the 'tags' array with a new array
   * Function.arrayConcat("tags", List.newArrayList("newTag1", "newTag2"));
   * }</pre>
   *
   * @param field The field name containing array values.
   * @param array The array of constants or expressions to concat with.
   * @return A new {@code Expr} representing the concatenated array.
   */
  @BetaApi
  public static ArrayConcat arrayConcat(String field, List<Object> array) {
    return new ArrayConcat(Field.of(field), toExprList(array.toArray()));
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
  //
  // /**
  //  * Creates an expression that filters elements from an array expression using the given {@link
  //  * FilterCondition} and returns the filtered elements as a new array.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Get items from the 'inventoryPrices' array where the array item is greater than 0
  //  * // Note we use {@link Function#arrayElement} to represent array elements to construct a
  //  * // filtering condition.
  //  * Function.arrayFilter(Field.of("inventoryPrices"), arrayElement().gt(0));
  //  * }</pre>
  //  *
  //  * @param expr The array expression to filter.
  //  * @param filter The {@link FilterCondition} to apply to the array elements.
  //  * @return A new {@code Expr} representing the filtered array.
  //  */
  // @BetaApi
  // static ArrayFilter arrayFilter(Expr expr, FilterCondition filter) {
  //   return new ArrayFilter(expr, filter);
  // }
  //
  // /**
  //  * Creates an expression that filters elements from an array field using the given {@link
  //  * FilterCondition} and returns the filtered elements as a new array.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Get items from the 'inventoryPrices' array where the array item is greater than 0
  //  * // Note we use {@link Function#arrayElement} to represent array elements to construct a
  //  * // filtering condition.
  //  * Function.arrayFilter("inventoryPrices", arrayElement().gt(0));
  //  * }</pre>
  //  *
  //  * @param field The field name containing array values.
  //  * @param filter The {@link FilterCondition} to apply to the array elements.
  //  * @return A new {@code Expr} representing the filtered array.
  //  */
  // @BetaApi
  // static ArrayFilter arrayFilter(String field, FilterCondition filter) {
  //   return new ArrayFilter(Field.of(field), filter);
  // }

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
   * Creates an expression that returns the reversed content of an array.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Get the 'preferences' array in reversed order.
   * Function.arrayReverse(Field.of("preferences"));
   * }</pre>
   *
   * @param expr The array expression to reverse.
   * @return A new {@code Expr} representing the length of the array.
   */
  @BetaApi
  public static ArrayReverse arrayReverse(Expr expr) {
    return new ArrayReverse(expr);
  }

  /**
   * Creates an expression that returns the reversed content of an array.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Get the 'preferences' array in reversed order.
   * Function.arrayReverse("preferences");
   * }</pre>
   *
   * @param field The array field name to reverse.
   * @return A new {@code Expr} representing the length of the array.
   */
  @BetaApi
  public static ArrayReverse arrayReverse(String field) {
    return new ArrayReverse(Field.of(field));
  }

  // /**
  //  * Creates an expression that applies a transformation function to each element in an array
  //  * expression and returns the new array as the result of the evaluation.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Convert all strings in the 'names' array to uppercase
  //  * // Note we use {@link Function#arrayElement} to represent array elements to construct a
  //  * // transforming function.
  //  * Function.arrayTransform(Field.of("names"), arrayElement().toUppercase());
  //  * }</pre>
  //  *
  //  * @param expr The array expression to transform.
  //  * @param transform The {@link Function} to apply to each array element.
  //  * @return A new {@code Expr} representing the transformed array.
  //  */
  // @BetaApi
  // static ArrayTransform arrayTransform(Expr expr, Function transform) {
  //   return new ArrayTransform(expr, transform);
  // }
  //
  // /**
  //  * Creates an expression that applies a transformation function to each element in an array
  // field
  //  * and returns the new array as the result of the evaluation.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Convert all strings in the 'names' array to uppercase
  //  * // Note we use {@link Function#arrayElement} to represent array elements to construct a
  //  * // transforming function.
  //  * Function.arrayTransform("names", arrayElement().toUppercase());
  //  * }</pre>
  //  *
  //  * @param field The field name containing array values.
  //  * @param transform The {@link Function} to apply to each array element.
  //  * @return A new {@code Expr} representing the transformed array.
  //  */
  // @BetaApi
  // static ArrayTransform arrayTransform(String field, Function transform) {
  //   return new ArrayTransform(Field.of(field), transform);
  // }

  /**
   * Creates an expression that calculates the character length of a string expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Get the length of the 'name' field
   * Function.charLength(Field.of("name"));
   * }</pre>
   *
   * @param expr The expression representing the string to calculate the length of.
   * @return A new {@code Expr} representing the length of the string.
   */
  @BetaApi
  public static CharLength charLength(Expr expr) {
    return new CharLength(expr);
  }

  /**
   * Creates an expression that calculates the character length of a string field.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Get the character length of the 'name' field
   * Function.charLength("name");
   * }</pre>
   *
   * @param field The name of the field containing the string.
   * @return A new {@code Expr} representing the length of the string.
   */
  @BetaApi
  public static CharLength charLength(String field) {
    return new CharLength(Field.of(field));
  }

  /**
   * Creates an expression that calculates the byte length of a string expression in its UTF-8 form.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Get the UTF-8 byte length of the 'name' field
   * Function.charLength(Field.of("name"));
   * }</pre>
   *
   * @param expr The expression representing the string to calculate the length of.
   * @return A new {@code Expr} representing the byte length of the string.
   */
  @BetaApi
  public static ByteLength byteLength(Expr expr) {
    return new ByteLength(expr);
  }

  /**
   * Creates an expression that calculates the byte length of a string expression in its UTF-8 form.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Get the UTF-8 byte length of the 'name' field
   * Function.charLength("name");
   * }</pre>
   *
   * @param field The name of the field containing the string.
   * @return A new {@code Expr} representing the byte length of the string.
   */
  @BetaApi
  public static ByteLength byteLength(String field) {
    return new ByteLength(Field.of(field));
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
   * Creates an expression that checks if a string expression contains a specified substring.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'description' field contains "example".
   * Function.regexContains(Field.of("description"), "example");
   * }</pre>
   *
   * @param expr The expression representing the string to perform the comparison on.
   * @param substring The substring to use for the search.
   * @return A new {@code Expr} representing the 'contains' comparison.
   */
  @BetaApi
  public static StrContains strContains(Expr expr, String substring) {
    return new StrContains(expr, Constant.of(substring));
  }

  /**
   * Creates an expression that checks if a string field contains a specified substring.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'description' field contains "example".
   * Function.regexContains("description", "example");
   * }</pre>
   *
   * @param field The name of the field containing the string.
   * @param substring The substring to use for the search.
   * @return A new {@code Expr} representing the 'contains' comparison.
   */
  @BetaApi
  public static StrContains strContains(String field, String substring) {
    return new StrContains(Field.of(field), Constant.of(substring));
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
  public static ToLower toLowercase(Expr expr) {
    return new ToLower(expr);
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
  public static ToLower toLowercase(String field) {
    return new ToLower(Field.of(field));
  }

  /**
   * Creates an expression that converts a string expression to uppercase.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Convert the 'title' field to uppercase
   * Function.toUpper(Field.of("title"));
   * }</pre>
   *
   * @param expr The expression representing the string to convert to uppercase.
   * @return A new {@code Expr} representing the uppercase string.
   */
  @BetaApi
  public static ToUpper toUpper(Expr expr) {
    return new ToUpper(expr);
  }

  /**
   * Creates an expression that converts a string field to uppercase.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Convert the 'title' field to uppercase
   * Function.toUpper("title");
   * }</pre>
   *
   * @param field The name of the field containing the string.
   * @return A new {@code Expr} representing the uppercase string.
   */
  @BetaApi
  public static ToUpper toUpper(String field) {
    return new ToUpper(Field.of(field));
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
   * Creates an expression that reverses a string.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Reverse the 'userInput' field
   * Function.reverse(Field.of("userInput"));
   * }</pre>
   *
   * @param expr The expression representing the string to reverse.
   * @return A new {@code Expr} representing the reversed string.
   */
  @BetaApi
  public static Reverse reverse(Expr expr) {
    return new Reverse(expr);
  }

  /**
   * Creates an expression that reverses a string represented by a field.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Reverse the 'userInput' field
   * Function.reverse("userInput");
   * }</pre>
   *
   * @param field The name of the field representing the string to reverse.
   * @return A new {@code Expr} representing the reversed string.
   */
  @BetaApi
  public static Reverse reverse(String field) {
    return new Reverse(Field.of(field));
  }

  // ReplaceFirst

  /**
   * Creates an expression that replaces the first occurrence of a substring within a string with
   * another substring.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Replace the first occurrence of "hello" with "hi" in the 'message' field
   * Function.replaceFirst(Field.of("message"), "hello", "hi");
   * }</pre>
   *
   * @param value The expression representing the string to perform the replacement on.
   * @param find The substring to search for.
   * @param replace The substring to replace the first occurrence of 'find' with.
   * @return A new {@code Expr} representing the string with the first occurrence replaced.
   */
  @BetaApi
  public static ReplaceFirst replaceFirst(Expr value, String find, String replace) {
    return new ReplaceFirst(value, Constant.of(find), Constant.of(replace));
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
   * Function.replaceFirst(Field.of("message"), Field.of("findField"), Field.of("replaceField"));
   * }</pre>
   *
   * @param value The expression representing the string to perform the replacement on.
   * @param find The expression representing the substring to search for.
   * @param replace The expression representing the substring to replace the first occurrence of
   *     'find' with.
   * @return A new {@code Expr} representing the string with the first occurrence replaced.
   */
  @BetaApi
  public static ReplaceFirst replaceFirst(Expr value, Expr find, Expr replace) {
    return new ReplaceFirst(value, find, replace);
  }

  /**
   * Creates an expression that replaces the first occurrence of a substring within a string
   * represented by a field with another substring.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Replace the first occurrence of "hello" with "hi" in the 'message' field
   * Function.replaceFirst("message", "hello", "hi");
   * }</pre>
   *
   * @param field The name of the field representing the string to perform the replacement on.
   * @param find The substring to search for.
   * @param replace The substring to replace the first occurrence of 'find' with.
   * @return A new {@code Expr} representing the string with the first occurrence replaced.
   */
  @BetaApi
  public static ReplaceFirst replaceFirst(String field, String find, String replace) {
    return new ReplaceFirst(Field.of(field), Constant.of(find), Constant.of(replace));
  }

  // ReplaceAll

  /**
   * Creates an expression that replaces all occurrences of a substring within a string with another
   * substring.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Replace all occurrences of "hello" with "hi" in the 'message' field
   * Function.replaceAll(Field.of("message"), "hello", "hi");
   * }</pre>
   *
   * @param value The expression representing the string to perform the replacement on.
   * @param find The substring to search for.
   * @param replace The substring to replace all occurrences of 'find' with.
   * @return A new {@code Expr} representing the string with all occurrences replaced.
   */
  @BetaApi
  public static ReplaceAll replaceAll(Expr value, String find, String replace) {
    return new ReplaceAll(value, Constant.of(find), Constant.of(replace));
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
   * Function.replaceAll(Field.of("message"), Field.of("findField"), Field.of("replaceField"));
   * }</pre>
   *
   * @param value The expression representing the string to perform the replacement on.
   * @param find The expression representing the substring to search for.
   * @param replace The expression representing the substring to replace all occurrences of 'find'
   *     with.
   * @return A new {@code Expr} representing the string with all occurrences replaced.
   */
  @BetaApi
  public static ReplaceAll replaceAll(Expr value, Expr find, Expr replace) {
    return new ReplaceAll(value, find, replace);
  }

  /**
   * Creates an expression that replaces all occurrences of a substring within a string represented
   * by a field with another substring.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Replace all occurrences of "hello" with "hi" in the 'message' field
   * Function.replaceAll("message", "hello", "hi");
   * }</pre>
   *
   * @param field The name of the field representing the string to perform the replacement on.
   * @param find The substring to search for.
   * @param replace The substring to replace all occurrences of 'find' with.
   * @return A new {@code Expr} representing the string with all occurrences replaced.
   */
  @BetaApi
  public static ReplaceAll replaceAll(String field, String find, String replace) {
    return new ReplaceAll(Field.of(field), Constant.of(find), Constant.of(replace));
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
    return new Count(expr);
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
    return new Count(Field.of(field));
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
  public static CountAll countAll() {
    return new CountAll();
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
   * Calculates the dot product between two vector expressions.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the dot product between two document vectors: 'docVector1' and 'docVector2'
   * Function.dotProduct(Field.of("docVector1"), Field.of("docVector2"));
   * }</pre>
   *
   * @param expr The first vector (represented as an Expr) to calculate dot product with.
   * @param other The other vector (represented as an Expr) to calculate dot product with.
   * @return A new {@code Expr} representing the dot product between the two vectors.
   */
  @BetaApi
  public static DotProduct dotProduct(Expr expr, Expr other) {
    return new DotProduct(expr, other);
  }

  /**
   * Calculates the dot product between a vector expression and a double array.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the dot product between a feature vector and a target vector
   * Function.dotProduct(Field.of("features"), new double[] {0.5, 0.8, 0.2});
   * }</pre>
   *
   * @param expr The first vector (represented as an Expr) to calculate dot product with.
   * @param other The other vector (represented as an Expr) to calculate dot product with.
   * @return A new {@code Expr} representing the dot product between the two vectors.
   */
  @BetaApi
  public static DotProduct dotProduct(Expr expr, double[] other) {
    return new DotProduct(expr, Constant.vector(other));
  }

  /**
   * Calculates the dot product between a field's vector value and a vector expression.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the dot product between two document vectors: 'docVector1' and 'docVector2'
   * Function.dotProduct("docVector1", Field.of("docVector2"));
   * }</pre>
   *
   * @param field The name of the field containing the first vector.
   * @param other The other vector (represented as an Expr) to calculate dot product with.
   * @return A new {@code Expr} representing the dot product distance between the two vectors.
   */
  @BetaApi
  public static DotProduct dotProduct(String field, Expr other) {
    return new DotProduct(Field.of(field), other);
  }

  /**
   * Calculates the dot product between a field's vector value and a double array.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the dot product between a feature vector and a target vector
   * Function.dotProduct("features", new double[] {0.5, 0.8, 0.2});
   * }</pre>
   *
   * @param field The name of the field containing the first vector.
   * @param other The other vector (represented as an Expr) to calculate dot product with.
   * @return A new {@code Expr} representing the dot product distance between the two vectors.
   */
  @BetaApi
  public static DotProduct dotProduct(String field, double[] other) {
    return new DotProduct(Field.of(field), Constant.vector(other));
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
   * Creates an expression that calculates the length of a Firestore Vector.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Get the vector length (dimension) of the field 'embedding'.
   * Function.vectorLength(Field.of("embedding"));
   * }</pre>
   *
   * @param expr The expression representing the Firestore Vector.
   * @return A new {@code Expr} representing the length of the array.
   */
  @BetaApi
  public static VectorLength vectorLength(Expr expr) {
    return new VectorLength(expr);
  }

  /**
   * Creates an expression that calculates the length of a Firestore Vector represented by a field.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Get the vector length (dimension) of the field 'embedding'.
   * Function.vectorLength("embedding");
   * }</pre>
   *
   * @param field The name of the field representing the Firestore Vector.
   * @return A new {@code Expr} representing the length of the array.
   */
  @BetaApi
  public static VectorLength vectorLength(String field) {
    return new VectorLength(Field.of(field));
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
   * Function.timestampToUnixMicros(Field.of("timestamp"));
   * }</pre>
   *
   * @param expr The expression representing the timestamp to convert.
   * @return A new {@code Expr} representing the number of microseconds since the epoch.
   */
  @BetaApi
  public static TimestampToUnixMicros timestampToUnixMicros(Expr expr) {
    return new TimestampToUnixMicros(expr);
  }

  /**
   * Creates an expression that converts a timestamp represented by a field to the number of
   * microseconds since the epoch (1970-01-01 00:00:00 UTC).
   *
   * <p>Truncates higher levels of precision by rounding down to the beginning of the microsecond.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Convert the 'timestamp' field to microseconds since the epoch.
   * Function.timestampToUnixMicros("timestamp");
   * }</pre>
   *
   * @param field The name of the field representing the timestamp to convert.
   * @return A new {@code Expr} representing the number of microseconds since the epoch.
   */
  @BetaApi
  public static TimestampToUnixMicros timestampToUnixMicros(String field) {
    return new TimestampToUnixMicros(Field.of(field));
  }

  /**
   * Creates an expression that converts a number of microseconds since the epoch (1970-01-01
   * 00:00:00 UTC) to a timestamp.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Convert the 'microseconds' field to a timestamp.
   * Function.unixMicrosToTimestamp(Field.of("microseconds"));
   * }</pre>
   *
   * @param expr The expression representing the number of microseconds since the epoch.
   * @return A new {@code Expr} representing the timestamp.
   */
  @BetaApi
  public static UnixMicrosToTimestamp unixMicrosToTimestamp(Expr expr) {
    return new UnixMicrosToTimestamp(expr);
  }

  /**
   * Creates an expression that converts a number of microseconds since the epoch (1970-01-01
   * 00:00:00 UTC), represented by a field, to a timestamp.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Convert the 'microseconds' field to a timestamp.
   * Function.unixMicrosToTimestamp("microseconds");
   * }</pre>
   *
   * @param field The name of the field representing the number of microseconds since the epoch.
   * @return A new {@code Expr} representing the timestamp.
   */
  @BetaApi
  public static UnixMicrosToTimestamp unixMicrosToTimestamp(String field) {
    return new UnixMicrosToTimestamp(Field.of(field));
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
   * Function.timestampToUnixMillis(Field.of("timestamp"));
   * }</pre>
   *
   * @param expr The expression representing the timestamp to convert.
   * @return A new {@code Expr} representing the number of milliseconds since the epoch.
   */
  @BetaApi
  public static TimestampToUnixMillis timestampToUnixMillis(Expr expr) {
    return new TimestampToUnixMillis(expr);
  }

  /**
   * Creates an expression that converts a timestamp represented by a field to the number of
   * milliseconds since the epoch (1970-01-01 00:00:00 UTC).
   *
   * <p>Truncates higher levels of precision by rounding down to the beginning of the millisecond.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Convert the 'timestamp' field to milliseconds since the epoch.
   * Function.timestampToUnixMillis("timestamp");
   * }</pre>
   *
   * @param field The name of the field representing the timestamp to convert.
   * @return A new {@code Expr} representing the number of milliseconds since the epoch.
   */
  @BetaApi
  public static TimestampToUnixMillis timestampToUnixMillis(String field) {
    return new TimestampToUnixMillis(Field.of(field));
  }

  /**
   * Creates an expression that converts a number of milliseconds since the epoch (1970-01-01
   * 00:00:00 UTC) to a timestamp.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Convert the 'milliseconds' field to a timestamp.
   * Function.unixMillisToTimestamp(Field.of("milliseconds"));
   * }</pre>
   *
   * @param expr The expression representing the number of milliseconds since the epoch.
   * @return A new {@code Expr} representing the timestamp.
   */
  @BetaApi
  public static UnixMillisToTimestamp unixMillisToTimestamp(Expr expr) {
    return new UnixMillisToTimestamp(expr);
  }

  /**
   * Creates an expression that converts a number of milliseconds since the epoch (1970-01-01
   * 00:00:00 UTC), represented by a field, to a timestamp.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Convert the 'milliseconds' field to a timestamp.
   * Function.unixMillisToTimestamp("milliseconds");
   * }</pre>
   *
   * @param field The name of the field representing the number of milliseconds since the epoch.
   * @return A new {@code Expr} representing the timestamp.
   */
  @BetaApi
  public static UnixMillisToTimestamp unixMillisToTimestamp(String field) {
    return new UnixMillisToTimestamp(Field.of(field));
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
   * Function.timestampToUnixSeconds(Field.of("timestamp"));
   * }</pre>
   *
   * @param expr The expression representing the timestamp to convert.
   * @return A new {@code Expr} representing the number of seconds since the epoch.
   */
  @BetaApi
  public static TimestampToUnixSeconds timestampToUnixSeconds(Expr expr) {
    return new TimestampToUnixSeconds(expr);
  }

  /**
   * Creates an expression that converts a timestamp represented by a field to the number of seconds
   * since the epoch (1970-01-01 00:00:00 UTC).
   *
   * <p>Truncates higher levels of precision by rounding down to the beginning of the second.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Convert the 'timestamp' field to seconds since the epoch.
   * Function.timestampToUnixSeconds("timestamp");
   * }</pre>
   *
   * @param field The name of the field representing the timestamp to convert.
   * @return A new {@code Expr} representing the number of seconds since the epoch.
   */
  @BetaApi
  public static TimestampToUnixSeconds timestampToUnixSeconds(String field) {
    return new TimestampToUnixSeconds(Field.of(field));
  }

  /**
   * Creates an expression that converts a number of seconds since the epoch (1970-01-01 00:00:00
   * UTC) to a timestamp.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Convert the 'seconds' field to a timestamp.
   * Function.unixSecondsToTimestamp(Field.of("seconds"));
   * }</pre>
   *
   * @param expr The expression representing the number of seconds since the epoch.
   * @return A new {@code Expr} representing the timestamp.
   */
  @BetaApi
  public static UnixSecondsToTimestamp unixSecondsToTimestamp(Expr expr) {
    return new UnixSecondsToTimestamp(expr);
  }

  /**
   * Creates an expression that converts a number of seconds since the epoch (1970-01-01 00:00:00
   * UTC), represented by a field, to a timestamp.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Convert the 'seconds' field to a timestamp.
   * Function.unixSecondsToTimestamp("seconds");
   * }</pre>
   *
   * @param field The name of the field representing the number of seconds since the epoch.
   * @return A new {@code Expr} representing the timestamp.
   */
  @BetaApi
  public static UnixSecondsToTimestamp unixSecondsToTimestamp(String field) {
    return new UnixSecondsToTimestamp(Field.of(field));
  }

  /**
   * Creates an expression that adds a specified amount of time to a timestamp.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Add a duration specified by the 'unit' and 'amount' fields to the 'timestamp' field.
   * Function.timestampAdd(Field.of("timestamp"), Field.of("unit"), Field.of("amount"));
   * }</pre>
   *
   * @param timestamp The expression representing the timestamp.
   * @param unit The expression evaluating to the unit of time to add, must be one of 'microsecond',
   *     'millisecond', 'second', 'minute', 'hour', 'day'.
   * @param amount The expression representing the amount of time to add.
   * @return A new {@code Expr} representing the resulting timestamp.
   */
  @BetaApi
  public static TimestampAdd timestampAdd(Expr timestamp, Expr unit, Expr amount) {
    return new TimestampAdd(timestamp, unit, amount);
  }

  /**
   * Creates an expression that adds a specified amount of time to a timestamp represented by a
   * field.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Add a duration specified by the 'unit' and 'amount' fields to the 'timestamp' field.
   * Function.timestampAdd("timestamp", Field.of("unit"), Field.of("amount"));
   * }</pre>
   *
   * @param field The name of the field representing the timestamp.
   * @param unit The expression evaluating to the unit of time to add, must be one of 'microsecond',
   *     'millisecond', 'second', 'minute', 'hour', 'day'.
   * @param amount The expression representing the amount of time to add.
   * @return A new {@code Expr} representing the resulting timestamp.
   */
  @BetaApi
  public static TimestampAdd timestampAdd(String field, Expr unit, Expr amount) {
    return new TimestampAdd(Field.of(field), unit, amount);
  }

  /**
   * Creates an expression that adds a specified amount of time to a timestamp.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Add 1.5 days to the 'timestamp' field.
   * Function.timestampAdd(Field.of("timestamp"), "day", 1.5);
   * }</pre>
   *
   * @param timestamp The expression representing the timestamp.
   * @param unit The unit of time to add, must be one of 'microsecond', 'millisecond', 'second',
   *     'minute', 'hour', 'day'.
   * @param amount The amount of time to add.
   * @return A new {@code Expr} representing the resulting timestamp.
   */
  @BetaApi
  public static TimestampAdd timestampAdd(Expr timestamp, String unit, Double amount) {
    return new TimestampAdd(timestamp, Constant.of(unit), Constant.of(amount));
  }

  /**
   * Creates an expression that adds a specified amount of time to a timestamp represented by a
   * field.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Add 1.5 days to the 'timestamp' field.
   * Function.timestampAdd("timestamp", "day", 1.5);
   * }</pre>
   *
   * @param field The name of the field representing the timestamp.
   * @param unit The unit of time to add, must be one of 'microsecond', 'millisecond', 'second',
   *     'minute', 'hour', 'day'.
   * @param amount The amount of time to add.
   * @return A new {@code Expr} representing the resulting timestamp.
   */
  @BetaApi
  public static TimestampAdd timestampAdd(String field, String unit, Double amount) {
    return new TimestampAdd(Field.of(field), Constant.of(unit), Constant.of(amount));
  }

  /**
   * Creates an expression that subtracts a specified amount of time from a timestamp.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Subtract a duration specified by the 'unit' and 'amount' fields from the 'timestamp' field.
   * Function.timestampSub(Field.of("timestamp"), Field.of("unit"), Field.of("amount"));
   * }</pre>
   *
   * @param timestamp The expression representing the timestamp.
   * @param unit The expression evaluating to the unit of time to subtract, must be one of
   *     'microsecond', 'millisecond', 'second', 'minute', 'hour', 'day'.
   * @param amount The expression representing the amount of time to subtract.
   * @return A new {@code Expr} representing the resulting timestamp.
   */
  @BetaApi
  public static TimestampSub timestampSub(Expr timestamp, Expr unit, Expr amount) {
    return new TimestampSub(timestamp, unit, amount);
  }

  /**
   * Creates an expression that subtracts a specified amount of time from a timestamp represented by
   * a field.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Subtract a duration specified by the 'unit' and 'amount' fields from the 'timestamp' field.
   * Function.timestampSub("timestamp", Field.of("unit"), Field.of("amount"));
   * }</pre>
   *
   * @param field The name of the field representing the timestamp.
   * @param unit The expression evaluating to the unit of time to subtract, must be one of
   *     'microsecond', 'millisecond', 'second', 'minute', 'hour', 'day'.
   * @param amount The expression representing the amount of time to subtract.
   * @return A new {@code Expr} representing the resulting timestamp.
   */
  @BetaApi
  public static TimestampSub timestampSub(String field, Expr unit, Expr amount) {
    return new TimestampSub(Field.of(field), unit, amount);
  }

  /**
   * Creates an expression that subtracts a specified amount of time from a timestamp.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Subtract 2.5 hours from the 'timestamp' field.
   * Function.timestampSub(Field.of("timestamp"), "hour", 2.5);
   * }</pre>
   *
   * @param timestamp The expression representing the timestamp.
   * @param unit The unit of time to subtract, must be one of 'microsecond', 'millisecond',
   *     'second', 'minute', 'hour', 'day'.
   * @param amount The amount of time to subtract.
   * @return A new {@code Expr} representing the resulting timestamp.
   */
  @BetaApi
  public static TimestampSub timestampSub(Expr timestamp, String unit, Double amount) {
    return new TimestampSub(timestamp, Constant.of(unit), Constant.of(amount));
  }

  /**
   * Creates an expression that subtracts a specified amount of time from a timestamp represented by
   * a field.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Subtract 2.5 hours from the 'timestamp' field.
   * Function.timestampSub("timestamp", "hour", 2.5);
   * }</pre>
   *
   * @param field The name of the field representing the timestamp.
   * @param unit The unit of time to subtract, must be one of 'microsecond', 'millisecond',
   *     'second', 'minute', 'hour', 'day'.
   * @param amount The amount of time to subtract.
   * @return A new {@code Expr} representing the resulting timestamp.
   */
  @BetaApi
  public static TimestampSub timestampSub(String field, String unit, Double amount) {
    return new TimestampSub(Field.of(field), Constant.of(unit), Constant.of(amount));
  }

  // /**
  //  * Returns an expression that represents an array element within an {@link ArrayFilter} or
  // {@link
  //  * ArrayTransform} expression.
  //  *
  //  * <p>Example:
  //  *
  //  * <pre>{@code
  //  * // Get items from the 'inventoryPrices' array where the array item is greater than 0
  //  * Function.arrayFilter(Field.of("inventoryPrices"), Function.arrayElement().gt(0));
  //  * }</pre>
  //  *
  //  * @return A new {@code Expr} representing an array element.
  //  */
  // @BetaApi
  // public static ArrayElement arrayElement() {
  //   return new ArrayElement();
  // }

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
    return new Function(name, ImmutableList.copyOf(params));
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
