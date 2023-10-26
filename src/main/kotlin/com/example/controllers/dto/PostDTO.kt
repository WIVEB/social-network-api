package com.example.controllers.dto

import com.example.business.models.Post
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class PostDTO (var id: String?,
                    var user: String,
                    val createdAt: LocalDateTime,
                    val text: String?,
                    val images: List<String>?){
    companion object{
        fun from (post: Post): PostDTO {
            return PostDTO(
                id = post.id!!,
                user = post.user.id!!,
                createdAt = post.createdAt,
                text = post.text,
                images = post.images
            )
        }
    }
}