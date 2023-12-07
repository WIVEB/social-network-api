package com.example.business.models

import kotlinx.datetime.LocalDateTime

class Message (
    var id: String?,
    var author: User,
    val createdAt: LocalDateTime,
    val text: String?,
    val images: List<String>?,
    val isDeleted: Boolean
)