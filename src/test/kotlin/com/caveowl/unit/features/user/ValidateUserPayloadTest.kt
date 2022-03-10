package com.caveowl.unit.features.user

import com.caveowl.features.user.VerifyUserPayload
import com.caveowl.models.daos.UserVerificationCodeDao.VERIFICATION_CODE_SIZE
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
        val payload = VerifyUserPayload("", validCode)
        // Validate
        payload.validate()
        //
        assertFalse(payload.isValid)
        assertEquals(1, payload.errors.size)
        assertEquals("user id is required", payload.errors[VerifyUserPayload.FIELD_USER_ID])
    }

    @Test
    fun `Validate user payload invalid uuid`() {
        // Mock payload
        val payload = VerifyUserPayload("invaliduuid", validCode)
        // Validate
        payload.validate()

        assertFalse(payload.isValid)
        assertEquals(1, payload.errors.size)
        assertEquals("invalid user id", payload.errors[VerifyUserPayload.FIELD_USER_ID])
    }

    @Test
    fun `Validate user payload empty verification code`() {
        // Mock payload
        val payload = VerifyUserPayload(validUUID, "")
        // Validate
        payload.validate()

        assertFalse(payload.isValid)
        assertEquals(1, payload.errors.size)
        assertEquals("verification code is required", payload.errors[VerifyUserPayload.FIELD_VERIFICATION_CODE])
    }

    @Test
    fun `Validate user payload invalid verification code`() {
        // Mock payload
        val payload = VerifyUserPayload(validUUID, "12312039881092831")
        // Validate
        payload.validate()

        assertFalse(payload.isValid)
        assertEquals(1, payload.errors.size)
        assertEquals("invalid verification code", payload.errors[VerifyUserPayload.FIELD_VERIFICATION_CODE])
    }
}
