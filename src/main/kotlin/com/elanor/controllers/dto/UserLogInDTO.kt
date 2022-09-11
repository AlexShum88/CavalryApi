package com.elanor.controllers.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserLogInDTO(
    val login: String,
    val password: String
)
