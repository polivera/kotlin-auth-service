package com.caveowl.routes

import com.caveowl.features.authentication.AuthenticationHandler
import com.caveowl.repositories.UserRepository
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.authenticationRoutes() {

    val userRepository = UserRepository()
    val handler = AuthenticationHandler(userRepository)

    routing {
//        route(".well-known") {
//            get { call.respond(HttpStatusCode.OK, KeyGenerator.instance.getJWK()) }
//        }
        route("/api/v1/auth") {
            post("/login") {
                val response = handler.login(call.receive())
                call.respond(response.status, response.payload)
            }
        }
    }
}
