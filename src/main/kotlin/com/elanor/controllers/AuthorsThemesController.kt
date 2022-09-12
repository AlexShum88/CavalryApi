package com.elanor.controllers

import com.elanor.controllers.dto.AuthorsThemesTDO
import com.elanor.controllers.dto.IdDTO
import com.elanor.controllers.dto.NameDTO
import com.elanor.dao.model.IDao
import com.elanor.dao.model.authors.AuthorsDao
import com.elanor.dao.model.themes.ThemesDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

open class AuthorsThemesController(
    private val call: ApplicationCall,
    private val iDao: IDao
) {


    suspend fun selectAll() {
        call.respond(HttpStatusCode.OK, iDao.selectAll())
    }

    suspend fun selectNameById() {
        val id = call.receive<IdDTO>().id
        val result: String = iDao.selectNameById(id) ?: return call.respond(HttpStatusCode.BadRequest, "no such in db")
        call.respond(HttpStatusCode.OK, NameDTO(result))
    }

    suspend fun selectIdByName() {
        val name = call.receive<NameDTO>().name
        if (name.isBlank()) call.respond(HttpStatusCode.BadRequest, "name cant be blank")
        val result: Int = iDao.selectIdByName(name) ?: return call.respond(HttpStatusCode.BadRequest, "no such in db")
        call.respond(HttpStatusCode.OK, IdDTO(result))
    }

    suspend fun getAuthorsAndThemes() {
        val auth = AuthorsThemesTDO(
            authors = AuthorsDao.selectAll(),
            themes = ThemesDao.selectAll()
        )
        call.respond(HttpStatusCode.OK, auth)
    }

}