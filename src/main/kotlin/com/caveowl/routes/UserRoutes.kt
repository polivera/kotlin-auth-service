package com.caveowl.routes

import com.caveowl.features.user.UserHandler
import com.caveowl.repositories.UserRepository
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.userRoutes() {

    val handler = UserHandler(
        UserRepository()
    )

    routing {
        route("/user") {
            post {
                val response = handler.createUser(call.receive())
                call.respond(response.status, response.payload)
            }
        }
    }
}
