package com.example

import com.example.business.AuthenticationDao
import com.example.persistance.MongoDBClient
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import com.typesafe.config.ConfigFactory
import io.ktor.network.tls.certificates.*
import io.ktor.server.config.*
import org.slf4j.*
import java.io.*

fun main(args: Array<String>) {
    val keyStoreFile = File("build/keystore.jks")
    val keyStore = buildKeyStore {
        certificate("sampleAlias") {
            password = "foobar"
            domains = listOf("127.0.0.1", "0.0.0.0", "localhost")
        }
    }
    keyStore.saveToFile(keyStoreFile, "123456")

    val environment = args.firstOrNull()

    embeddedServer(Netty, environment = applicationEngineEnvironment{
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
        module {
            val configFileName = environment?.let {
                "application-$environment.conf"
            } ?: "application-local.conf"

            val config = HoconApplicationConfig(ConfigFactory.load(configFileName))
            val mongoDBClient = MongoDBClient(config.property("ktor.mongo.url").getString(), "social_network")


            configureAuthentication(AuthenticationDao(mongoDBClient))
            configureSockets()
            configureRouting(mongoDBClient)
            configureSerialization()
        }
    })
        .start(wait = true)
}
