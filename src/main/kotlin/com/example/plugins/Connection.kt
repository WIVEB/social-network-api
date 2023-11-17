package com.example.plugins

import io.ktor.websocket.*

class Connection(
    val session: DefaultWebSocketSession,
    val userEmail: String,
    val conversationId: String
)