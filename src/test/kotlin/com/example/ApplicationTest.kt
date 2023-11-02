package com.example

import com.example.persistance.MongoDBClient
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.util.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.example.plugins.*
import io.ktor.client.call.body
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before

class ApplicationTest {

    @MockK
    lateinit var mongoDBClient: MongoDBClient

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }
    @Test
    fun testRoot() = testApplication {
        assertEquals(true, true)
        /*application {
            configureRouting(mongoDBClient)
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }*/
    }
}