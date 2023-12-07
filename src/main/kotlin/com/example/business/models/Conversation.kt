package com.example.business.models

import kotlinx.datetime.LocalDateTime

class Conversation (
    var id: String? = null,
    var users: List<User>,
    val name: String? = null,
    val createdAt: LocalDateTime,
    val creator: User,
    val messages: List<Message>? = null,
    val thumbnail: String? = null,
)