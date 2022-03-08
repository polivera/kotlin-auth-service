package com.caveowl.features.user

import com.caveowl.models.UserStatus
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: String,
    val email: String,
    val verificationCode: String,
    val status: UserStatus
)
