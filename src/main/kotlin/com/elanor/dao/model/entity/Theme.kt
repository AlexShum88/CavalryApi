package com.elanor.dao.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class Theme(
    val id: Int,
    val name: String,
    val param1: String? = null,
    val param2: String? = null,
    val param3: String? = null,
    val param4: String? = null,
    val param5: String? = null,
    val param6: String? = null,
    val description: String? = null
) : Entity
