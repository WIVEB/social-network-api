package com.example.controllers.dto

import com.example.business.models.Conversation
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class ChatDTO (var id: String?,
                    var users: List<String>,
                    val createdAt: LocalDateTime,
                    val name: String? = null,
                    val thumbnail: String? = null,
                    val messages: List<ChatMessageDTO>){
    companion object{
        fun from (chat: Conversation): ChatDTO {
            return ChatDTO(
                id = chat.id!!,
                users = chat.users.map { it.id!! }.toList(),
                createdAt = chat.createdAt,
                name = chat.name,
                thumbnail = chat.thumbnail,
                messages = chat.messages!!.map { ChatMessageDTO.from(it) }
            )
        }
    }
}