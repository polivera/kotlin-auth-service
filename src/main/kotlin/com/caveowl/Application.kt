package com.caveowl

import com.caveowl.plugins.configureDatabase
import com.caveowl.plugins.configureRouting
import com.caveowl.plugins.configureSerialization
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, environment = applicationEngineEnvironment {
        config = HoconApplicationConfig(ConfigFactory.load())

        module(Application::bootstrap)

        connector {
            port = config.property("ktor.deployment.port").getString().toInt()
            host = config.property("ktor.deployment.host").getString()
        }

    }).start(wait = true)
}

fun Application.bootstrap() {
    configureDatabase()
    configureSerialization()
    configureRouting()
}