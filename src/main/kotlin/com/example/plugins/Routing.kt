package com.example.plugins

import com.example.business.AuthenticationDao
import com.example.controllers.*
import com.example.persistance.ChatDaoImpl
import com.example.persistance.MongoDBClient
import com.example.persistance.PostDaoImpl
import com.example.persistance.UserDaoImpl
import com.example.services.ChatService
import com.example.services.PostService
import com.example.services.UserService
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting(mongoDBClient: MongoDBClient) {

    routing {
        val userDao = UserDaoImpl(mongoDBClient)
        val postDao = PostDaoImpl(mongoDBClient)
        val chatDaoImpl = ChatDaoImpl(mongoDBClient)
        val userService = UserService(userDao)
        val postService = PostService(userDao, postDao)
        val chatService = ChatService(userDao, chatDaoImpl)
        val authenticationDao = AuthenticationDao(mongoDBClient)
        authenticationController(authenticationDao)
        userController(userService)
        postController(postService)
        feedController(postService)
        chatController(chatService)
    }
}
