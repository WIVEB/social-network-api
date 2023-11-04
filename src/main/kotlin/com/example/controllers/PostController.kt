package com.example.controllers

import com.example.services.PostService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

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