package com.example.controllers

import com.example.controllers.dto.PostDTO
import com.example.services.PostService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable()
data class PostRequest(val text: String? = "", val images: List<String>? = emptyList())

fun Route.postController(postService: PostService) {
    authenticate("auth_basic") {
        route("/post") {
            post<PostDTO>() {
                val userEmail = call.principal<UserIdPrincipal>()?.name!!
                try {
                    postService.addUserPost(userEmail, it)
                    call.respond(HttpStatusCode.OK)
                } catch (e: Error) {
                    call.respond(HttpStatusCode.InternalServerError, Json.encodeToString(e))
                }
            }

            put<PostDTO>("/{id}") {
                val userEmail = call.principal<UserIdPrincipal>()?.name!!
                val postId = call.parameters["id"]!!
                try {
                    postService.updateUserPost(userEmail, postId, it)
                    call.respond(HttpStatusCode.OK)
                } catch (e: Error) {
                    call.respond(HttpStatusCode.InternalServerError, Json.encodeToString(e))
                }
            }

        }
    }
}