package com.caveowl.integration.authentication

import com.caveowl.features.authentication.LoginPayload
import com.caveowl.features.common.ErrorResponse
import com.caveowl.integration.TestHelper
import com.caveowl.models.UserStatus
import com.caveowl.plugins.configureSerialization
import com.caveowl.plugins.configureTestDatabase
import com.caveowl.routes.authenticationRoutes
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class LoginUserIntegrationTest {

    private val helper = TestHelper()

    @Test
    fun `Test login user invalid email and password`() {
        withTestApplication({
            configureTestDatabase()
            configureSerialization()
            authenticationRoutes()
        }) {
            handleRequest(HttpMethod.Post, "/api/v1/auth/login") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(Json.encodeToString(LoginPayload("", "")))
            }.apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                response.content?.let {
                    val res = Json.decodeFromString<ErrorResponse>(it)
                    assertEquals("errors on login", res.message)
                    res.errors?.let { err ->
                        assertEquals(2, err.size)
                        assertEquals("email is required", err["email"])
                        assertEquals("password is required", err["password"])
                    }
                }
            }
        }
    }


    @Test
    fun `Test login user valid user information`() {
        withTestApplication({
            configureTestDatabase()
            configureSerialization()
            authenticationRoutes()
        }) {
            val pass = "Test.123"
            val user = helper.createUser(UserStatus.Verified, pass)

            handleRequest(HttpMethod.Post, "/api/v1/auth/login") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(Json.encodeToString(LoginPayload(user.email, pass)))
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                response.content?.let {
                    val res = Json.decodeFromString<ErrorResponse>(it)
                    assertEquals("errors on login", res.message)
                    res.errors?.let { err ->
                        assertEquals(2, err.size)
                        assertEquals("email is required", err["email"])
                        assertEquals("password is required", err["password"])
                    }
                }
            }
        }
    }
}
