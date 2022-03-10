package com.caveowl.features.authentication

import com.caveowl.features.common.ErrorResponse
import com.caveowl.features.common.Response
import com.caveowl.repositories.UserRepository
import com.caveowl.utils.Password
import io.ktor.http.*

class AuthenticationHandler(
    private val userRepository: UserRepository
) {

    fun login(loginPayload: LoginPayload): Response {
        loginPayload.validate()

        if (!loginPayload.isValid) {
            return Response(ErrorResponse("errors on login", loginPayload.errors), HttpStatusCode.BadRequest)
        }

        val user = userRepository.getUserByEmail(loginPayload.email) ?: return Response(
            ErrorResponse("errors on login", mapOf("reason" to "invalid user or password")),
            HttpStatusCode.BadRequest
        )

        if (!Password(loginPayload.password).matchPasswordHash(user.password)) {
            return Response(
                ErrorResponse("errors on login", mapOf("reason" to "invalid user or password")),
                HttpStatusCode.BadRequest
            )
        }

        return Response(
            "email is: ${loginPayload.email} and password is ${loginPayload.password}",
            HttpStatusCode.Accepted
        )
    }
}
