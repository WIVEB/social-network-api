package com.example.business.models

import com.example.business.models.Post

data class User(
    var id: String?,
    val firstname: String,
    val lastname: String,
    val profileImgUrl: String?,
    val friends: List<String>,
    val posts: List<Post>?
)
