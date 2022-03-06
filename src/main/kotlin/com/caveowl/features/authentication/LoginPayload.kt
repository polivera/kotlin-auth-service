package com.caveowl.features.authentication

import com.caveowl.features.common.BasePayload
import com.caveowl.features.common.Constants
import kotlinx.serialization.Serializable

@Serializable
data class LoginPayload(
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
        }
    }
}
