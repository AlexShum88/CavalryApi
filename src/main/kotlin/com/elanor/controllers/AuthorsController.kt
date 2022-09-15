package com.elanor.controllers

import com.elanor.controllers.dto.AdminDTO
import com.elanor.controllers.dto.IdDTO
import com.elanor.controllers.dto.IdNameDTO
import com.elanor.controllers.dto.UserDTO
import com.elanor.dao.model.authors.AuthorsDao
import com.elanor.dao.model.users.UsersDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class AuthorsController(val call: ApplicationCall) : AuthorsThemesController(call, AuthorsDao) {

    suspend fun insert() {
        val user = call.receive<UserDTO>()
        val res = try {
            UsersDao.signUp (user)
            "author added"
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.toString())
            return
        }
        call.respond(HttpStatusCode.OK, res)
    }

    suspend fun logIn() {
        val user = call.receive<UserDTO>()
        val res = UsersDao.logIn(user) ?: call.respond(
            HttpStatusCode.BadRequest,
            "wrong login or password"
        ).also { return }
        call.respond(HttpStatusCode.OK, res)
    }

    suspend fun updateName() {
        val idn = call.receive<IdNameDTO>()
        if (!isCurrentAuthor(idn.id)) return
        if (idn.name.isBlank()) {
            call.respond(HttpStatusCode.BadRequest, "name cant be blank")
            return
        }
        val update: String = try {
            AuthorsDao.updateName(idn.id, idn.name)
            "name is updated"
        } catch (e: Exception) {
            call.respond(HttpStatusCode.NotFound, e.message.toString())
            return
        }
        call.respond(HttpStatusCode.OK, update)
    }

    suspend fun updateToAdmin(){
        val user = call.receive<AdminDTO>()
        val token =
            call.request.headers["token"] ?: return call.respond(HttpStatusCode.BadRequest, "null token")
        if(!AuthorsDao.isAdminToken(token)) return call.respond(HttpStatusCode.Conflict, "only admin can change admin status")
        AuthorsDao.updateAdminStatus(user.id, user.isAdmin)
        call.respond(HttpStatusCode.OK, "status changed")
    }

    suspend fun delete() {
        val id = call.receive<IdDTO>().id
        if (!isCurrentAuthor(id)) return
        val delete = try {
            AuthorsDao.delete(id)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, "${e.message}").also { return }
        }
        call.respond(HttpStatusCode.OK, delete)
    }


    private suspend fun isCurrentAuthor(authorId: Int): Boolean {
        val token =
            call.request.headers["token"] ?: call.respond(HttpStatusCode.BadRequest, "null token")
                .also { return false }
        if (AuthorsDao.tokenById(authorId) != token) call.respond(HttpStatusCode.BadRequest, "no authorised author")
            .also { return false }
        return true
    }


}

