package com.caveowl.features.common

import io.ktor.http.*

data class Response(
    val payload: Any,
    val status: HttpStatusCode
) {}