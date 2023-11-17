package com.example.controllers.dto

import com.example.business.models.Message
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageDTO (var id: String? = null,
                           var author: String? = null,
                           val createdAt: LocalDateTime? = null,
                           val text: String? = null,
                           val images: List<String>? = null){
    companion object{
        fun from (message: Message): ChatMessageDTO {
            return ChatMessageDTO(
                id = message.id!!,
                author = message.author.id!!,
                createdAt = message.createdAt,
                text = message.text,
                images = message.images
            )
        }
    }
}