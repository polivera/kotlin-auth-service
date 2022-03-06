package com.caveowl.models.entities

import com.caveowl.models.daos.UserDao
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class UserEntity(id: EntityID<UUID>) : UUIDEntity(id){
    companion object: UUIDEntityClass<UserEntity>(UserDao)
    var email by UserDao.email
    var password by UserDao.password
    var status by UserDao.status
}
