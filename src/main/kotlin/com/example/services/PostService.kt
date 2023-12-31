package com.example.services

import com.example.business.models.Post
import com.example.controllers.dto.PostDTO
import com.example.social_network.business.PostDao
import com.example.social_network.business.UserDao
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

class PostService(private val userDao: UserDao, private val postDao: PostDao) {

    fun addUserPost(userEmail: String, postRequest: PostDTO){
        val user = userDao.findByEmail(userEmail)!!.toUser()
        val post = Post(
            id = UUID.randomUUID().toString(),
            user = user,
            createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            text = postRequest.text,
            images = postRequest.images,
        )
        postDao.insertPost(post)
    }

    fun updateUserPost(userEmail: String, postId: String, postRequest: PostDTO){
        val user = userDao.findByEmail(userEmail)!!.toUser()
        val post = Post(
            id = postId,
            user = user,
            createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            text = postRequest.text,
            images = postRequest.images,
        )
        postDao.updatePost(post)
    }

    fun getUserFeed(userEmail: String): List<Post> {
        val user = userDao.findByEmail(userEmail)?.toUser()!!
        val feedUsers = mutableListOf<String>()
        feedUsers.add(user.id!!)
        feedUsers.addAll(user.friends)
        return feedUsers
            .map { friend -> postDao.findByUser(friend) }
            .flatten()
            .sortedByDescending { it.createdAt }
    }
}