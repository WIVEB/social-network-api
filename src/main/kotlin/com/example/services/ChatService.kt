package com.example.services

import com.example.business.models.User
import com.example.business.models.Conversation
import com.example.business.models.Message
import com.example.controllers.dto.ChatDTO
import com.example.controllers.dto.ChatMessageDTO
import com.example.social_network.business.ChatDao
import com.example.social_network.business.UserDao
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

class ChatService(private val userDao: UserDao, private val chatDao: ChatDao) {

    fun addConversationMessage(userEmail: String, conversationId: String, messageText: String? = null,
                               messageImages: List<String>? = null): Message {
        val user = userDao.findByEmail(userEmail)!!.toUser()
        val message = Message(
            id = UUID.randomUUID().toString(),
            author = user,
            createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            text = messageText,
            images = messageImages,
            isDeleted = false,
        )
        chatDao.insertConversationMessage(message, conversationId)
        return message
    }

    fun createConversation(userEmail: String, request: ChatDTO){
        val creator = userDao.findByEmail(userEmail)!!.toUser()

        val conversationMembers = (request.users ?: emptyList()).toMutableList()

        if(!isUserMemberOf(creator,conversationMembers)){
            conversationMembers.add(creator.id!!)
        }

        val messages = request.messages.map { createMessage(creator, it) }
        val conversation = createConversation(creator, conversationMembers, request.name, messages)

        chatDao.insertConversation(conversation)
    }

    private fun createMessage(
        creator: User,
        request: ChatMessageDTO
    ): Message {
        return Message(
            id = UUID.randomUUID().toString(),
            author = creator,
            createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            text = request.text,
            images = request.images,
            isDeleted = false,
        )
    }

    private fun createConversation(
        creator: User,
        conversationMembers: List<String>,
        name: String?,
        messages: List<Message>
    ): Conversation {
        return Conversation(
            id = UUID.randomUUID().toString(),
            creator = creator,
            createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            users = conversationMembers.map { getUser(it) },
            messages = messages,
            name = name,
        )
    }

    private fun isUserMemberOf(
        creator: User,
        conversationMembers: List<String>?
    ) : Boolean{
        return conversationMembers?.find { creator.id == it } != null
    }

    fun getUserConversations(userEmail: String): List<Conversation> {
        val user = userDao.findByEmail(userEmail)?.toUser()!!
        return chatDao.findByUser(user.id!!)
            .sortedBy { it.createdAt }
    }

    fun getUser(userUuid: String): User {
        return userDao.getUser(userUuid)
    }

    fun getUserByEmail(email: String): User {
        return userDao.findByEmail(email)!!.toUser()
    }

    fun getUserConversation(userEmail: String, conversationId: String): Conversation {
        val userConversations = getUserConversations(userEmail)
        val conversation = userConversations.find { it.id == conversationId }
            ?: throw Exception("Unable to find conversation $conversationId associated to this user")

        return conversation
    }
}