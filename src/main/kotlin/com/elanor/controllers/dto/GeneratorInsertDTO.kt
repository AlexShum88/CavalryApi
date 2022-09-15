package com.elanor.controllers.dto

import kotlinx.serialization.Serializable

@Serializable
data class GeneratorInsertDTO(
    val name: String,
    val authorId: Int,
    val themeId: Int,
    val description: String? = null
)
