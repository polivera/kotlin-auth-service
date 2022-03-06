package com.caveowl.routes;

import com.caveowl.features.authentication.LoginPayload
import com.caveowl.features.common.ErrorResponse
import com.caveowl.plugins.configureSerialization
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthenticationRoutesKtTest {

//    @Test
//    fun `Try to login with invalid email address`() {
//        withTestApplication({
//            authenticationRoutes()
//            configureSerialization()
//        }) {
//            handleRequest(HttpMethod.Post, "/auth/login") {
//                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
//                setBody(Json.encodeToString(LoginPayload("testEmail", "TestPassword")))
//            }.apply {
//                val expectedErrors = mapOf("email" to "invalid email address")
//                val errorMessage = "Errors on login"
//                assertEquals(HttpStatusCode.BadRequest, response.status())
//                assertEquals(Json.encodeToString(ErrorResponse(errorMessage, expectedErrors)), response.content)
//            }
//        }
//    }
}