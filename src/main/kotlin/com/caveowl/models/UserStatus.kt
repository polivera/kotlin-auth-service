package com.caveowl.models

enum class UserStatus(val id: Byte) {
    Created(1),
    Verified(2),
    Deleted(3)
}
