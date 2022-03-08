package com.caveowl.models.daos

import org.jetbrains.exposed.dao.id.UUIDTable

object UserVerificationCodeDao : UUIDTable("user_verification_codes") {
    const val FIELD_USER_ID = "user_id"
    const val FIELD_VERIFICATION_CODE = "verification_code"

    val userId = uuid(FIELD_USER_ID).uniqueIndex()
    val verificationCode = varchar(FIELD_VERIFICATION_CODE, 150)
}
