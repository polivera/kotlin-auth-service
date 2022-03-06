package com.caveowl.features.common

import io.ktor.http.*

data class Response(
    val message: Any,
    val status: HttpStatusCode
) {}