package com.caveowl.features.user

import com.caveowl.features.common.BasePayload
import com.caveowl.features.common.Constants
import kotlinx.serialization.Serializable

@Serializable
class CreateUserPayload(
    val email: String,
    val password: String
) : BasePayload() {
    companion object {
        const val FIELD_EMAIL = "email"
        const val FIELD_PASSWORD = "password"
    }

    override fun validate() {
        if (this.email.isEmpty()) {
            this.errors[FIELD_EMAIL] = "email is required"
        } else if (!this.email.matches(Constants.PATTERNS.EMAIL_VALIDATION.toRegex())) {
            this.errors[FIELD_EMAIL] = "invalid email address"
        }

        if (this.password.isEmpty()) {
            this.errors[FIELD_PASSWORD] = "password is required"
        } else if (
            8 > this.password.length || 100 < this.password.length ||
            !this.password.matches(Constants.PATTERNS.PASSWORD.HAS_SYMBOL.toRegex()) ||
            !this.password.matches(Constants.PATTERNS.PASSWORD.HAS_NUMBER.toRegex()) ||
            !this.password.matches(Constants.PATTERNS.PASSWORD.HAS_UPPER_CASE_LETTER.toRegex()) ||
            !this.password.matches(Constants.PATTERNS.PASSWORD.HAS_LOWER_CASE_LETTER.toRegex())
        ) {
            this.errors[FIELD_PASSWORD] = "invalid password"
        }
    }
}
