package com.example.social_network.business

import com.example.business.Dao
import com.example.business.models.User
import com.example.persistance.entity.UserEntity

interface UserDao : Dao {
    fun getUser(id: String): User
    fun addUserFriend(userId: String, friendId: String)
    fun insertUser(user: User)
    fun findByEmail(email: String): UserEntity?
    fun findAll(): List<User>
}