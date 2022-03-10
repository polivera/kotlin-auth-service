package com.caveowl.models.daos

import org.jetbrains.exposed.dao.id.UUIDTable

object UserVerificationCodeDao : UUIDTable("user_verification_codes") {
    const val FIELD_USER_ID = "user_id"
    const val FIELD_VERIFICATION_CODE = "verification_code"
    const val VERIFICATION_CODE_SIZE = 128

    val userId = uuid(FIELD_USER_ID).uniqueIndex()
    val verificationCode = varchar(FIELD_VERIFICATION_CODE, 150)
}
