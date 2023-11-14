package com.example.social_network.business

import com.example.business.Dao
import com.example.business.models.Post

interface PostDao : Dao {
    fun findByUser(userId: String): List<Post>
    fun insertPost(post: Post): String

    fun updatePost(post: Post)
    fun getPost(uuid: String): Post
}