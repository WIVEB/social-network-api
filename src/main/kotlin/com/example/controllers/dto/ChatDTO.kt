package com.example.controllers.dto

import com.example.business.models.Conversation
import com.example.business.models.User
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class ChatDTO (var id: String? = null,
                    var users: List<String>? = null,
                    val createdAt: LocalDateTime? = null,
                    val name: String? = null,
                    val thumbnail: String? = null,
                    val messages: List<ChatMessageDTO> = emptyList()
){
    companion object{
        fun from (chat: Conversation, currentUser: User): ChatDTO {
            return ChatDTO(
                id = chat.id!!,
                users = chat.users.map { it.id!! }.toList(),
                createdAt = chat.createdAt,
                name = chat.name,
                thumbnail = chat.thumbnail,
                messages = chat.messages!!.map { ChatMessageDTO.from(it, it.author.id == currentUser.id) }
            )
        }
    }
}