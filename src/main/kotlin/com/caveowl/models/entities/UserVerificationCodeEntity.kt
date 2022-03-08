package com.caveowl.models.entities

import com.caveowl.models.daos.UserVerificationCodeDao
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class UserVerificationCodeEntity(id: EntityID<UUID>) : UUIDEntity(id){
    companion object: UUIDEntityClass<UserVerificationCodeEntity>(UserVerificationCodeDao)
    var userId by UserVerificationCodeDao.userId
    var verificationCode by UserVerificationCodeDao.verificationCode
}
