package com.example.business.models

import kotlinx.datetime.LocalDateTime

data class Post(
    var id: String?,
    var user: User,
    val createdAt: LocalDateTime,
    val text: String?,
    val images: List<String>?,
)