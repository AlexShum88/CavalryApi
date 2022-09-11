package com.elanor.controllers.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthorIdTokenDTO(
    val id: Int,
    val token: String
)
