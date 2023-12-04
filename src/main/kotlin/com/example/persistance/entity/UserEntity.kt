package com.example.persistance.entity

import com.example.business.User
import com.example.controllers.dto.UserDTO
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class UserEntity(
    var id: String,
    val email: String? = null,
    val password: String? = null,
    private val firstname: String,
    private val friends: List<String>? = emptyList(),
    private val lastname: String,
    private val profileImgUrl: String?){

    companion object{
        fun from (user: User): UserEntity {
            return UserEntity(
                id = user.id!!,
                firstname = user.firstname,
                friends = user.friends,
                lastname = user.lastname,
                profileImgUrl = user.profileImgUrl
            )
        }
        fun from (signUpRequest: UserDTO): UserEntity {
            return UserEntity(
                id = UUID.randomUUID().toString(),
                email = signUpRequest.email,
                password = signUpRequest.password,
                firstname = signUpRequest.firstname,
                lastname = signUpRequest.lastname,
                friends = emptyList(),
                profileImgUrl = ""
            )
        }
    }
    fun toUser(): User {
        return User(this.id, this.firstname, this.lastname, this.profileImgUrl!!, this.friends!!, null)
    }
}