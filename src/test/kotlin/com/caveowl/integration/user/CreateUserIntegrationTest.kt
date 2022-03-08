package com.caveowl.integration.user

import com.caveowl.features.common.ErrorResponse
import com.caveowl.features.user.CreateUserPayload
import com.caveowl.features.user.UserResponse
import com.caveowl.models.UserStatus
import com.caveowl.models.daos.UserDao
import com.caveowl.models.daos.UserVerificationCodeDao
import com.caveowl.models.entities.UserEntity
import com.caveowl.models.entities.UserVerificationCodeEntity
import com.caveowl.plugins.configureSerialization
import com.caveowl.plugins.configureTestDatabase
import com.caveowl.routes.userRoutes
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.test.*

internal class CreateUserIntegrationTest {

    @BeforeTest
    fun beforeEach() {
        withTestApplication({
            configureTestDatabase()
        }) {
            transaction { UserDao.deleteAll() }
        }
    }

    @Test
    fun `Test create user success response with valid payload`() {
        val expectedEmail = "test@test.local"
        val expectedStatus = UserStatus.Created
        withTestApplication({
            configureTestDatabase()
            configureSerialization()
            userRoutes()
        }) {
            handleRequest(HttpMethod.Post, "/api/v1/user") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(Json.encodeToString(CreateUserPayload("test@test.local", "Test.123")))
            }.apply {
                assertEquals(HttpStatusCode.Created, response.status())
                response.content?.let {
                    val result = Json.decodeFromString<UserResponse>(it)
                    assertEquals(expectedEmail, result.email)
                    assertEquals(expectedStatus, result.status)
                    assertFalse(result.id.isEmpty())
                    assertFalse(result.verificationCode.isEmpty())

                    // Check if row is in the database
                    val dbUser = transaction {
                        UserEntity.findById(UUID.fromString(result.id))
                    }
                    assertNotNull(dbUser)
                    assertEquals(expectedEmail, dbUser.email)
                    assertEquals(expectedStatus.id, dbUser.status)
                    assertEquals(result.id, dbUser.id.toString())

                    val dbValidation = transaction {
                        UserVerificationCodeEntity.find {
                            UserVerificationCodeDao.verificationCode eq result.verificationCode
                        }.first()
                    }
                    assertEquals(result.verificationCode, dbValidation.verificationCode)
                }
            }
        }
    }

    @Test
    fun `Test create user try to create an already existing user`() {
        val expectedEmail = "test@test.local"
        withTestApplication({
            configureTestDatabase()
            configureSerialization()
            userRoutes()
        }) {

            transaction {
                UserEntity.new {
                    email = expectedEmail
                    password = "some password"
                    status = UserStatus.Created.id
                }
            }

            handleRequest(HttpMethod.Post, "/api/v1/user") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(Json.encodeToString(CreateUserPayload("test@test.local", "Test.123")))
            }.apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                response.content?.let {
                    val result = Json.decodeFromString<ErrorResponse>(it)
                    assertEquals("errors on create user payload", result.message)
                    result.errors?.let { err ->
                        assertEquals(1, err.size)
                        assertEquals("email $expectedEmail already in use", err["email"])
                    }
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
            handleRequest(HttpMethod.Post, "/api/v1/user") {
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
