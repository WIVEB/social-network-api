package com.example.controllers

import com.example.controllers.dto.ChatDTO
import com.example.controllers.dto.UserDTO
import com.example.services.ChatService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable()
data class AddConversationMessageDTO(
    val text: String? = "",
    val images: List<String>? = emptyList()
)

@Serializable()
data class AddConversationDTO(
    val conversationMembers: List<String> = emptyList(),
    val message: AddConversationMessageDTO
)

fun Route.chatController(chatService: ChatService) {
    authenticate("auth_basic") {
        route("/chat") {
            get {
                val userEmail = call.principal<UserIdPrincipal>()?.name!!
                try {
                    val userConversations = chatService.getUserConversations(userEmail)
                    val response = userConversations.map { ChatDTO.from(it) }
                    call.respond(HttpStatusCode.OK, Json.encodeToString(response))
                } catch (e: Error) {
                    call.respond(HttpStatusCode.InternalServerError, Json.encodeToString(e))
                }
            }

            get("/{id}") {
                val requestUserEmail = call.principal<UserIdPrincipal>()?.name!!
                val conversationId = call.parameters["id"]!!
                chatService.getUserConversation(requestUserEmail, conversationId)
            }

            post {
                val addConversationDTO = call.receive<AddConversationDTO>()
                val userEmail = call.principal<UserIdPrincipal>()?.name!!
                try {
                    chatService.createConversation(userEmail, addConversationDTO)
                    call.respond(HttpStatusCode.OK)
                } catch (e: Error) {
                    throw e
                }
            }
            post("/{id}/message") {
                val addConversationMessageDTO = call.receive<AddConversationMessageDTO>()
                val userEmail = call.principal<UserIdPrincipal>()?.name!!
                val chatId = call.parameters["id"]!!
                try {
                    chatService.addConversationMessage(userEmail, chatId, addConversationMessageDTO)
                    call.response.status(HttpStatusCode.OK)
                } catch (e: Error) {
                    throw e
                }
            }
        }
    }
}