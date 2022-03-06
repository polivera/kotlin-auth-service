package com.caveowl.utils

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class PasswordTest {

    @Test
    fun `Test encrypt password`() {
        val pass = Password("Test.123!")
        val hashedPassword = pass.hash()

        assertEquals(99, hashedPassword.length)
    }

    @Test
    fun `Test validate password`() {
        val pass = Password("Test.123!")
        val hashedPassword = pass.hash()

        assertTrue(pass.isValid(hashedPassword))
    }

    @Test
    fun `Test valid password of several random strings`() {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9') + '#' + '.' + '!' + '@' + '\\'

        for (ind in (1 until 20)) {
            val randomString = (1..(8..32).random())
                .map { kotlin.random.Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("")

            val pass = Password(randomString)
            val hashedPassword = pass.hash()

            assertTrue(pass.isValid(hashedPassword), "Unable to validate password $randomString")
        }
    }
}