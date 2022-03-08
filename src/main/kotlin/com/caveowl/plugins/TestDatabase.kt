package com.caveowl.plugins

import com.caveowl.models.daos.UserDao
import com.caveowl.models.daos.UserVerificationCodeDao
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun configureTestDatabase() {
    Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver")

    transaction {
        SchemaUtils.create(
            UserDao,
            UserVerificationCodeDao
        )
    }
}