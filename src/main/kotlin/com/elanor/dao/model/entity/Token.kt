package com.elanor.dao.model.entity

data class Token(
    val id: Int,
    val token: String,
    val userId: Int
): Entity
