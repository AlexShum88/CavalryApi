package com.elanor.dao.model.users

import com.elanor.dao.model.TableBase
import com.elanor.dao.model.entity.Entity
import com.elanor.dao.model.entity.User
import org.jetbrains.exposed.sql.ResultRow

object Users: TableBase("users") {
    val id = integer("id").autoIncrement()
    val login = varchar("login", 16)
    val password = varchar("password", 128)

    override fun rowToEntity(row: ResultRow): Entity = User(
        id = row[id],
        login = row[login],
        password = row[password]
    )
}