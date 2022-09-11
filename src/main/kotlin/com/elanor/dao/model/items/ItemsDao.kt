package com.elanor.dao.model.items

import com.elanor.controllers.dto.ItemDTO
import com.elanor.dao.DaoFactory.dbQuery
import com.elanor.dao.model.entity.Item
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateStatement

object ItemsDao {
    suspend fun getItemsByGeneratorId(generatorId: Int): List<Item> = dbQuery {
        Items.select { Items.generatorId.eq(generatorId) }.map { Items.rowToEntity(it) as Item }
    }

    suspend fun getItemByGeneratorIdAndGrain(generatorId: Int, grain: Int): List<Item> = dbQuery {
        Items.select { Items.generatorId.eq(generatorId) and Items.grain.eq(grain) }
            .map { Items.rowToEntity(it) as Item }
    }

    suspend fun insertItem(item: ItemDTO): Int = dbQuery {
        Items.insert {

            it[generatorId] = item.generatorId
            it[grain] = item.grain
            it[text] = item.text
            it[description] = item.description
            it[param1] = item.param1
            it[param2] = item.param2
            it[param3] = item.param3
            it[param4] = item.param4
            it[param5] = item.param5
            it[param6] = item.param6
        }[Items.id]
    }

    suspend fun deleteItemsByGeneratorId(generatorId: Int): Boolean = dbQuery {
        Items.deleteWhere { Items.generatorId.eq(generatorId) } > 0
    }

    suspend fun deleteItemsByGeneratorIdAndGrain(generatorId: Int, grain: Int): Boolean = dbQuery {
        Items.deleteWhere { Items.generatorId.eq(generatorId) and Items.grain.eq(grain) } > 0
    }

    suspend fun updateItem(generatorId: Int, grain: Int, edit: Items.(UpdateStatement) -> Unit): Boolean =
        dbQuery {
            Items.update({ Items.generatorId.eq(generatorId) and Items.grain.eq(grain) }) {
                edit
            } > 0
        }

    suspend fun updateItemText(generatorId: Int, grain: Int, text: String): Boolean = dbQuery {
        updateItem(generatorId, grain) {
            it[Items.text] = text
        }
    }

    suspend fun updateItemDescription(generatorId: Int, grain: Int, description: String?): Boolean = dbQuery {
        updateItem(generatorId, grain) {
            it[Items.description] = description
        }
    }

    suspend fun updateItemParam1(generatorId: Int, grain: Int, param1: String?): Boolean = dbQuery {
        updateItem(generatorId, grain) {
            it[Items.param1] = param1
        }
    }

    suspend fun updateItemParam2(generatorId: Int, grain: Int, param2: String?): Boolean = dbQuery {
        updateItem(generatorId, grain) {
            it[Items.param2] = param2
        }
    }

    suspend fun updateItemParam3(generatorId: Int, grain: Int, param3: String?): Boolean = dbQuery {
        updateItem(generatorId, grain) {
            it[Items.param3] = param3
        }
    }

    suspend fun updateItemParam4(generatorId: Int, grain: Int, param4: String?): Boolean = dbQuery {
        updateItem(generatorId, grain) {
            it[Items.param4] = param4
        }
    }

    suspend fun updateItemParam5(generatorId: Int, grain: Int, param5: String?): Boolean = dbQuery {
        updateItem(generatorId, grain) {
            it[Items.param5] = param5
        }
    }

    suspend fun updateItemParam6(generatorId: Int, grain: Int, param6: String?): Boolean = dbQuery {
        updateItem(generatorId, grain) {
            it[Items.param6] = param6
        }
    }

    suspend fun updateAll(item: ItemDTO): Boolean = dbQuery {
        Items.update({ Items.generatorId.eq(item.generatorId) and Items.grain.eq(item.grain) }) {
            it[text] = item.text
            it[description] = item.description
            it[param1] = item.param1
            it[param2] = item.param2
            it[param3] = item.param3
            it[param4] = item.param4
            it[param5] = item.param5
            it[param6] = item.param6

        } > 0

    }
}