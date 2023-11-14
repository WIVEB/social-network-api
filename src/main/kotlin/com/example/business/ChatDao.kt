package com.example.social_network.business

import com.example.business.Dao
import com.example.business.models.Conversation
import com.example.business.models.Message

interface ChatDao : Dao {
    fun findByUser(userId: String): List<Conversation>
    fun findConversation(conversationId: String): Conversation?
    fun insertConversation(conversation: Conversation): String
    fun insertConversationMessage(message: Message, conversationUuid: String): String
}