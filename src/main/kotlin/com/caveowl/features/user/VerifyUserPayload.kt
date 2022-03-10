package com.caveowl.features.user

import com.caveowl.features.common.BasePayload
import com.caveowl.models.daos.UserVerificationCodeDao.VERIFICATION_CODE_SIZE
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class VerifyUserPayload(
    val userId: String,
    val verificationCode: String,
) : BasePayload() {
    companion object {
        const val FIELD_USER_ID = "userId"
        const val FIELD_VERIFICATION_CODE = "verificationCode"
    }

    override fun validate() {
        if (this.userId.isEmpty()) {
            this.errors[FIELD_USER_ID] = "user id is required"
        } else {
            try {
                UUID.fromString(this.userId)
            } catch (e: IllegalArgumentException) {
                this.errors[FIELD_USER_ID] = "invalid user id"
            }
        }
        if (this.verificationCode.isEmpty()) {
            this.errors[FIELD_VERIFICATION_CODE] = "verification code is required"
        } else if (this.verificationCode.length < VERIFICATION_CODE_SIZE) {
            this.errors[FIELD_VERIFICATION_CODE] = "invalid verification code"
        }
    }
}