package com.caveowl.integration.user

import com.caveowl.features.common.ErrorResponse
import com.caveowl.features.user.VerifyUserPayload
import com.caveowl.integration.TestHelper
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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ValidateUserIntegrationTest {

    private val helper = TestHelper()

    @Test
    fun `Test validate user empty userId and verification code`() {
        withTestApplication({
            configureTestDatabase()
            configureSerialization()
            userRoutes()
        }) {
            handleRequest(HttpMethod.Post, "/api/v1/user/verify") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(
                    Json.encodeToString(
                        VerifyUserPayload("", "")
                    )
                )
            }.apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                response.content?.let {
                    val response = Json.decodeFromString<ErrorResponse>(it)
                    assertEquals("errors on verification user payload", response.message)
                    assertNotNull(response.errors)
                    response.errors?.let { err ->
                        assertEquals(2, err.size)
                        assertEquals("user id is required", err["userId"])
                        assertEquals("verification code is required", err["verificationCode"])
                    }
                }
            }
        }
    }

    @Test
    fun `Test validate user with invalid uuid and verification code`() {
        withTestApplication({
            configureTestDatabase()
            configureSerialization()
            userRoutes()
        }) {
            handleRequest(HttpMethod.Post, "/api/v1/user/verify") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(
                    Json.encodeToString(VerifyUserPayload("wrong-uuid", "wrong-code"))
                )
            }.apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                response.content?.let {
                    val response = Json.decodeFromString<ErrorResponse>(it)
                    assertEquals("errors on verification user payload", response.message)
                    assertNotNull(response.errors)
                    response.errors?.let { err ->
                        assertEquals(2, err.size)
                        assertEquals("invalid user id", err["userId"])
                        assertEquals("invalid verification code", err["verificationCode"])
                    }
                }
            }
        }
    }

    @Test
    fun `Test validate user with valid but not existing data`() {
        withTestApplication({
            configureTestDatabase()
            configureSerialization()
            userRoutes()
        }) {
            val user = helper.createUser(UserStatus.Created)
            val vCode = helper.addVerificationCode(user.id.value)
            handleRequest(HttpMethod.Post, "/api/v1/user/verify") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(Json.encodeToString(VerifyUserPayload(UUID.randomUUID().toString(), vCode.verificationCode)))
            }.apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                response.content?.let {
                    val res = Json.decodeFromString<ErrorResponse>(it)
                    assertEquals("errors on verification user payload", res.message)
                    res.errors?.let { err ->
                        assertEquals(1, err.size)
                        assertEquals("user/verification code does not exist", err["reason"])
                    }
                }
            }

            transaction {
                UserDao.deleteAll()
                UserVerificationCodeDao.deleteAll()
            }
        }
    }

    @Test
    fun `Test validate user with valid data`() {
        withTestApplication({
            configureTestDatabase()
            configureSerialization()
            userRoutes()
        }) {
            val user = helper.createUser(UserStatus.Created)
            val vCode = helper.addVerificationCode(user.id.value)
            handleRequest(HttpMethod.Post, "/api/v1/user/verify") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(Json.encodeToString(VerifyUserPayload(user.id.toString(), vCode.verificationCode)))
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val dbUser = transaction {
                    UserEntity.findById(user.id)
                }
                assertNotNull(dbUser)
                assertEquals(UserStatus.Verified.id, dbUser.status)
                val dbVCode = transaction {
                    UserVerificationCodeEntity.findById(vCode.id)
                }
                assertNull(dbVCode)
            }

            transaction {
                UserDao.deleteAll()
                UserVerificationCodeDao.deleteAll()
            }
        }
    }
}
