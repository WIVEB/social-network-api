package com.example.plugins

import com.example.business.AuthenticationDao
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureAuthentication(authenticationDao: AuthenticationDao) {
    authentication {
        basic(name = "auth_basic") {

            realm = "Ktor Server"
            validate { credentials ->
                val userEntity = authenticationDao.findByEmail(credentials.name)
                if (userEntity?.email == credentials.name && userEntity.password == credentials.password) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }
}