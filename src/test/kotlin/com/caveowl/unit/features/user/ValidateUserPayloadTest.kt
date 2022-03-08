package com.caveowl.unit.features.user

import com.caveowl.features.user.UserHandler.Companion.VERIFICATION_CODE_SIZE
import com.caveowl.features.user.ValidateUserPayload
import com.caveowl.utils.getRandomString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import kotlin.test.Test

internal class ValidateUserPayloadTest {

    val validUUID = "d39016e6-303b-4895-b2fa-76e47077746c"
    val validCode = getRandomString(VERIFICATION_CODE_SIZE)

    @Test
    fun `Validate user payload empty uuid`() {
        // Mock payload
        val payload = ValidateUserPayload("", validCode)
        // Validate
        payload.validate()
        //
        assertFalse(payload.isValid)
        assertEquals(1, payload.errors.size)
        assertEquals("user id is required", payload.errors[ValidateUserPayload.FIELD_USER_ID])
    }

    @Test
    fun `Validate user payload invalid uuid`() {
        // Mock payload
        val payload = ValidateUserPayload("invaliduuid", validCode)
        // Validate
        payload.validate()

        assertFalse(payload.isValid)
        assertEquals(1, payload.errors.size)
        assertEquals("invalid user id", payload.errors[ValidateUserPayload.FIELD_USER_ID])
    }

    @Test
    fun `Validate user payload empty validation code`() {
        // Mock payload
        val payload = ValidateUserPayload(validUUID, "")
        // Validate
        payload.validate()

        assertFalse(payload.isValid)
        assertEquals(1, payload.errors.size)
        assertEquals("validation code is required", payload.errors[ValidateUserPayload.FIELD_VALIDATION_CODE])
    }

    @Test
    fun `Validate user payload invalid validation code`() {
        // Mock payload
        val payload = ValidateUserPayload(validUUID, "12312039881092831")
        // Validate
        payload.validate()

        assertFalse(payload.isValid)
        assertEquals(1, payload.errors.size)
        assertEquals("invalid validation code", payload.errors[ValidateUserPayload.FIELD_VALIDATION_CODE])
    }
}
