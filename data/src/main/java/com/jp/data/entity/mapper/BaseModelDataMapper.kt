package com.jp.data.entity.mapper

import java.util.ArrayList

abstract class BaseModelDataMapper<E, T> {
    abstract fun transform(source: E): T

    /**
     * Transform a List of [E] into a List of [T].
     *
     * @param source Object List to be transformed.
     * @return [T] if valid [E] otherwise null.
     */
    fun transform(source: List<E>): List<T> {
        val destination = ArrayList<T>()
        var out: T?
        for (`in` in source) {
            out = transform(`in`)
            if (out != null) {
                destination.add(out)
            }
        }
        return destination
    }

    /**
     * Transform a [T] into an [E].
     *
     * @param source Object to be transformed.
     * @return [E] if valid [T] otherwise null.
     */
    abstract fun inverseTransform(source: T): E

    /**
     * Transform a List of [T] into a List of [E].
     *
     * @param source Object List to be transformed.
     * @return [E] if valid [T] otherwise null.
     */
    fun inverseTransform(source: List<T>): List<E> {
        val destination = ArrayList<E>()
        var out: E?
        for (`in` in source) {
            out = inverseTransform(`in`)
            if (out != null) {
                destination.add(out)
            }
        }
        return destination
    }
}