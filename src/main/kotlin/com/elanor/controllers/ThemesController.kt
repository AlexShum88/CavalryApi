package com.elanor.controllers

import com.elanor.controllers.dto.IdDTO
import com.elanor.controllers.dto.IdNameDTO
import com.elanor.controllers.dto.ThemeDTO
import com.elanor.dao.model.authors.AuthorsDao
import com.elanor.dao.model.entity.Theme
import com.elanor.dao.model.themes.ThemesDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class ThemesController(val call: ApplicationCall) : AuthorsThemesController(call, ThemesDao) {

    suspend fun insertTheme() {
        val theme = call.receive<ThemeDTO>()
        if (!checkTokenIsAdmin()) return
        val resultId = try {
            ThemesDao.insertTheme(theme)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message.toString())
            return
        }
        call.respond(HttpStatusCode.OK, resultId)
    }

    suspend fun updateTheme() {
        val theme = call.receive<Theme>()
        if (!checkTokenIsAdmin()) return
        val result = try {
            ThemesDao.updateAll(theme)
            "theme is updated"
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message.toString())
            return
        }
        call.respond(HttpStatusCode.OK, result)
    }

    suspend fun updateName() {
        if (!checkTokenIsAdmin()) return
        val idn = call.receive<IdNameDTO>()

        if (idn.name.isBlank()) {
            call.respond(HttpStatusCode.BadRequest, "name cant be blank")
            return
        }
        val update = try {
            ThemesDao.updateName(idn.id, idn.name)
            "name is updated"
        } catch (e: Exception) {
            call.respond(HttpStatusCode.NotFound, e.message.toString())
            return
        }
        call.respond(HttpStatusCode.OK, update)
    }

    suspend fun delete() {
        if (!checkTokenIsAdmin()) return
        val id = call.receive<IdDTO>().id
        val delete = try {
            ThemesDao.delete(id)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, e.message.toString()).also { return }
        }
        call.respond(HttpStatusCode.OK, delete)
    }


    private suspend fun checkTokenIsAdmin(): Boolean {
        val token = call.request.headers["token"] ?: call.respond(HttpStatusCode.BadRequest, "no authorised user")
            .also { return false }
        if (!AuthorsDao.isAdminToken(token as String)) call.respond(HttpStatusCode.BadRequest, "no admin user")
            .also { return false }
        return true
    }
}