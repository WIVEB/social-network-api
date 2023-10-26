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
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting() {

    routing {
        val socialNetworkDB = MongoDBClient("mongodb://localhost:27017", "social_network")
        val userDao = UserDaoImpl(socialNetworkDB)
        val postDao = PostDaoImpl(socialNetworkDB)
        val userService = UserService(userDao)
        val postService = PostService(userDao, postDao)
        val authenticationDao = AuthenticationDao(socialNetworkDB)
        authenticationController(authenticationDao)
        userController(userService)
        postController(postService)
        feedController(postService)
    }
}
