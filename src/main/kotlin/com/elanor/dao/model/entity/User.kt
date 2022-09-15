package com.elanor.dao.model.entity

data class User(
    val id: Int,
    val login: String,
    val password: String
): Entity
