package com.caveowl.integration

import com.caveowl.models.UserStatus
import com.caveowl.models.daos.UserVerificationCodeDao.VERIFICATION_CODE_SIZE
import com.caveowl.models.entities.UserEntity
import com.caveowl.models.entities.UserVerificationCodeEntity
import com.caveowl.utils.Password
import com.caveowl.utils.getRandomString
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID


class TestHelper() {
    /**
     * Create test user
     */
    fun createUser(userStatus: UserStatus, pass: String = "Test.123"): UserEntity {
        return transaction {
            UserEntity.new {
                email = "test@test.com"
                password = Password(pass).hash()
                status = userStatus.id
            }
        }
    }

    /**
     * Add verification code to the user
     */
    fun addVerificationCode(uid: UUID): UserVerificationCodeEntity {
        return transaction {
            UserVerificationCodeEntity.new {
                userId = uid
                verificationCode = getRandomString(VERIFICATION_CODE_SIZE)
            }
        }
    }
}
