package com.elanor.controllers.dto

import kotlinx.serialization.Serializable

@Serializable
data class GeneratorIdGrainDTO(
    val generatorId: Int,
    val grain: Int
)