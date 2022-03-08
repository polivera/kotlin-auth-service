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

/**
 * Get a random string of given size
 *
 * @param size the result size of the random string
 * @return a random string
 */
fun getRandomString(size: Int = 32): String {
    val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..size)
        .map { kotlin.random.Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")
}
