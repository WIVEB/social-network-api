package com.example.services

import com.example.business.User
import com.example.social_network.business.UserDao

class UserService(private val userDao: UserDao) {

    fun getUser(userId: String): User {
        return userDao.getUser(userId)
    }

    fun getUserProfile(userId: String, requestUserEmail: String): User {
        if(userId == "me"){
            return userDao.findByEmail(requestUserEmail)!!.toUser()
        }
        return userDao.getUser(userId)
    }

    fun addFriend(userEmail: String, friendId: String){
        val user = userDao.findByEmail(userEmail)!!
        userDao.addUserFriend(user.id, friendId)
    }

    fun search(searchValue: String): List<User>{
        val users = userDao.findAll()
        return users.filter { it.firstname.contains(searchValue) || it.lastname.contains(searchValue) }
    }

    fun getUserFriends(userId : String): List<User> {
        val user = userDao.getUser(userId)
        return user.friends
            .map { friendUuid -> userDao.getUser(friendUuid) }
            .toList()
    }
}