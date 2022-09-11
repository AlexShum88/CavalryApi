package com.elanor.controllers

import com.elanor.controllers.dto.AuthorSignUpDTO
import com.elanor.controllers.dto.IdDTO
import com.elanor.controllers.dto.IdNameDTO
import com.elanor.controllers.dto.UserLogInDTO
import com.elanor.dao.model.authors.AuthorsDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class AuthorsController(val call: ApplicationCall) : AuthorsThemesController(call, AuthorsDao) {

    suspend fun insert() {
        val author = call.receive<AuthorSignUpDTO>()
        val res = try {
            AuthorsDao.insertAuthor(author)
            "author added"
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.toString())
            return
        }
        call.respond(HttpStatusCode.OK, res)
    }

    suspend fun logIn() {
        val user = call.receive<UserLogInDTO>()
        val res = AuthorsDao.login(user.login, user.password) ?: call.respond(
            HttpStatusCode.BadRequest,
            "wrong login or password"
        ).also { return }
        call.respond(HttpStatusCode.OK, res)
    }

    suspend fun updateName() {
        val idn = call.receive<IdNameDTO>()
        if (!isCurrentUser(idn.id)) return
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

    suspend fun delete() {
        val id = call.receive<IdDTO>().id
        if (!isCurrentUser(id)) return
        val delete = try {
            AuthorsDao.delete(id)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, "${e.message}").also { return }
        }
        call.respond(HttpStatusCode.OK, delete)
    }

    private suspend fun isCurrentUser(id: Int): Boolean {
        val token =
            call.request.headers["token"] ?: call.respond(HttpStatusCode.BadRequest, "no authorised user (null token)")
                .also { return false }
        if (AuthorsDao.tokenById(id) != token) call.respond(HttpStatusCode.BadRequest, "no authorised user")
            .also { return false }
        return true
    }


}

