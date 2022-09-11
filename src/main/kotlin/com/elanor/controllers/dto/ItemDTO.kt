package com.elanor.controllers.dto

import kotlinx.serialization.Serializable

@Serializable
data class ItemDTO(
    val generatorId: Int,
    val grain: Int,
    val text: String,
    val description: String? = null,
    val param1: String? = null,
    val param2: String? = null,
    val param3: String? = null,
    val param4: String? = null,
    val param5: String? = null,
    val param6: String? = null,
)


