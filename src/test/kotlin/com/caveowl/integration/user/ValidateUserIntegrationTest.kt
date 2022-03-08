package com.caveowl.integration.user

import com.caveowl.features.user.UserHandler.Companion.VERIFICATION_CODE_SIZE
import com.caveowl.plugins.configureSerialization
import com.caveowl.plugins.configureTestDatabase
import com.caveowl.routes.userRoutes
import com.caveowl.utils.getRandomString
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateUserIntegrationTest {

    @Test
    fun `Test validate user with valid data`() {
        val expectedUUID = "d39016e6-303b-4895-b2fa-76e47077746c"
        val expectedVCode = getRandomString(VERIFICATION_CODE_SIZE)

        withTestApplication({
            configureTestDatabase()
            configureSerialization()
            userRoutes()
        }) {

            handleRequest(HttpMethod.Post, "/api/v1/user/validate") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(Json.encodeToString(
                    mapOf("userId" to expectedUUID, "validationCode" to expectedVCode)
                ))
            }.apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                response.content?.let {
                    println(it)
                }
            }
        }
    }
}