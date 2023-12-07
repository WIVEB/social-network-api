package com.example.controllers

import com.example.business.AuthenticationDao
import com.example.controllers.dto.UserDTO
import com.mongodb.MongoException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authenticationController(authenticationDao: AuthenticationDao) {
    route("/signin") {
        authenticate("auth_basic") {
            get() {
                call.response.status(HttpStatusCode.OK)
            }
        }
    }
    route("/signup") {
        post<UserDTO>() {
            try {
                validateEmail(it.email)
                authenticationDao.signUp(it)
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
const val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
fun validateEmail(email: String?) {
    if(email == null || !email.matches(emailRegex.toRegex())){
        throw Exception("Invalid email")
    }
}