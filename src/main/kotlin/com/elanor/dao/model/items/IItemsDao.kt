package com.elanor.dao.model.items

import com.elanor.dao.model.entity.Item
import org.jetbrains.exposed.sql.statements.UpdateStatement

interface IItemsDao {
    suspend fun getItemsByGeneratorId(generatorId: Int): List<Item>

    suspend fun getItemByGeneratorIdAndGrain(generatorId: Int, grain: Int): List<Item>

    suspend fun insertItem(item: Item): Int

    suspend fun deleteItemsByGeneratorId(generatorId: Int): Boolean

    suspend fun deleteItemsByGeneratorIdAndGrain(generatorId: Int, grain: Int): Boolean

    suspend fun updateItem(generatorId: Int, grain: Int, edit: Items.(UpdateStatement) -> Unit): Boolean

    suspend fun updateItemText(generatorId: Int, grain: Int, text: String): Boolean

    suspend fun updateItemDescription(generatorId: Int, grain: Int, description: String?): Boolean

    suspend fun updateItemParam1(generatorId: Int, grain: Int, param1: String?): Boolean

    suspend fun updateItemParam2(generatorId: Int, grain: Int, param2: String?): Boolean

    suspend fun updateItemParam3(generatorId: Int, grain: Int, param3: String?): Boolean

    suspend fun updateItemParam4(generatorId: Int, grain: Int, param4: String?): Boolean

    suspend fun updateItemParam5(generatorId: Int, grain: Int, param5: String?): Boolean

    suspend fun updateItemParam6(generatorId: Int, grain: Int, param6: String?): Boolean
}