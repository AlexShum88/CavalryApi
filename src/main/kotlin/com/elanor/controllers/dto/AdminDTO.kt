package com.elanor.controllers.dto

import kotlinx.serialization.Serializable

@Serializable
data class AdminDTO(
    val id: Int,
    val isAdmin: Boolean
)
