package com.elanor.controllers

import com.elanor.controllers.dto.GeneratorIdGrainDTO
import com.elanor.controllers.dto.IdDTO
import com.elanor.controllers.dto.ItemDTO
import com.elanor.controllers.dto.ItemUpdateDTO
import com.elanor.dao.model.authors.AuthorsDao
import com.elanor.dao.model.entity.Item
import com.elanor.dao.model.generators.GeneratorsDao
import com.elanor.dao.model.items.ItemsDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.lang.NullPointerException
import kotlin.random.Random

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
            if (!isAuthorOfGenerator(item.generatorId)) return
            itemsDao.insertItem(item)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, e.message ?: "not inserted")
        }
        call.respond(HttpStatusCode.OK, "item inserted")
    }

    suspend fun insertItems() {
        val items = call.receive<List<ItemDTO>>()
        val grains = items.map { it.grain }.toSet()
        if (items.size > grains.size) return call.respond(HttpStatusCode.BadRequest, "some grains duplicate")
        var count = 0
        try {
            val generatorId: Int = items[0].generatorId
            if (!isAuthorOfGenerator(generatorId)) return
            items.forEach {
                if (it.generatorId == generatorId) {
                    ItemsDao.insertItem(it)
                    count++
                }
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.NotFound, e.message.toString())
            return
        }

        call.respond(HttpStatusCode.OK, "inserted $count items")

    }

    suspend fun deleteItemsByGeneratorId() {
        val id = call.receive<IdDTO>().id
        call.respond(HttpStatusCode.OK, itemsDao.deleteItemsByGeneratorId(id))
    }

    suspend fun deleteItemsByGeneratorIdAndGrain() {
        val cr = call.receive<GeneratorIdGrainDTO>()
        try {
            if (!isAuthorOfGenerator(cr.generatorId)) return
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
        val items = call.receive<List<ItemUpdateDTO>>()
        var count = 0
        try {
            val generatorId: Int = items[0].generatorId
            if (!isAuthorOfGenerator(generatorId)) return
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
        val item = call.receive<ItemUpdateDTO>()
        try {
            if (!isAuthorOfGenerator(item.generatorId)) return
            ItemsDao.updateAll(item)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.NotFound, e.message.toString())
            return
        }
        call.respond(HttpStatusCode.OK, "update item")
    }

    suspend fun getRandomItemByGeneratorIdAndGrain() {
        val item = call.receive<IdDTO>()
        val res = try{
            val grain = ItemsDao.getAllGrains(item.id).random(Random)
            ItemsDao.getItemByGeneratorIdAndGrain(item.id, grain) ?: throw NullPointerException("no such item")
        }
        catch (e: Exception){
            call.respond(HttpStatusCode.BadRequest, e.message.toString())
            return
        }

        call.respond(HttpStatusCode.OK, res)
    }

    suspend fun getAllGrainsOfGenerator(){
        val item = call.receive<IdDTO>()
        val res = try{ ItemsDao.getAllGrains(item.id).map{ IdDTO(it) } }
        catch (e: Exception){
            call.respond(HttpStatusCode.BadRequest, e.message.toString())
            return
        }
        call.respond(HttpStatusCode.OK, res)
    }

    private suspend fun isAuthorOfGenerator(generatorId: Int): Boolean {
        val token: String =
            (call.request.headers["token"] ?: call.respond(HttpStatusCode.BadRequest, "no authorised user")
                .also { return false }) as String
        if (!GeneratorsDao.isAuthorHasGenerator(generatorId, AuthorsDao.idByToken(token) ?: return false)
//            && !AuthorsDao.isAdminToken(token)
        )
            call.respond(HttpStatusCode.BadRequest, "user is no author or admin")
                .also { return false }
        return true
    }


}