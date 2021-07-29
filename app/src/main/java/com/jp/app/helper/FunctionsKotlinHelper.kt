package com.jp.app.helper

/**
 * Use this method to check if at least one item of the elements is null
 * if it happens then it will run
 * Remember to end the closure with a "return", "throw"...
 * [elements] must be the same Type
 */
inline fun <T : Any> guardLet(vararg elements: T?, closure: () -> Nothing): List<T> {
    return if (elements.all { it != null }) {
        elements.filterNotNull()
    } else {
        closure()
    }
}

/**
 * Use this method to check if all the elements list are not null
 * [elements] must be the same Type
 */
fun <T : Any, R : Any> ifLet(vararg elements: T?, block: (List<T>) -> R?): R? {
    return if (elements.all { it != null }) {
        block(elements.filterNotNull())
    } else null
}

/**
 * Use this method to check if the 2 elements list are not null
 * [elements] can be different Types
 */
fun <T1 : Any, T2 : Any, R : Any> ifLetDiffTypes(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

/**
 * Use this method to check if the 3 elements list are not null
 * [elements] can be different Types
 */
fun <T1 : Any, T2 : Any, T3 : Any, R : Any> ifLetDiffTypes(p1: T1?, p2: T2?, p3: T3?, block: (T1, T2, T3) -> R?): R? {
    return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
}

/**
 * Use this method to check if the 4 elements list are not null
 * [elements] can be different Types
 */
fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, R : Any> ifLetDiffTypes(p1: T1?, p2: T2?, p3: T3?, p4: T4?, block: (T1, T2, T3, T4) -> R?): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null) block(p1, p2, p3, p4) else null
}

/**
 * Use this method to check if the 5 elements list are not null
 * [elements] can be different Types
 */
fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, R : Any> ifLetDiffTypes(p1: T1?, p2: T2?, p3: T3?, p4: T4?, p5: T5?, block: (T1, T2, T3, T4, T5) -> R?): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null && p5 != null) block(p1, p2, p3, p4, p5) else null
}