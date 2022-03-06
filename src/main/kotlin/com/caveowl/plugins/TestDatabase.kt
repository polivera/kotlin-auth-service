package com.caveowl.plugins

import io.ktor.application.*
import org.jetbrains.exposed.sql.Database

fun Application.configureTestDatabase() {
    Database.connect("jdbc:h2:mem:regular", "org.h2.Driver")
}