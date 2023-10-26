package com.example.controllers

import com.example.business.User
import com.example.business.models.Post
import com.example.services.PostService
import com.example.social_network.controllers.SignUpRequest
import com.mongodb.MongoException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.UUID

@Serializable()
data class PostRequest(val text: String? = "", val images: List<String>? = emptyList())

fun Route.postController(postService: PostService) {
    authenticate("auth_basic") {
        route("/post") {
            put() {
                val postRequest = call.receive<PostRequest>()
                val userEmail = call.principal<UserIdPrincipal>()?.name!!
                try {
                    postService.addUserPost(userEmail, postRequest)
                    call.response.status(HttpStatusCode.OK)
                } catch (e: Error) {
                    throw e
                }
            }

        }
    }
}