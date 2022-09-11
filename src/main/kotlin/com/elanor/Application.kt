package com.elanor


import com.elanor.dao.DaoFactory
import com.elanor.plugins.configureRouting
import com.elanor.plugins.configureSerialization
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    DaoFactory.init()
    embeddedServer(Netty, port = System.getenv("PORT").toInt()) {
        configureSerialization()
        configureRouting()
    }.start(wait = true)


    //todo 1 - модернізувати, щоб генератоні діапазони були в конкретних межах (лізти в дб((()
        //todo 2 - додати юзерів (токени?), за якими вони авторизуються.
}
