package com.caveowl.unit.utils

import com.caveowl.utils.getRandomString
import com.caveowl.utils.splitAt
import org.junit.Test
import kotlin.test.assertEquals

internal class StringsTest {

    @Test
    fun `Test split string`() {
        val stringParts = "123456".splitAt(3)
        assertEquals("123", stringParts[0])
        assertEquals("456", stringParts[1])
    }

    @Test
    fun `Test get random string`() {
        val randomString = getRandomString(64)
        assertEquals(64, randomString.length)
    }
}