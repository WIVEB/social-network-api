package com.example.controllers

import com.example.controllers.dto.ChatDTO
import com.example.controllers.dto.ChatMessageDTO
import com.example.plugins.Connection
import com.example.services.ChatService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import kotlin.collections.LinkedHashSet

fun Route.chatController(chatService: ChatService) {
    authenticate("auth_basic") {
        route("/chat") {
            get {
                val userEmail = call.principal<UserIdPrincipal>()?.name!!
                try {
                    val userConversations = chatService.getUserConversations(userEmail)
                    val response = userConversations.map { ChatDTO.from(it) }
                    call.respond(HttpStatusCode.OK, response)
                } catch (e: Error) {
                    call.respond(HttpStatusCode.InternalServerError, Json.encodeToString(e))
                }
            }

            get("/{id}") {
                val requestUserEmail = call.principal<UserIdPrincipal>()?.name!!
                val conversationId = call.parameters["id"]!!
                try {
                    val userConversation = chatService.getUserConversation(requestUserEmail, conversationId)
                    val response = ChatDTO.from(userConversation)
                    call.respond(HttpStatusCode.OK, response)
                } catch (e: Error) {
                    call.respond(HttpStatusCode.InternalServerError, Json.encodeToString(e))
                }
            }

            post<ChatDTO> {
                val userEmail = call.principal<UserIdPrincipal>()?.name!!
                try {
                    chatService.createConversation(userEmail, it)
                    call.respond(HttpStatusCode.OK)
                } catch (e: Error) {
                    call.respond(HttpStatusCode.InternalServerError, Json.encodeToString(e))
                }
            }
            post<ChatMessageDTO>("/{id}/message") {
                val conversationMessageDTO = call.receive<ChatMessageDTO>()
                val userEmail = call.principal<UserIdPrincipal>()?.name!!
                val chatId = call.parameters["id"]!!
                try {
                    chatService.addConversationMessage(userEmail, chatId, conversationMessageDTO.text,
                        conversationMessageDTO.images)
                    call.respond(HttpStatusCode.OK)
                } catch (e: Error) {
                    call.respond(HttpStatusCode.InternalServerError, Json.encodeToString(e))
                }
            }

            val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
            webSocket("/{id}") {
                val requestUserEmail = call.principal<UserIdPrincipal>()?.name!!
                val conversationId = call.parameters["id"]!!

                validateUserIsMemberOfConversation(chatService, requestUserEmail, conversationId)

                val thisConnection = Connection(this, requestUserEmail, conversationId)
                connections += thisConnection

                try {
                    println("Connecting ${thisConnection.userEmail} from chat ${thisConnection.conversationId}!")

                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val message = Json.decodeFromString<ChatMessageDTO>(frame.readText())
                        val newMessage = chatService.addConversationMessage(
                            thisConnection.userEmail,
                            thisConnection.conversationId,
                            message.text,
                            message.images
                        )

                        connections.forEach {
                            it.session.send(Json.encodeToString(ChatMessageDTO.from(newMessage)))
                        }
                    }
                } catch (e: Exception) {
                    println(e.localizedMessage)
                } finally {
                    println("Removing ${thisConnection.userEmail} from chat ${thisConnection.conversationId}!")
                    connections -= thisConnection
                }
            }
        }
    }
}

private fun validateUserIsMemberOfConversation(
    chatService: ChatService,
    requestUserEmail: String,
    conversationId: String
) {
    chatService.getUserConversation(requestUserEmail, conversationId)
}