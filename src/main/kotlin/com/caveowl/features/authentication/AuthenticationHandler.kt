package com.caveowl.features.authentication

import com.caveowl.features.common.ErrorResponse
import com.caveowl.features.common.Response
import io.ktor.http.*

class AuthenticationHandler {

    fun login(loginPayload: LoginPayload): Response {
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
