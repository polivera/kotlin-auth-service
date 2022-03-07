package com.caveowl.features.user

import com.caveowl.features.common.ErrorResponse
import com.caveowl.features.common.Response
import com.caveowl.repositories.UserRepository
import com.caveowl.utils.Password
import io.ktor.http.*

class UserHandler(
    private val userRepo: UserRepository,
) {

    fun createUser(createUserPayload: CreateUserPayload): Response {
        createUserPayload.validate()

        if (!createUserPayload.isValid) {
            return Response(
                ErrorResponse("errors on create user payload", createUserPayload.errors),
                HttpStatusCode.BadRequest
            )
        }

        val hashPassword = Password(createUserPayload.password).hash()
        val user = userRepo.createUser(createUserPayload, hashPassword)

        return Response(
            UserResponse(
                id = user.id.toString(),
                email = user.email,
                status = user.status.toInt()
            ),
            HttpStatusCode.Created
        )
    }
}
