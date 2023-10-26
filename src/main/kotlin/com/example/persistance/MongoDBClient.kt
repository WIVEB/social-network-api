package com.example.persistance

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase

class MongoDBClient(url: String, name: String) {
    val db: MongoDatabase = MongoClients.create(url).getDatabase(name)
}
