package com.example.controllers.dto

import com.example.business.models.Message
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageDTO (var id: String?,
                           var author: String,
                           val createdAt: LocalDateTime,
                           val text: String?,
                           val images: List<String>?){
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