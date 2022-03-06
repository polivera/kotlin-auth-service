package com.caveowl.models.daos

import org.jetbrains.exposed.dao.id.UUIDTable

object UserDao : UUIDTable("users") {
    const val FIELD_ID = "id"
    const val FIELD_EMAIL = "email"
    const val FIELD_PASSWORD = "password"
    const val FIELD_STATUS = "status"

    val email = varchar(FIELD_EMAIL, 150).uniqueIndex()
    val password = varchar(FIELD_PASSWORD, 150)
    val status = integer(FIELD_STATUS)
}
