package com.example.persistance

import com.example.business.User
import com.example.persistance.entity.UserEntity
import com.example.social_network.business.UserDao
import com.mongodb.client.model.Updates
import kotlinx.serialization.decodeFromString
import org.litote.kmongo.*
import org.bson.Document
import java.util.UUID

class UserDaoImpl(socialNetworkDB: MongoDBClient): UserDao {
    private val userCollection = socialNetworkDB.db.getCollection("Users")

    override fun getUser(id: String): User {
        val user = findUserDocument(id)
        val toJson = user?.toJson()!!
        val userEntity = jsonFormatter.decodeFromString<UserEntity>(toJson)

        return userEntity.toUser()
    }

    override fun addUserFriend(userId: String, friendId: String) {
        addFriendToUserDocument(userId, friendId)
        addFriendToUserDocument(friendId, userId)
    }

    override fun insertUser(user: User) {
        user.id = UUID.randomUUID().toString();
        userCollection.insertOne(Document.parse(UserEntity.from(user).toString()))
    }

    override fun findByEmail(email: String): UserEntity? {
        val user = userCollection.findOne(UserEntity::email eq email)
        val jsonUserEntity = user?.toJson()!!
        return jsonFormatter.decodeFromString<UserEntity>(jsonUserEntity)
    }

    override fun findAll(): List<User> {
        val userDocuments = userCollection.find()
        return userDocuments
            .mapNotNull { it.toJson() }
            .map { jsonFormatter.decodeFromString<UserEntity>(it) }
            .map { it.toUser() }
    }

    private fun findUserDocument(id: String): Document? {
        return userCollection.findOne(UserEntity::id eq id)
    }

    private fun addFriendToUserDocument(userId: String, friendId: String){
        userCollection.updateOne(
            UserEntity::id eq userId,
            Updates.addToSet("friends", friendId)
        )
    }
}