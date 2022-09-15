package com.elanor.controllers

import com.elanor.controllers.dto.*
import com.elanor.dao.model.authors.AuthorsDao
import com.elanor.dao.model.entity.Generator
import com.elanor.dao.model.generators.GeneratorsDao
import com.elanor.dao.model.items.ItemsDao
import com.elanor.dao.model.themes.ThemesDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class GeneratorsController(val call: ApplicationCall) {
    private val generators = GeneratorsDao


    suspend fun getAllGenerators() {
        call.respond(HttpStatusCode.OK, generators.getGeneratorsFacade())
    }

    suspend fun insertGenerator() {
        val id = isAuthor()
        val generator = call.receive<GeneratorInsertDTO>()
        if (id < 0 || generator.authorId != id) call.respond(
            HttpStatusCode.BadRequest,
            "author.id != generator.authorId"
        ).also { return }
        if (checkGenerator(generator)) {
            val gid = GeneratorsDao.insertGenerator(generator)

            call.respond(HttpStatusCode.OK, "generator $gid inserted")
        }
    }

    suspend fun selectGeneratorById() {
        val id = call.receive<IdDTO>().id
        val generator: Generator = generators.selectGeneratorById(id) ?: call.respond(
            HttpStatusCode.NotFound,
            "cant find this id"
        ) as Generator
        call.respond(HttpStatusCode.OK, generator)
    }

    suspend fun selectGeneratorByName() {
        val name = call.receive<NameDTO>().name
        val generator: Generator =
            (generators.selectGeneratorByName(name) ?: call.respond(
                HttpStatusCode.NotFound,
                "cant find this name"
            )) as Generator
        val generatorWithItems = GeneratorThemeItemsTDO(
            generator = generator,
            theme = ThemesDao.selecctThemeById(generator.themeId) ?: return call.respond(
                HttpStatusCode.BadRequest,
                "no such theme"
            ),
            items = ItemsDao.getItemsByGeneratorId(generator.id)
        )
        call.respond(HttpStatusCode.OK, generatorWithItems)
    }

    suspend fun selectGeneratorsByAuthor() {
        val authorId = call.receive<IdDTO>().id
        val generators: List<Generator> = try {
            generators.selectGeneratorsByAuthor(authorId)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.NotFound, "no such id")
            return
        }
        call.respond(HttpStatusCode.OK, generators)
    }

    suspend fun selectGeneratorsByTheme() {
        val themeId = call.receive<IdDTO>().id
        val generators: List<Generator> = try {
            generators.selectGeneratorsByAuthor(themeId)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.NotFound, "no such id")
            return
        }
        call.respond(HttpStatusCode.OK, generators)
    }

    suspend fun getGeneratorsFacade() {
        call.respond(HttpStatusCode.OK, GeneratorsDao.getGeneratorsFacade())
    }

    suspend fun editGenerator() {
        val generator = call.receive<GeneratorUpdateDTO>()
        if (!isAuthorOfGenerator(generatorId = generator.id)) return

        generators.selectGeneratorById(generator.id) ?: return call.respond(
            HttpStatusCode.BadRequest,
            "no such generator"
        )
        if (!generator.name.isNullOrBlank() &&
            generators.selectGeneratorByName(generator.name) != null
        ) return call.respond(HttpStatusCode.BadRequest, "this name is already using. Rename generator")


        try {
            GeneratorsDao.updateGenerator(generator)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, e.message.toString())
            return
        }
        call.respond(HttpStatusCode.OK, "generator updated")
    }

    suspend fun deleteGenerator() {
        val id = call.receive<IdDTO>().id
        if (!isAuthorOfGenerator(id)) return
        GeneratorsDao.deleteGenerator(id)
        call.respond(HttpStatusCode.OK, "generator $id deleted")
    }

    private suspend fun checkGenerator(generator: GeneratorInsertDTO): Boolean {

        if (generators.selectGeneratorByName(generator.name) != null) {
            call.respond(
                HttpStatusCode.BadRequest,
                "rename generator"
            )
            return false
        }

        if (!generators.isAuthorPresent(generator.authorId)) {
            call.respond(
                HttpStatusCode.BadRequest,
                "no such author"
            )
            return false
        }
        if (!generators.isThemePresent(generator.themeId)) {
            call.respond(HttpStatusCode.BadRequest, "no such theme")
            return false
        }

        return true
    }

    private suspend fun isAuthor(): Int {
        val token = call.request.headers["token"] ?: call.respond(HttpStatusCode.BadRequest, "no authorised user null")
            .also { return -1 }
        val id = AuthorsDao.idByToken(token as String)
        if (id == null) call.respond(HttpStatusCode.BadRequest, "no authorised user")
            .also { return -1 }
        return id ?: -1
    }

    private suspend fun isAuthorOfGenerator(generatorId: Int): Boolean {
        val token: String =
            (call.request.headers["token"] ?: call.respond(HttpStatusCode.BadRequest, "no authorised user")
                .also { return false }) as String
        if (!GeneratorsDao.isAuthorHasGenerator(generatorId, AuthorsDao.idByToken(token) ?: return false)
//            && !AuthorsDao.isAdminToken(token)
        )
            call.respond(HttpStatusCode.BadRequest, "this user is not author of this generator")
                .also { return false }
        return true
    }
}