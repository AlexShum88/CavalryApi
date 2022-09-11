package com.elanor.dao.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class Generator(
    val id :Int,
    val name: String,
    val authorId: Int,
    val themeId: Int,
    val minVal: Int,
    val maxVal: Int,
): Entity
