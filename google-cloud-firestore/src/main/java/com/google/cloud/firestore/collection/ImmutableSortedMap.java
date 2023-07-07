/*
 * Copyright 2017 Google LLC
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

package com.google.cloud.firestore.collection;

import com.google.api.core.InternalApi;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// Note: This package is copied from https://github.com/firebase/firebase-admin-java/tree/master/src
// /main/java/com/google/firebase/database/collection
@InternalApi
public abstract class ImmutableSortedMap<K, V> implements Iterable<Map.Entry<K, V>> {

  /**
   * Checks if the map contains the specified key.
   *
   * @param key the key to check
   * @return {@code true} if the map contains the key, {@code false} otherwise
   */
  public abstract boolean containsKey(K key);

  /**
   * Retrieves the value associated with the specified key.
   *
   * @param key the key to retrieve the value for
   * @return the value associated with the key, or {@code null} if the key is not present in the map
   */
  public abstract V get(K key);

  /**
   * Removes the specified key and its associated value from the map.
   *
   * @param key the key to remove
   * @return a new {@code ImmutableSortedMap} instance with the specified key and value removed
   */
  public abstract ImmutableSortedMap<K, V> remove(K key);

  /**
   * Inserts a new key-value pair into the map.
   *
   * @param key the key to insert
   * @param value the value associated with the key
   * @return a new {@code ImmutableSortedMap} instance with the specified key and value inserted
   */
  public abstract ImmutableSortedMap<K, V> insert(K key, V value);

  /**
   * Retrieves the minimum key in the map.
   *
   * @return the minimum key in the map
   */
  public abstract K getMinKey();

  /**
   * Retrieves the maximum key in the map.
   *
   * @return the maximum key in the map
   */
  public abstract K getMaxKey();

  /**
   * Returns the size of the map.
   *
   * @return the number of key-value pairs in the map
   */
  public abstract int size();

  /**
   * Checks if the map is empty.
   *
   * @return {@code true} if the map is empty, {@code false} otherwise
   */
  public abstract boolean isEmpty();

  /**
   * Performs an in-order traversal of the map and applies the specified visitor to each node.
   *
   * @param visitor the visitor to apply to each node in the map
   */
  public abstract void inOrderTraversal(LLRBNode.NodeVisitor<K, V> visitor);

  /**
   * Returns an iterator over the entries in the map.
   *
   * @return an iterator over the entries in the map
   */
  public abstract Iterator<Map.Entry<K, V>> iterator();

  /**
   * Returns an iterator over the entries in the map starting from the specified key.
   *
   * @param key the key to start the iterator from
   * @return an iterator over the entries in the map starting from the specified key
   */
  public abstract Iterator<Map.Entry<K, V>> iteratorFrom(K key);

  /**
   * Returns a reverse iterator over the entries in the map starting from the specified key.
   *
   * @param key the key to start the reverse iterator from
   * @return a reverse iterator over the entries in the map starting from the specified key
   */
  public abstract Iterator<Map.Entry<K, V>> reverseIteratorFrom(K key);

  /**
   * Returns a reverse iterator over the entries in the map.
   *
   * @return a reverse iterator over the entries in the map
   */
  public abstract Iterator<Map.Entry<K, V>> reverseIterator();

  /**
   * Retrieves the predecessor key of the specified key.
   *
   * @param key the key to find the predecessor for
   * @return the predecessor key of the specified key, or {@code null} if there is no predecessor
   */
  public abstract K getPredecessorKey(K key);

  /**
   * Retrieves the successor key of the specified key.
   *
   * @param key the key to find the successor for
   * @return the successor key of the specified key, or {@code null} if there is no successor
   */
  public abstract K getSuccessorKey(K key);

  /**
   * Returns the index of the specified key in the map.
   *
   * @param key the key to find the index for
   * @return the index of the specified key, or {@code -1} if the key is not present in the map
   */
  public abstract int indexOf(K key);

  /**
   * Returns the comparator used to sort the keys in the map.
   *
   * @return the comparator used to sort the keys
   */
  public abstract Comparator<K> getComparator();

  /**
   * Checks if this map is equal to the specified object.
   *
   * @param o the object to compare to
   * @return {@code true} if this map is equal to the specified object, {@code false} otherwise
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ImmutableSortedMap)) return false;

    ImmutableSortedMap<K, V> that = (ImmutableSortedMap) o;

    if (!this.getComparator().equals(that.getComparator())) return false;
    if (this.size() != that.size()) return false;

    Iterator<Map.Entry<K, V>> thisIterator = this.iterator();
    Iterator<Map.Entry<K, V>> thatIterator = that.iterator();
    while (thisIterator.hasNext()) {
      if (!thisIterator.next().equals(thatIterator.next())) return false;
    }

    return true;
  }

  /**
   * Returns the hash code for this map.
   *
   * @return the hash code for this map
   */
  @Override
  public int hashCode() {
    int result = this.getComparator().hashCode();
    for (Map.Entry<K, V> entry : this) {
      result = 31 * result + entry.hashCode();
    }

    return result;
  }

  /**
   * Returns a string representation of this map.
   *
   * @return a string representation of this map
   */
  public String toString() {
    StringBuilder b = new StringBuilder();
    b.append(this.getClass().getSimpleName());
    b.append("{");
    boolean first = true;
    for (Map.Entry<K, V> entry : this) {
      if (first) first = false;
      else b.append(", ");
      b.append("(");
      b.append(entry.getKey());
      b.append("=>");
      b.append(entry.getValue());
      b.append(")");
    }
    b.append("};");
    return b.toString();
  }

  public static class Builder {
    /**
     * The size threshold where we use a tree backed sorted map instead of an array backed sorted
     * map. This is a more or less arbitrary chosen value, that was chosen to be large enough to fit
     * most of object kind of Database data, but small enough to not notice degradation in
     * performance for inserting and lookups. Feel free to empirically determine this constant, but
     * don't expect much gain in real world performance.
     */
    static final int ARRAY_TO_RB_TREE_SIZE_THRESHOLD = 25;

    public static <K, V> ImmutableSortedMap<K, V> emptyMap(Comparator<K> comparator) {
      return new ArraySortedMap<>(comparator);
    }

    public interface KeyTranslator<C, D> {
      D translate(C key);
    }

    private static final KeyTranslator IDENTITY_TRANSLATOR = key -> key;

    @SuppressWarnings("unchecked")
    public static <A> KeyTranslator<A, A> identityTranslator() {
      return IDENTITY_TRANSLATOR;
    }

    public static <A, B> ImmutableSortedMap<A, B> fromMap(
        Map<A, B> values, Comparator<A> comparator) {
      if (values.size() < ARRAY_TO_RB_TREE_SIZE_THRESHOLD) {
        return ArraySortedMap.fromMap(values, comparator);
      } else {
        return RBTreeSortedMap.fromMap(values, comparator);
      }
    }

    public static <A, B, C> ImmutableSortedMap<A, C> buildFrom(
        List<A> keys,
        Map<B, C> values,
        ImmutableSortedMap.Builder.KeyTranslator<A, B> translator,
        Comparator<A> comparator) {
      if (keys.size() < ARRAY_TO_RB_TREE_SIZE_THRESHOLD) {
        return ArraySortedMap.buildFrom(keys, values, translator, comparator);
      } else {
        return RBTreeSortedMap.buildFrom(keys, values, translator, comparator);
      }
    }
  }
}
