package com.example.controllers

import com.example.controllers.dto.UserDTO
import com.example.services.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


fun Route.userController(userService: UserService) {
    authenticate("auth_basic") {
        route("/user") {
            get("/{id}") {
                val requestUserEmail = call.principal<UserIdPrincipal>()?.name!!
                val id = call.parameters["id"]!!
                val user = userService.getUserProfile(id, requestUserEmail)
                val response = UserDTO.from(user)
                call.respond(Json.encodeToString(response))
            }
            put("/friend/{id}") {
                val userEmail = call.principal<UserIdPrincipal>()?.name!!
                val friendId = call.parameters["id"]!!
                userService.addFriend(userEmail, friendId)
                call.response.status(HttpStatusCode.OK)
            }

            get() {
                val searchValue = call.request.queryParameters["search"]
                if (searchValue == null) {
                    call.response.status(HttpStatusCode.NotFound)
                } else {
                    val users = userService.search(searchValue)
                    val response = users.map { UserDTO.from(it) }
                    call.respond(response)
                }
            }

        }
    }
}