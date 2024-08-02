package com.google.cloud.firestore.pipeline.expressions;

import static com.google.cloud.firestore.pipeline.expressions.FunctionUtils.toExprList;

import com.google.api.core.BetaApi;
import java.util.Arrays;

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
   * Creates an expression that concatenates an array expression with one or more other arrays.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Combine the 'items' array with another array field.
   * Field.of("items").arrayConcat(Field.of("otherItems"));
   * }</pre>
   *
   * @param elements The array expressions to concatenate.
   * @return A new {@code Expr} representing the concatenated array.
   */
  @BetaApi
  default ArrayConcat arrayConcat(Expr... elements) {
    return new ArrayConcat(this, Arrays.asList(elements));
  }

  /**
   * Creates an expression that concatenates an array expression with one or more other arrays.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Combine the 'tags' array with a new array and an array field
   * Field.of("tags").arrayConcat(Arrays.asList("newTag1", "newTag2"), Field.of("otherTag"));
   * }</pre>
   *
   * @param elements The array expressions or values to concatenate.
   * @return A new {@code Expr} representing the concatenated array.
   */
  @BetaApi
  default ArrayConcat arrayConcat(Object... elements) {
    return new ArrayConcat(this, toExprList(elements));
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
   * Creates an expression that filters elements from an array using the given {@link
   * FilterCondition} and returns the filtered elements as a new array.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Get items from the 'inventoryPrices' array where the array item is greater than 0
   * // Note we use {@link Function#arrayElement} to represent array elements to construct a
   * // filtering condition.
   * Field.of("inventoryPrices").arrayFilter(arrayElement().gt(0));
   * }</pre>
   *
   * @param filter The {@link FilterCondition} to apply to the array elements.
   * @return A new {@code Expr} representing the filtered array.
   */
  @BetaApi
  default ArrayFilter arrayFilter(FilterCondition filter) {
    return new ArrayFilter(this, filter);
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
   * Creates an expression that applies a transformation function to each element in an array and
   * returns the new array as the result of the evaluation.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Convert all strings in the 'names' array to uppercase
   * Field.of("names").arrayTransform(arrayElement().toUppercase());
   * }</pre>
   *
   * @param transform The {@link Function} to apply to each array element.
   * @return A new {@code Expr} representing the transformed array.
   */
  @BetaApi
  default ArrayTransform arrayTransform(Function transform) {
    return new ArrayTransform(this, transform);
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

  /**
   * Creates an expression that checks if this expression evaluates to null.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Check if the 'optionalField' is null
   * Field.of("optionalField").isNull();
   * }</pre>
   *
   * @return A new {@code Expr} representing the null check.
   */
  @BetaApi
  default IsNull isNull() {
    return new IsNull(this);
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
    return new Count(this, false);
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
   * Creates an expression that calculates the length of a string.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Get the length of the 'name' field
   * Field.of("name").length();
   * }</pre>
   *
   * @return A new {@code Expr} representing the length of the string.
   */
  @BetaApi
  default Length length() {
    return new Length(this);
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
  default ToLowercase toLowercase() {
    return new ToLowercase(this);
  }

  /**
   * Creates an expression that converts a string to uppercase.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Convert the 'title' field to uppercase
   * Field.of("title").toUpperCase();
   * }</pre>
   *
   * @return A new {@code Expr} representing the uppercase string.
   */
  @BetaApi
  default ToUppercase toUppercase() {
    return new ToUppercase(this);
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
    return new CosineDistance(this, Constant.ofVector(other));
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
    return new EuclideanDistance(this, Constant.ofVector(other));
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
   * Calculates the dot product distance between two vectors.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the dot product distance between a feature vector and a target vector
   * Field.of("features").dotProductDistance(new double[] {0.5, 0.8, 0.2});
   * }</pre>
   *
   * @param other The other vector (as an array of doubles) to compare against.
   * @return A new {@code Expr} representing the dot product distance between the two vectors.
   */
  @BetaApi
  default DotProductDistance dotProductDistance(double[] other) {
    return new DotProductDistance(this, Constant.ofVector(other));
  }

  /**
   * Calculates the dot product distance between two vectors.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Calculate the dot product distance between two document vectors: 'docVector1' and 'docVector2'
   * Field.of("docVector1").dotProductDistance(Field.of("docVector2"));
   * }</pre>
   *
   * @param other The other vector (represented as an Expr) to compare against.
   * @return A new {@code Expr} representing the dot product distance between the two vectors.
   */
  @BetaApi
  default DotProductDistance dotProductDistance(Expr other) {
    return new DotProductDistance(this, other);
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
    return new ExprWithAlias(this, alias);
  }
}
