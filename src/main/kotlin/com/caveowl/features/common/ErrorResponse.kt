package com.caveowl.features.common

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val message: String,
    val errors: Map<String, String>? = null
)
