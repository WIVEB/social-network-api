package com.example.persistance.entity

import com.example.business.User
import com.example.business.models.Conversation
import com.example.business.models.Message
import com.example.business.models.Post
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class MessageEntity(
    var id: String?,
    val conversationId: String,
    var creatorUserId: String,
    val createdAt: LocalDateTime,
    val text: String? = "",
    val images: List<String>? = emptyList(),
    val isDeleted: Boolean
){

    companion object{
        fun from (message: Message, conversationId: String): MessageEntity {
            return MessageEntity(
                id = message.id!!,
                creatorUserId = message.author.id!!,
                createdAt = message.createdAt,
                text = message.text,
                images = message.images,
                isDeleted = message.isDeleted,
                conversationId = conversationId
            )
        }
    }

    fun toMessage(user: User):Message{
        return Message(this.id, user, this.createdAt, this.text, this.images, this.isDeleted)
    }
}