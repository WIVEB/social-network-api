package com.example.persistance.entity

import com.example.business.User
import com.example.business.models.Conversation
import com.example.business.models.Message
import com.example.business.models.Post
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class ChatEntity(
    var id: String?,
    var creatorUserId: String,
    val createdAt: LocalDateTime,
    val members: List<String>,
    val name: String?,
    val thumbnail: String?
){

    companion object{
        fun from (conversation: Conversation): ChatEntity {
            return ChatEntity(
                id = conversation.id!!,
                creatorUserId = conversation.creator.id!!,
                createdAt = conversation.createdAt,
                name = conversation.name,
                thumbnail = conversation.thumbnail,
                members = conversation.users.map { it.id!! }
            )
        }
    }

    fun toConversation(creator: User, members: List<User>, messages: List<Message>):Conversation{
        return Conversation(
            this.id,
            members,
            this.name,
            this.createdAt,
            creator,
            messages,
            this.thumbnail
        )
    }
}