package com.prototype.dialer.core.extension

import kotlin.reflect.KClass

/**
 * Расширение, предоставляющее свойство TAG в любые классы
 */
val Any.TAG: String
    get() {
        return if (!javaClass.isAnonymousClass) {
            var name = javaClass.simpleName
            if (name == "Companion") {
                name = this::class.companionClass?.simpleName ?: name
            }
            // first 30 chars
            if (name.length <= 30) name else name.substring(0, 30)
        } else {
            val name = javaClass.name
            // last 30 chars
            if (name.length <= 30) name else name.substring(name.length - 30, name.length)
        }
    }

val <T : Any> KClass<T>.companionClass
    get() = if (isCompanion) this.java.declaringClass else null

/**
 * Расширение позволяет обернуть любую потенциально небезопасную функцию или участок кода в
 * конструкцию вида `tryOrNull { ... }`, которая возвращает результат или `null` в случае ошибки.
 */
inline fun <T> tryOrNull(f: () -> T) =
    try {
        f()
    } catch (_: Exception) {
        null
    }

fun ByteArray.toHexString(delimiter: String = "") = joinToString(delimiter) { "%02x".format(it) }

/**
 * Сравнение списков поэлементно
 */
fun List<*>.deepEquals(other : List<*>) =
    this.size == other.size && this.mapIndexed { index, element -> element == other[index] }.all { it }

