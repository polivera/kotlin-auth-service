package com.caveowl.features.user

import com.caveowl.features.common.BasePayload
import com.caveowl.features.user.UserHandler.Companion.VERIFICATION_CODE_SIZE
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ValidateUserPayload(
    val userId: String,
    val validationCode: String,
) : BasePayload() {
    companion object {
        const val FIELD_USER_ID = "userId"
        const val FIELD_VALIDATION_CODE = "validationCode"
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
        if (this.validationCode.isEmpty()) {
            this.errors[FIELD_VALIDATION_CODE] = "validation code is required"
        } else if (this.validationCode.length < VERIFICATION_CODE_SIZE) {
            this.errors[FIELD_VALIDATION_CODE] = "invalid validation code"
        }
    }
}