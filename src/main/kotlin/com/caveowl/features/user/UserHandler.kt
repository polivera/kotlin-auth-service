package com.caveowl.features.user

import com.caveowl.repositories.UserRepository
import io.ktor.application.*

class UserHandler(val userRepo: UserRepository){


    fun createUser(createUserPayload: CreateUserPayload) {


    }

}