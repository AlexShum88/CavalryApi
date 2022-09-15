package com.elanor.controllers.dto

import kotlinx.serialization.Serializable

@Serializable
data class GeneratorUpdateDTO(
    val id: Int,
    val name: String? = null,
    val description: String? = null

) {

}
