package com.elanor.plugins

import com.elanor.controllers.AuthorsController

import com.elanor.routings.authorsRouting
import com.elanor.routings.generatorsRouting
import com.elanor.routings.themesRouting
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.reflect.*

/**
* you are here
*/
fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondRedirect("/map/index.html")
        }
            static("/map") {
                resources("map")
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
