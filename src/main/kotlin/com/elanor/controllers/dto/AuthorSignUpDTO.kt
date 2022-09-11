package com.elanor.controllers.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthorSignUpDTO(
    val name: String,
    val login: String,
    val password: String,
    val isAdmin: Boolean
) {

}
