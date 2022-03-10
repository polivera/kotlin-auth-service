package com.caveowl.repositories

import com.caveowl.exception.DuplicateUniqueKeyException
import com.caveowl.features.user.CreateUserPayload
import com.caveowl.models.UserStatus
import com.caveowl.models.daos.UserDao
import com.caveowl.models.daos.UserVerificationCodeDao
import com.caveowl.models.entities.UserEntity
import com.caveowl.models.entities.UserVerificationCodeEntity
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class UserRepository {

    /**
     * Create user
     */
    fun createUser(user: CreateUserPayload, hashPassword: String): UserEntity {
        return try {
            transaction {
                UserEntity.new {
                    email = user.email
                    password = hashPassword
                    status = UserStatus.Created.id
                }
            }
        } catch (e: ExposedSQLException) {
            // todo: log
            // todo: check if the error is actually duplicate key
            throw DuplicateUniqueKeyException()
        }
    }

    /**
     * Update user status
     */
    fun updateUserStatus(userId: UUID, userStatus: UserStatus) {
        return try {
            transaction {
                UserEntity.findById(userId)?.status = userStatus.id
            }
        } catch (e: ExposedSQLException) {
            // todo log
            throw e
        }
    }

    fun getUserByEmail(emailAddress: String): UserEntity? {
        return try {
            transaction {
                UserEntity.find { UserDao.email eq emailAddress }.firstOrNull()
            }
        } catch (e: ExposedSQLException) {
            // todo log
            throw e
        }
    }

    /**
     * Create a user verification code
     */
    fun createUserVerificationCode(uId: UUID, vCode: String): UserVerificationCodeEntity {
        return try {
            transaction {
                UserVerificationCodeEntity.new {
                    userId = uId
                    verificationCode = vCode
                }
            }
        } catch (e: ExposedSQLException) {
            // todo log
            throw e
        }
    }

    /**
     * Get user specific verification code
     */
    fun getUserVerificationCode(uId: UUID, vCode: String): UserVerificationCodeEntity? {
        return try {
            transaction {
                UserVerificationCodeEntity.find {
                    UserVerificationCodeDao.userId eq uId and
                            (UserVerificationCodeDao.verificationCode eq vCode)
                }.firstOrNull()
            }
        } catch (e: ExposedSQLException) {
            throw e
        }
    }

    /**
     * Delete user verification code
     */
    fun removeVerificationCode(userVerificationCodeEntity: UserVerificationCodeEntity) {
        return transaction {
            userVerificationCodeEntity.delete()
        }
    }
}