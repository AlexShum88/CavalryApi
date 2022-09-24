package com.elanor.dao.model.users

import com.elanor.dao.model.TableBase
import com.elanor.dao.model.entity.Entity
import com.elanor.dao.model.entity.Token
import org.jetbrains.exposed.sql.ResultRow

object Tokens: TableBase("tokens") {
    val id = integer("id").autoIncrement()
    val token = varchar("token", 64)
    val userId = reference("user_id", Users.id)

    override fun rowToEntity(row: ResultRow): Entity = Token(
        id = row[id],
        token = row[token],
        userId = row[userId]
    )
}