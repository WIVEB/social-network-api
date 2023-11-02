package com.example.plugins

import com.example.business.AuthenticationDao
import com.example.controllers.postController
import com.example.social_network.controllers.userController
import com.example.persistance.MongoDBClient
import com.example.persistance.PostDaoImpl
import com.example.persistance.UserDaoImpl
import com.example.services.PostService
import com.example.services.UserService
import com.example.social_network.controllers.authenticationController
import com.example.social_network.controllers.feedController
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureRouting(mongoDBClient: MongoDBClient) {

    routing {
        val userDao = UserDaoImpl(mongoDBClient)
        val postDao = PostDaoImpl(mongoDBClient)
        val userService = UserService(userDao)
        val postService = PostService(userDao, postDao)
        val authenticationDao = AuthenticationDao(mongoDBClient)
        authenticationController(authenticationDao)
        userController(userService)
        postController(postService)
        feedController(postService)
    }
}
