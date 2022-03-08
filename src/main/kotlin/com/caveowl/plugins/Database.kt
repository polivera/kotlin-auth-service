package com.caveowl.plugins

import com.caveowl.models.daos.UserDao
import com.caveowl.models.daos.UserVerificationCodeDao
import io.ktor.application.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabase() {
    val appConfig = environment.config
    val hikariConfig = HikariConfig().apply {
        jdbcUrl = appConfig.property("ktor.database.mysql.url").getString()
        driverClassName = appConfig.property("ktor.database.mysql.driver").getString()
        username = appConfig.property("ktor.database.mysql.user").getString()
        password = appConfig.property("ktor.database.mysql.pass").getString()
        maximumPoolSize = appConfig.property("ktor.database.mysql.maximumPoolSize").getString().toInt()
    }

    Database.connect(
        HikariDataSource(hikariConfig)
    )

    transaction {
        SchemaUtils.create(
            UserDao,
            UserVerificationCodeDao
        )
    }
}
