package com.caveowl.repositories

import com.caveowl.features.user.CreateUserPayload
import com.caveowl.models.entities.UserEntity
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepository {

    /**
     * Create user
     */
    fun createUser(user: CreateUserPayload, hashPassword: String): UserEntity {
        return transaction {
            UserEntity.new {
                email = user.email
                password = hashPassword
                status = 1
            }
        }
    }
}