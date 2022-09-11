package com.elanor.controllers.dto

import kotlinx.serialization.Serializable

@Serializable
data class GeneratorIdGrainTextDTO(
    val generatorId: Int,
    val grain: Int,
    val text: String
) {

}