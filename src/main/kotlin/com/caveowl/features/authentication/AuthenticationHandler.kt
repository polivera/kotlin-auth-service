package com.caveowl.features.authentication

import com.caveowl.features.common.ErrorResponse
import com.caveowl.features.common.Response
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*

class AuthenticationHandler {

    suspend fun login(loginPayload: LoginPayload) : Response {
        loginPayload.validate()

        if (!loginPayload.isValid) {
            return Response(ErrorResponse("Errors on login", loginPayload.errors), HttpStatusCode.BadRequest)
        }

//

        return Response(
            "email is: ${loginPayload.email} and password is ${loginPayload.password}",
            HttpStatusCode.Accepted
        )
    }
}
