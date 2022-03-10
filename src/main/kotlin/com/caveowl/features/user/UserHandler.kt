package com.caveowl.features.user

import com.caveowl.exception.DuplicateUniqueKeyException
import com.caveowl.features.common.ErrorResponse
import com.caveowl.features.common.Response
import com.caveowl.models.UserStatus
import com.caveowl.models.daos.UserVerificationCodeDao.VERIFICATION_CODE_SIZE
import com.caveowl.repositories.UserRepository
import com.caveowl.utils.Password
import com.caveowl.utils.getRandomString
import io.ktor.http.*
import java.util.*

class UserHandler(
    private val userRepo: UserRepository,
) {

    /**
     * Create new user handler
     *
     * This method has integration test at CreateUserIntegrationTest.kt
     */
    fun createUser(createUserPayload: CreateUserPayload): Response {
        createUserPayload.validate()

        if (!createUserPayload.isValid) {
            return Response(
                ErrorResponse("errors on create user payload", createUserPayload.errors),
                HttpStatusCode.BadRequest
            )
        }

        return try {
            // Create the user
            val hashPassword = Password(createUserPayload.password).hash()
            val user = userRepo.createUser(createUserPayload, hashPassword)

            // Create the random verification hash
            val verificationCode = getRandomString(VERIFICATION_CODE_SIZE)
            userRepo.createUserVerificationCode(user.id.value, verificationCode)

            Response(
                UserResponse(
                    id = user.id.toString(),
                    email = user.email,
                    verificationCode = verificationCode,
                    status = UserStatus.Created
                ),
                HttpStatusCode.Created
            )
        } catch (e: DuplicateUniqueKeyException) {
            Response(
                ErrorResponse(
                    "errors on create user payload",
                    mapOf(CreateUserPayload.FIELD_EMAIL to "email ${createUserPayload.email} already in use")
                ),
                HttpStatusCode.BadRequest
            )
        }
    }

    /**
     * Validate the user with verification code
     */
    fun validateUser(verifyUserPayload: VerifyUserPayload): Response {
        verifyUserPayload.validate()

        if (!verifyUserPayload.isValid) {
            return Response(
                ErrorResponse("errors on verification user payload", verifyUserPayload.errors),
                HttpStatusCode.BadRequest
            )
        }

        val code = userRepo.getUserVerificationCode(
            UUID.fromString(verifyUserPayload.userId),
            verifyUserPayload.verificationCode
        ) ?: return Response(
            ErrorResponse(
                "errors on verification user payload",
                mapOf("reason" to "user/verification code does not exist")
            ),
            HttpStatusCode.BadRequest
        )

        userRepo.removeVerificationCode(code)
        userRepo.updateUserStatus(UUID.fromString(verifyUserPayload.userId), UserStatus.Verified)

        return Response("", HttpStatusCode.OK)
    }
}
