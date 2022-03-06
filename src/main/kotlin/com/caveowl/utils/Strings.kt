package com.caveowl.utils

/**
 * Split a string into two parts at the given index.
 *
 * @param index where to split the string.
 * @return a list of two strings.
 */
fun String.splitAt(index: Int): List<String> {
    return listOf(
        this.substring(0 until index),
        this.substring(index until this.length)
    )
}
