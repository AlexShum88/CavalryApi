package com.elanor.dao.model.authors

import com.elanor.dao.model.users.Users

private const val cryptor = 12

fun Users.encryptPassword(password: String): String {
    return password.map {
        it.toInt() + cryptor
    }.toString()
}

fun Users.decryptPassword(crypt: String): String {
    val strB = StringBuilder()
    crypt.filter { it!='[' && it!=']' }.split(", ") .map {
        it.toInt() - cryptor
    }.map {
        it.toChar()
    }.forEach {
        strB.append(it)
    }

    return strB.toString()
}