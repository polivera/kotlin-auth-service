package com.caveowl.plugins

import com.caveowl.routes.authenticationRoutes
import com.caveowl.routes.userRoutes
import io.ktor.application.*

fun Application.configureRouting() {
    authenticationRoutes()
    userRoutes()
}
