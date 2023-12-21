package com.example.persistance

import com.example.business.models.User
import com.example.business.models.Conversation
import com.example.business.models.Message
import com.example.persistance.entity.ChatEntity
import com.example.persistance.entity.MessageEntity
import com.example.persistance.entity.UserEntity
import com.example.business.ChatDao
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.bson.Document.*
import org.bson.conversions.Bson
import org.litote.kmongo.*
import java.util.*

class ChatDaoImpl(socialNetworkDB: MongoDBClient) : ChatDao {

    private val chatCollection = socialNetworkDB.db.getCollection("Chats")

    private val messageCollection = socialNetworkDB.db.getCollection("Messages")

    private val userCollection = socialNetworkDB.db.getCollection("Users")
    override fun findByUser(userId: String): List<Conversation> {
        return findConversationsByFilter(ChatEntity::members contains userId)
    }

    override fun findConversation(conversationId: String): Conversation? {
        return findConversationByFilter(ChatEntity::id eq conversationId)
    }

    override fun insertConversation(conversation: Conversation): String {
        conversation.id = UUID.randomUUID().toString()
        val conversationEntity = ChatEntity.from(conversation)
        chatCollection.insertOne(parse(jsonFormatter.encodeToString(conversationEntity)))
        conversation.messages?.forEach { this.insertConversationMessage(it, conversation.id!!) }
        return conversation.id!!
    }

    override fun updateConversation(conversation: Conversation): String {
        val conversationEntity = ChatEntity.from(conversation)
        chatCollection.replaceOne(
            ChatEntity::id eq conversation.id,
            parse(jsonFormatter.encodeToString(conversationEntity)
            )
        )
        return conversation.id!!
    }

    override fun insertConversationMessage(message: Message, conversationUuid: String): String {
        message.id = UUID.randomUUID().toString()
        val messageEntity = MessageEntity.from(message, conversationUuid)
        messageCollection.insertOne(parse(jsonFormatter.encodeToString(messageEntity)))
        return message.id!!
    }

    override fun deleteConversation(conversationId: String) {
        chatCollection.deleteOne(ChatEntity::id eq conversationId)
    }


    private fun findMessagesByConversation(conversationId: String): List<Message> {
        val messageDocuments = messageCollection.find(MessageEntity::conversationId eq conversationId)
        return messageDocuments
            .mapNotNull { messageDocument -> messageDocument.toJson() }
            .map { messageJson -> jsonFormatter.decodeFromString<MessageEntity>(messageJson) }
            .map { it.toMessage(this.getConversationUser(it.creatorUserId)) }
    }

    private fun getConversationUser(id: String): User {
        val user = userCollection.findOne(UserEntity::id eq id)
        val toJson = user?.toJson()!!
        val userEntity = jsonFormatter.decodeFromString<UserEntity>(toJson)
        return userEntity.toUser()
    }

    private fun findConversationByFilter(filter: Bson): Conversation? {
        val chatDocument = chatCollection.findOne(filter)
        return chatDocument?.let {
            val chatEntity = jsonFormatter.decodeFromString<ChatEntity>(it.toJson())
            mapEntityToConversation(chatEntity)
        }
    }

    private fun findConversationsByFilter(filter: Bson): List<Conversation> {
        val chatDocuments = chatCollection.find(filter)
        return chatDocuments
            .mapNotNull { it.toJson() }
            .map { jsonFormatter.decodeFromString<ChatEntity>(it) }
            .map(this::mapEntityToConversation)
    }

    private fun mapEntityToConversation(chatEntity: ChatEntity): Conversation {
        val creator = this.getConversationMember(chatEntity.creatorUserId)
        val members = chatEntity.members.map { this.getConversationMember(it) }
        val messages = findMessagesByConversation(chatEntity.id!!)

        return chatEntity.toConversation(creator, members, messages)
    }

    private fun getConversationMember(id: String): User {
        val user = userCollection.findOne(UserEntity::id eq id)
        val toJson = user?.toJson()!!
        val userEntity = jsonFormatter.decodeFromString<UserEntity>(toJson)
        return userEntity.toUser()
    }
}