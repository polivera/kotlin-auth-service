package com.caveowl.unit.features.authentication

import com.caveowl.features.authentication.LoginPayload
import org.junit.Assert
import kotlin.test.Test

internal class LoginPayloadTest {

    @Test
    fun `Login payload with empty email`() {
        val model = LoginPayload("", "somepass123")
        model.validate()
        Assert.assertEquals(1, model.errors.size)
        Assert.assertEquals("email is required", model.errors[LoginPayload.FIELD_EMAIL])
    }

    @Test
    fun `Login payload with invalid email`() {
        var model = LoginPayload("test@test", "123456")
        model.validate()
        Assert.assertEquals(1, model.errors.size)
        Assert.assertEquals("invalid email address", model.errors[LoginPayload.FIELD_EMAIL])

        model = LoginPayload("@test", "123456")
        model.validate()
        Assert.assertEquals(1, model.errors.size)
        Assert.assertEquals("invalid email address", model.errors[LoginPayload.FIELD_EMAIL])

        model = LoginPayload("asd,adasdtest@test.com", "123456")
        model.validate()
        Assert.assertEquals(1, model.errors.size)
        Assert.assertEquals("invalid email address", model.errors[LoginPayload.FIELD_EMAIL])

        model = LoginPayload("asd,adasdtest@te.st.com", "123456")
        model.validate()
        Assert.assertEquals(1, model.errors.size)
        Assert.assertEquals("invalid email address", model.errors[LoginPayload.FIELD_EMAIL])
    }

    @Test
    fun `Login payload with empty password`() {
        val model = LoginPayload("test@test.local", "")
        model.validate()
        Assert.assertEquals(1, model.errors.size)
        Assert.assertEquals("password is required", model.errors[LoginPayload.FIELD_PASSWORD])
    }

    @Test
    fun `Login payload all empty`() {
        val model = LoginPayload("", "")
        model.validate()
        Assert.assertEquals(2, model.errors.size)
        Assert.assertEquals("email is required", model.errors[LoginPayload.FIELD_EMAIL])
        Assert.assertEquals("password is required", model.errors[LoginPayload.FIELD_PASSWORD])
    }

    @Test
    fun `Login payload valid payload`() {
        val model = LoginPayload("test@test.com", "123456")
        model.validate()
        Assert.assertEquals(0, model.errors.size)
    }
}
