package com.elanor.dao.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class Author(
    override val id: Int,
    override val name: String,
    val login: String,
    val password: String,
    val token: String,
    val isAdmin: Boolean
): Entity, IdNameEntity