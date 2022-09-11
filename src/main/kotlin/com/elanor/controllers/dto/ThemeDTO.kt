package com.elanor.controllers.dto

import kotlinx.serialization.Serializable

@Serializable
data class ThemeDTO (
    val name: String,
    val param1: String? = null,
    val param2: String? = null,
    val param3: String? = null,
    val param4: String? = null,
    val param5: String? = null,
    val param6: String? = null,
)
