package com.example.social_network.controllers

import com.example.business.AuthenticationDao
import com.example.business.User
import com.example.social_network.business.UserDao
import com.mongodb.MongoException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.security.Principal

@kotlinx.serialization.Serializable
data class SignUpRequest(val firstname: String, val lastname: String, val email: String, val password: String)

fun Route.authenticationController(authenticationDao: AuthenticationDao) {
    route("/signin") {
        authenticate("auth_basic") {
            get() {
                call.response.status(HttpStatusCode.OK)
            }
        }
    }
    route("/signup") {
        post() {
            val signUpRequest = call.receive<SignUpRequest>()
            try {
                authenticationDao.signUp(signUpRequest)
                call.response.status(HttpStatusCode.OK)
            } catch (mongoError: MongoException) {
                when (mongoError.code) {
                    11000 -> {
                        call.response.status(HttpStatusCode.Conflict)
                        call.respond("User already exists with this email")
                    }

                    else -> throw mongoError
                }
            } catch (e: Error) {
                throw e
            }
        }
    }
}