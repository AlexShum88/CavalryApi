package com.elanor.routings

import com.elanor.controllers.AuthorsController
import com.elanor.controllers.ThemesController
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Routing.themesRouting() {
    route("themes") {
        getAllThemes()
        insertTheme()//
//        insertCleanTheme()//
        selectThemeIdByName()
        selectThemeNameById()
        updateThemeName()//
        deleteTheme()//
        updateTheme()//
    }
}

private fun Route.getAllThemes() {
    get("all") {
        ThemesController(call).selectAll()
    }
}

//private fun Route.insertCleanTheme() {
//    post("insertCleanTheme") {
//        ThemesController(call).insert()
//    }
//}

private fun Route.insertTheme() {
    post("insert") {
        ThemesController(call).insertTheme()
    }
}

private fun Route.selectThemeNameById() {
    get("byId") {
        ThemesController(call).selectNameById()
    }
}

private fun Route.selectThemeIdByName() {
    get("byName") {
        ThemesController(call).selectIdByName()
    }
}

private fun Route.deleteTheme(){
    delete("delete") {
        ThemesController(call).delete()
    }
}

private fun Route.updateThemeName(){
    post("updateName") {
        ThemesController(call).updateName()
    }

}

private fun Route.updateTheme(){
    post("updateTheme") {
        ThemesController(call).updateTheme()
    }
}