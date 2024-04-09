package com.google.cloud.firestore

import com.google.cloud.Timestamp
import com.google.firestore.v1.Value
import java.util.Date
import javax.annotation.Nonnull

data class PipelineResult internal constructor(
  private val rpcContext: FirestoreRpcContext<*>?,
  val reference: DocumentReference?,
  val protoFields: Map<String, Value>?,
  val readTime: Timestamp?,
  val updateTime: Timestamp?,
  val createTime: Timestamp?
) {
  constructor() : this(null, null, null, null, null, null)

  val id: String?
    get() = reference?.id

  fun valid(): Boolean {
    return protoFields != null
  }

  val data: Map<String, Any>?
    /**
     * Returns the fields of the document as a Map or null if the document doesn't exist. Field values
     * will be converted to their native Java representation.
     *
     * @return The fields of the document as a Map or null if the document doesn't exist.
     */
    get() {
      if (protoFields == null) {
        return null
      }

      val decodedFields: MutableMap<String, Any> = HashMap()
      for ((key, value) in protoFields) {
        val decodedValue = UserDataConverter.decodeValue(rpcContext, value)
        decodedFields[key] = decodedValue
      }
      return decodedFields
    }

  /**
   * Returns the contents of the result converted to a POJO or null if the document doesn't exist.
   *
   * @param valueType The Java class to create
   * @return The contents of the result in an object of type T or null if the document doesn't
   * exist.
   */
  fun <T> toObject(@Nonnull valueType: Class<T>): T? {
    val data = data
    return if (data == null) null else CustomClassMapper.convertToCustomClass(
      data, valueType,
      reference
    )
  }

  /**
   * Returns whether or not the field exists in the result. Returns false if the result does not
   * exist.
   *
   * @param field the path to the field.
   * @return true iff the field exists.
   */
  fun contains(field: String): Boolean {
    return contains(FieldPath.fromDotSeparatedString(field))
  }

  /**
   * Returns whether or not the field exists in the result. Returns false if the result is invalid.
   *
   * @param fieldPath the path to the field.
   * @return true iff the field exists.
   */
  fun contains(fieldPath: FieldPath): Boolean {
    return this.extractField(fieldPath) != null
  }

  fun get(field: String): Any? {
    return get(FieldPath.fromDotSeparatedString(field))
  }

  fun <T> get(field: String?, valueType: Class<T>): T? {
    return get(FieldPath.fromDotSeparatedString(field), valueType)
  }

  fun get(fieldPath: FieldPath): Any? {
    val value = extractField(fieldPath) ?: return null

    return UserDataConverter.decodeValue(rpcContext, value)
  }

  fun <T> get(fieldPath: FieldPath, valueType: Class<T>): T? {
    val data = get(fieldPath)
    return if (data == null) null else CustomClassMapper.convertToCustomClass(
      data, valueType,
      reference
    )
  }

  fun extractField(fieldPath: FieldPath): Value? {
    var value: Value? = null

    if (protoFields != null) {
      val components: Iterator<String> = fieldPath.segments.iterator()
      value = protoFields[components.next()]

      while (value != null && components.hasNext()) {
        if (value.valueTypeCase != Value.ValueTypeCase.MAP_VALUE) {
          return null
        }
        value = value.mapValue.getFieldsOrDefault(components.next(), null)
      }
    }

    return value
  }

  fun getBoolean(field: String): Boolean? {
    return get(field) as Boolean?
  }

  fun getDouble(field: String): Double? {
    val number = get(field) as Number?
    return number?.toDouble()
  }

  fun getString(field: String): String? {
    return get(field) as String?
  }

  fun getLong(field: String): Long? {
    val number = get(field) as Number?
    return number?.toLong()
  }

  fun getDate(field: String): Date? {
    val timestamp = getTimestamp(field)
    return timestamp?.toDate()
  }

  fun getTimestamp(field: String): Timestamp? {
    return get(field) as Timestamp?
  }

  fun getBlob(field: String): Blob? {
    return get(field) as Blob?
  }

  fun getGeoPoint(field: String): GeoPoint? {
    return get(field) as GeoPoint?
  }

  val isEmpty: Boolean
    get() = protoFields == null || protoFields.isEmpty()
}
