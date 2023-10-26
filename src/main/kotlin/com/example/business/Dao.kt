package com.example.business

import kotlinx.serialization.json.Json

interface Dao {
    val jsonFormatter: Json
        get() = Json { ignoreUnknownKeys = true }
}