package com.caveowl.routes

import com.caveowl.features.common.ErrorResponse
import com.caveowl.features.user.CreateUserPayload
import com.caveowl.features.user.UserResponse
import com.caveowl.models.entities.UserEntity
import com.caveowl.plugins.configureSerialization
import com.caveowl.plugins.configureTestDatabase
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

internal class UserRoutesKtTest {

    @Test
    fun `Test create user success response with valid payload`() {
        val expectedEmail = "test@test.local"
        val expectedStatus = 1
        withTestApplication({
            configureTestDatabase()
            configureSerialization()
            userRoutes()
        }) {
            handleRequest(HttpMethod.Post, "/user") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(Json.encodeToString(CreateUserPayload("test@test.local", "Test.123")))
            }.apply {
                assertEquals(HttpStatusCode.Created, response.status())
                response.content?.let {
                    val result = Json.decodeFromString<UserResponse>(it)
                    assertEquals(expectedEmail, result.email)
                    assertEquals(expectedStatus, result.status)
                    assertFalse(result.id.isEmpty())
                    // Check if row is in the database
                    val dbUser = transaction {
                        UserEntity.findById(UUID.fromString(result.id))
                    }
                    assertNotNull(dbUser)
                    assertEquals(expectedEmail, dbUser.email)
                    assertEquals(expectedStatus.toByte(), dbUser.status)
                    assertEquals(result.id, dbUser.id.toString())
                }
            }
        }
    }

    @Test
    fun `Test create user will return bad request and list of errors with invalid payload`() {
        withTestApplication({
            configureTestDatabase()
            configureSerialization()
            userRoutes()
        }) {
            handleRequest(HttpMethod.Post, "/user") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(Json.encodeToString(CreateUserPayload("test@test", "Test123")))
            }.apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                response.content?.let {
                    val result = Json.decodeFromString<ErrorResponse>(it)
                    assertEquals("errors on create user payload", result.message)
                    result.errors?.let { err ->
                        assertEquals(2, err.size)
                        assertEquals("invalid email address", err["email"])
                        assertEquals("invalid password", err["password"])
                    }
                }
            }
        }
    }
}
