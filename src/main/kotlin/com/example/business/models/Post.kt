package com.example.business.models

import com.example.business.User
import kotlinx.datetime.LocalDateTime

data class Post(
    var id: String?,
    var user: User,
    val createdAt: LocalDateTime,
    val text: String?,
    val images: List<String>?,
)