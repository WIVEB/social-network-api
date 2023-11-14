package com.example.persistance.entity

import com.example.business.User
import com.example.business.models.Post
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class PostEntity(
    var id: String?,
    var userId: String,
    val createdAt: LocalDateTime,
    val text: String? = "",
    val image: List<String>? = emptyList()
){

    companion object{
        fun from (post: Post): PostEntity {
            return PostEntity(
                id = post.id!!,
                userId = post.user.id!!,
                createdAt = post.createdAt,
                text = post.text,
                image = post.images
            )
        }
    }

    fun toPost(user: User):Post{
        return Post(this.id, user, this.createdAt, this.text, this.image)
    }
}