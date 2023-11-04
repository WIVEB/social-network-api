package com.example.controllers

import com.example.controllers.dto.PostDTO
import com.example.services.PostService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


fun Route.feedController(postService: PostService) {
    authenticate("auth_basic") {
        route("/feed") {
            get() {
                val feed = postService.getUserFeed(call.principal<UserIdPrincipal>()?.name!!)
                val feedResponse = feed.map { PostDTO.from(it) }
                call.respond(Json.encodeToString(feedResponse))
            }
        }
    }
}
