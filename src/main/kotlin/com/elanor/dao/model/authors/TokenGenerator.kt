package com.elanor.dao.model.authors

import java.util.*

fun tokenGenerator(login: String)  =
    UUID.nameUUIDFromBytes(login.toByteArray())

