package com.elanor.plugins

import com.elanor.controllers.AuthorsController

import com.elanor.routings.authorsRouting
import com.elanor.routings.generatorsRouting
import com.elanor.routings.themesRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("getAuthorsAndThemes"){
            get {
                AuthorsController(call).getAuthorsAndThemes()
            }
        }

        authorsRouting()
        themesRouting()
        generatorsRouting()

    }
}
