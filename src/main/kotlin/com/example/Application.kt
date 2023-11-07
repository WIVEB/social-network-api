package com.example

import com.example.business.AuthenticationDao
import com.example.persistance.MongoDBClient
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import io.ktor.network.tls.certificates.*
import org.slf4j.*
import java.io.*

fun main() {
    val keyStoreFile = File("build/keystore.jks")
    val keyStore = buildKeyStore {
        certificate("sampleAlias") {
            password = "foobar"
            domains = listOf("127.0.0.1", "0.0.0.0", "localhost")
        }
    }
    keyStore.saveToFile(keyStoreFile, "123456")

    val environment = applicationEngineEnvironment {
        log = LoggerFactory.getLogger("ktor.application")
        connector {
            port = 8080
        }
        sslConnector(
            keyStore = keyStore,
            keyAlias = "sampleAlias",
            keyStorePassword = { "123456".toCharArray() },
            privateKeyPassword = { "foobar".toCharArray() }) {
            port = 8443
            keyStorePath = keyStoreFile
        }
        module(Application::module)
    }

    embeddedServer(Netty, environment)
        .start(wait = true)
}

fun Application.module() {
    val mongoDBClient = MongoDBClient("mongodb://192.169.18.2:27017", "social_network")
    configureAuthentication(AuthenticationDao(mongoDBClient))
    configureRouting(mongoDBClient)
    configureSerialization()
}
