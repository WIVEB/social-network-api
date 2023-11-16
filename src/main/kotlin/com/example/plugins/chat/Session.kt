package com.example.plugins.chat

import io.ktor.websocket.*

data class Session (val id: String, val conversationId: String, val userId: String, val socket: WebSocketSession)