package com.elanor.dao.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class Author(
    val id: Int,
    val name: String,
    val isAdmin: Boolean,
    val userId: Int
): Entity