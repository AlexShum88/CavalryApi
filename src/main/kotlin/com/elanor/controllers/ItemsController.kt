package com.elanor.controllers

import com.elanor.controllers.dto.GeneratorIdGrainDTO
import com.elanor.controllers.dto.GeneratorIdGrainTextDTO
import com.elanor.controllers.dto.IdDTO
import com.elanor.controllers.dto.ItemDTO
import com.elanor.dao.model.authors.AuthorsDao
import com.elanor.dao.model.entity.Item
import com.elanor.dao.model.generators.GeneratorsDao
import com.elanor.dao.model.items.ItemsDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class ItemsController(
    val call: ApplicationCall
) {
    private val itemsDao = ItemsDao

    suspend fun getItemsByGeneratorId() {
        val gId = call.receive<IdDTO>().id
        call.respond(HttpStatusCode.OK, itemsDao.getItemsByGeneratorId(gId))
    }

    suspend fun getItemByGeneratorIdAndGrain() {
        val cr = call.receive<GeneratorIdGrainDTO>()
        val generatorId = cr.generatorId
        val grain = cr.grain
        val res: Item = itemsDao.getItemByGeneratorIdAndGrain(generatorId, grain) ?: return call.respond(HttpStatusCode.NotFound)
        call.respond(HttpStatusCode.OK, res)
    }

    suspend fun insertItem() {
        val item = call.receive<ItemDTO>()
        try {
            if (!isAuthorOfGeneratorOrAdmin(item.generatorId)) return
            itemsDao.insertItem(item)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, e.message ?: "not inserted")
        }
        call.respond(HttpStatusCode.OK, "item inserted")
    }

    suspend fun deleteItemsByGeneratorId() {
        val id = call.receive<IdDTO>().id
        call.respond(HttpStatusCode.OK, itemsDao.deleteItemsByGeneratorId(id))
    }

    suspend fun deleteItemsByGeneratorIdAndGrain() {
        val cr = call.receive<GeneratorIdGrainDTO>()
        try {
            if (!isAuthorOfGeneratorOrAdmin(cr.generatorId)) return
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message.toString())
        }

        call.respond(HttpStatusCode.OK, itemsDao.deleteItemsByGeneratorIdAndGrain(cr.generatorId, cr.grain))
    }

//
//    suspend fun updateItemText() {
//        val cr = call.receive<GeneratorIdGrainTextDTO>()
//        call.respond(HttpStatusCode.OK, itemsDao.updateItemText(cr.generatorId, cr.grain, cr.text))
//    }
//
//    suspend fun updateItemDescription() {
//        val cr = call.receive<GeneratorIdGrainTextDTO>()
//        call.respond(HttpStatusCode.OK, itemsDao.updateItemDescription(cr.generatorId, cr.grain, cr.text))
//    }
//
//    suspend fun updateItemParam1() {
//        val cr = call.receive<GeneratorIdGrainTextDTO>()
//        call.respond(HttpStatusCode.OK, itemsDao.updateItemParam1(cr.generatorId, cr.grain, cr.text))
//    }
//
//    suspend fun updateItemParam2() {
//        val cr = call.receive<GeneratorIdGrainTextDTO>()
//        call.respond(HttpStatusCode.OK, itemsDao.updateItemParam2(cr.generatorId, cr.grain, cr.text))
//    }
//
//    suspend fun updateItemParam3() {
//        val cr = call.receive<GeneratorIdGrainTextDTO>()
//        call.respond(HttpStatusCode.OK, itemsDao.updateItemParam3(cr.generatorId, cr.grain, cr.text))
//    }
//
//    suspend fun updateItemParam4() {
//        val cr = call.receive<GeneratorIdGrainTextDTO>()
//        call.respond(HttpStatusCode.OK, itemsDao.updateItemParam4(cr.generatorId, cr.grain, cr.text))
//    }
//
//    suspend fun updateItemParam5() {
//        val cr = call.receive<GeneratorIdGrainTextDTO>()
//        call.respond(HttpStatusCode.OK, itemsDao.updateItemParam5(cr.generatorId, cr.grain, cr.text))
//    }
//
//    suspend fun updateItemParam6() {
//        val cr = call.receive<GeneratorIdGrainTextDTO>()
//        call.respond(HttpStatusCode.OK, itemsDao.updateItemParam6(cr.generatorId, cr.grain, cr.text))
//    }

    suspend fun updateAll() {
        val items = call.receive<List<ItemDTO>>()
        var count = 0
        try {
            val generatorId: Int = items[0].generatorId
            if (!isAuthorOfGeneratorOrAdmin(generatorId)) return
            items.forEach {
                if (it.generatorId == generatorId) {
                    ItemsDao.updateAll(it)
                    count++
                }
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.NotFound, e.message.toString())
            return
        }

        call.respond(HttpStatusCode.OK, "updated $count items")
    }

    suspend fun update() {
        val item = call.receive<ItemDTO>()
        try {
            if (!isAuthorOfGeneratorOrAdmin(item.generatorId)) return
            ItemsDao.updateAll(item)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.NotFound, e.message.toString())
            return
        }
        call.respond(HttpStatusCode.OK, "update item")
    }

    private suspend fun isAuthorOfGeneratorOrAdmin(generatorId: Int): Boolean {
        val token: String =
            (call.request.headers["token"] ?: call.respond(HttpStatusCode.BadRequest, "no authorised user")
                .also { return false }) as String
        if (!GeneratorsDao.isAuthorHasGenerator(generatorId, AuthorsDao.idByToken(token) ?: return false)
            && !AuthorsDao.isAdminToken(token)
        )
            call.respond(HttpStatusCode.BadRequest, "user is no author or admin")
                .also { return false }
        return true
    }
}