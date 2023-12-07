package com.example.controllers.dto

import com.example.business.models.User
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    var id: String? = null,
    val email: String? = null,
    val password: String? = null,
    val firstname: String? = null,
    val friends: List<String>? = emptyList(),
    val lastname: String? = null,
    val profileImgUrl: String? = null){

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
