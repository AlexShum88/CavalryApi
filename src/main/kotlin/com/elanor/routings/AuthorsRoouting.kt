package com.elanor.routings

import com.elanor.controllers.AuthorsController
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*


fun Routing.authorsRouting(){
    route("authors"){
        getAllAuthors()
        insertAuthor()
//        selectIdByName()
        selectNameById()
//        deleteAuthor()
        updateAuthorName()
        logIn()
        updateAdminStatus()
    }
}

private fun Route.getAllAuthors(){
    get("all") {
        AuthorsController(call).selectAll()
    }
}

private fun Route.insertAuthor(){
    post("signUp") {
       AuthorsController(call).insert()
    }
}
private fun Route.logIn(){
    post("logIn") {
       AuthorsController(call).logIn()
    }
}

private fun Route.selectNameById(){
    get("byId"){
        AuthorsController(call).selectNameById()
    }
}

private fun Route.selectIdByName(){
    get("byName"){
        AuthorsController(call).selectIdByName()
    }
}

private fun Route.deleteAuthor(){
    delete("delete") {
        AuthorsController(call).delete()
    }
}

private fun Route.updateAuthorName(){
    post("updateName") {
        AuthorsController(call).updateName()
    }
}

private fun Route.updateAdminStatus(){
    post("updateAdmin") {
        AuthorsController(call).updateToAdmin()
    }
}