package com.elanor.controllers.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val login: String,
    val password: String,
)
