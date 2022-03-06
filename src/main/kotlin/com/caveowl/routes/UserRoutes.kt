package com.caveowl.routes

import com.caveowl.features.user.CreateUserPayload
import com.caveowl.features.user.UserHandler
import com.caveowl.repositories.UserRepository
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*

fun Application.userRoutes() {

    val handler = UserHandler(
        UserRepository()
    )

    routing {
        route("/user") {
            post {
                handler.createUser(call.receive())
            }
        }
    }
}
