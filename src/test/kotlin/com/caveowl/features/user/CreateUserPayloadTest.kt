package com.caveowl.features.user

import org.junit.Assert
import kotlin.test.Test

internal class CreateUserPayloadTest {

    @Test
    fun `Create user payload with empty email`() {
        val model = CreateUserPayload("", "Test.123")
        model.validate()
        Assert.assertEquals(1, model.errors.size)
        Assert.assertEquals("email is required", model.errors[CreateUserPayload.FIELD_EMAIL])
    }

    @Test
    fun `Create user payload with empty password`() {
        val model = CreateUserPayload("test@test.local", "")
        model.validate()
        Assert.assertEquals(1, model.errors.size)
        Assert.assertEquals("password is required", model.errors[CreateUserPayload.FIELD_PASSWORD])
    }

    @Test
    fun `Create user payload with invalid password`() {
        var model = CreateUserPayload("test@test.local", "Test123")
        model.validate()
        Assert.assertEquals(1, model.errors.size)
        Assert.assertEquals("invalid password", model.errors[CreateUserPayload.FIELD_PASSWORD])

        model = CreateUserPayload("test@test.local", "Test.t")
        model.validate()
        Assert.assertEquals(1, model.errors.size)
        Assert.assertEquals("invalid password", model.errors[CreateUserPayload.FIELD_PASSWORD])

        model = CreateUserPayload("test@test.local", "test.123")
        model.validate()
        Assert.assertEquals(1, model.errors.size)
        Assert.assertEquals("invalid password", model.errors[CreateUserPayload.FIELD_PASSWORD])
    }

    @Test
    fun `Create user payload with invalid email`() {
        var model = CreateUserPayload("test@test", "Test.123")
        model.validate()
        Assert.assertEquals(1, model.errors.size)
        Assert.assertEquals("invalid email address", model.errors[CreateUserPayload.FIELD_EMAIL])

        model = CreateUserPayload("@test", "Test.123")
        model.validate()
        Assert.assertEquals(1, model.errors.size)
        Assert.assertEquals("invalid email address", model.errors[CreateUserPayload.FIELD_EMAIL])

        model = CreateUserPayload("asd,adasdtest@test.com", "Test.123")
        model.validate()
        Assert.assertEquals(1, model.errors.size)
        Assert.assertEquals("invalid email address", model.errors[CreateUserPayload.FIELD_EMAIL])

        model = CreateUserPayload("asd,adasdtest@te.st.com", "Test.123")
        model.validate()
        Assert.assertEquals(1, model.errors.size)
        Assert.assertEquals("invalid email address", model.errors[CreateUserPayload.FIELD_EMAIL])
    }

    @Test
    fun `Create user payload all empty`() {
        val model = CreateUserPayload("", "")
        model.validate()
        Assert.assertEquals(2, model.errors.size)
        Assert.assertEquals("email is required", model.errors[CreateUserPayload.FIELD_EMAIL])
        Assert.assertEquals("password is required", model.errors[CreateUserPayload.FIELD_PASSWORD])
    }

    @Test
    fun `Create user payload valid payload`() {
        val model = CreateUserPayload("test@test.local", "Test.123")
        model.validate()
        Assert.assertEquals(0, model.errors.size)
    }
}