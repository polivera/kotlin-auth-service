package com.caveowl.unit.features.user

import com.caveowl.features.common.ErrorResponse
import com.caveowl.features.user.CreateUserPayload
import com.caveowl.features.user.UserHandler
import com.caveowl.repositories.UserRepository
import io.ktor.http.*
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

internal class UserHandlerTest {

    private val userRepository = UserRepository()
    private val userHandler = UserHandler(userRepository)


    @Test
    fun `Test create user invalid payload`() {
        // Mock payload
        val createUserPayload = CreateUserPayload("wrong-email", "wrongPassword")

        // Call action
        val result = this.userHandler.createUser(createUserPayload)

        // Assert results
        assertEquals(HttpStatusCode.BadRequest, result.status)
        assertTrue(result.payload is ErrorResponse)
        with(result.payload as ErrorResponse) {
            errors?.let {
                assertEquals(2, it.size)
                assertEquals("invalid email address", it["email"])
                assertEquals("invalid password", it["password"])
            }
        }
    }

    @Test
    fun `Test create user empty payload`() {
        // Mock payload
        val createUserPayload = CreateUserPayload("", "")

        // Call action
        val result = this.userHandler.createUser(createUserPayload)

        // Assert results
        assertEquals(HttpStatusCode.BadRequest, result.status)
        assertTrue(result.payload is ErrorResponse)
        with(result.payload as ErrorResponse) {
            errors?.let {
                assertEquals(2, it.size)
                assertEquals("email is required", it["email"])
                assertEquals("password is required", it["password"])
            }
        }
    }

}