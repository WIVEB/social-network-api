package com.example.persistance

import com.example.business.User
import com.example.business.models.Post
import com.example.social_network.business.PostDao
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.bson.Document
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import java.util.*

class PostDaoImpl(socialNetworkDB: MongoDBClient) : PostDao {

    private val postCollection = socialNetworkDB.db.getCollection("Posts")

    private val userCollection = socialNetworkDB.db.getCollection("Users")

    override fun getPost(uuid: String): Post {
        val postDocument = postCollection.findOne(Post::id eq uuid)
        val postJson = postDocument?.toJson()!!
        val postEntity = jsonFormatter.decodeFromString<PostEntity>(postJson)
        return postEntity.toPost(this.getPostUser(postEntity.userId))
    }

    override fun insertPost(post: Post) {
        post.id = UUID.randomUUID().toString();
        val postEntity = PostEntity.from(post)
        postCollection.insertOne(Document.parse(jsonFormatter.encodeToString(postEntity)))
    }

    override fun findByUser(userId: String): List<Post> {
        val postDocuments = postCollection.find(PostEntity::userId eq userId)
        return postDocuments
            .mapNotNull { postDocument -> postDocument.toJson() }
            .map { postJson -> jsonFormatter.decodeFromString<PostEntity>(postJson) }
            .map { it.toPost(this.getPostUser(it.userId)) }
    }

    private fun getPostUser(id: String): User{
        val user = userCollection.findOne(UserEntity::id eq id)
        val toJson = user?.toJson()!!
        val userEntity = jsonFormatter.decodeFromString<UserEntity>(toJson)
        return userEntity.toUser()
    }
}