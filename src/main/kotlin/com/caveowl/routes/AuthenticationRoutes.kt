package com.caveowl.routes

import com.caveowl.features.authentication.AuthenticationHandler
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.authenticationRoutes() {

    val handler = AuthenticationHandler()

    routing {
        route("/api/v1/auth") {
            post("/login") {
                val response = handler.login(call.receive())
                call.respond(response.status, response.payload)
            }
        }
    }
}
