package com.elanor.dao.model.items

import com.elanor.dao.model.TableBase
import com.elanor.dao.model.entity.Entity
import com.elanor.dao.model.entity.Item
import com.elanor.dao.model.generators.Generators
import org.jetbrains.exposed.sql.ResultRow

object Items : TableBase() {
    val id = integer("id").autoIncrement()
    val generatorId = reference("generator_id", Generators.id)
    val grain = integer("grain")
    val text = varchar("text", 1024)
    val description = varchar("description", 2048).nullable()
    val param1 = varchar("param1", 16).nullable()
    val param2 = varchar("param2", 16).nullable()
    val param3 = varchar("param3", 16).nullable()
    val param4 = varchar("param4", 16).nullable()
    val param5 = varchar("param5", 16).nullable()
    val param6 = varchar("param6", 16).nullable()

    override fun rowToEntity(row: ResultRow): Entity = Item(
        id = row[Items.id],
        generatorId = row[generatorId],
        grain = row[grain],
        text = row[text],
        description = row[description],
        param1 = row[param1],
        param2 = row[param2],
        param3 = row[param3],
        param4 = row[param4],
        param5 = row[param5],
        param6 = row[param6],
    )

}