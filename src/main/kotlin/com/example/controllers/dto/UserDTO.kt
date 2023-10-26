package com.example.controllers.dto

import com.example.business.User
import com.example.persistance.UserEntity
import com.example.social_network.controllers.SignUpRequest
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class UserDTO(
    var id: String,
    val email: String? = null,
    val password: String? = null,
    private val firstname: String,
    private val friends: List<String>? = emptyList(),
    private val lastname: String,
    private val profileImgUrl: String?){

    companion object{
        fun from (user: User): UserDTO {
            return UserDTO(
                id = user.id!!,
                firstname = user.firstname,
                friends = user.friends,
                lastname = user.lastname,
                profileImgUrl = user.profileImgUrl
            )
        }
    }
}
