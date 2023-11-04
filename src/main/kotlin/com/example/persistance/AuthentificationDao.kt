package com.example.business

import com.example.controllers.SignUpRequest
import com.example.persistance.MongoDBClient
import com.example.persistance.UserEntity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.bson.Document
import org.litote.kmongo.eq
import org.litote.kmongo.findOne

class AuthenticationDao(socialNetworkDB: MongoDBClient) : Dao {
    private val userCollection = socialNetworkDB.db.getCollection("Users")
    fun signUp( signUpRequest: SignUpRequest){
        val jsonUserEntity = jsonFormatter.encodeToString(UserEntity.from(signUpRequest))
        userCollection.insertOne(Document.parse(jsonUserEntity))
        // add error handling
    }

    fun findByEmail(email: String): UserEntity? {
        val userEntity: UserEntity? = try {
            val user = userCollection.findOne(UserEntity::email eq email)
            val jsonUserEntity = user?.toJson()!!
            jsonFormatter.decodeFromString<UserEntity>(jsonUserEntity)
        }catch(e: Throwable) {
            null
        }
        return userEntity;
    }

}