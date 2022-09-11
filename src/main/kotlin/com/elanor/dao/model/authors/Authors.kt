package com.elanor.dao.model.authors

import com.elanor.dao.model.TableBase
import com.elanor.dao.model.entity.Author
import com.elanor.dao.model.entity.Entity
import org.jetbrains.exposed.sql.ResultRow

object Authors: TableBase() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 64)
    val login = varchar("login", 32)
    val password = varchar("password", 256)
    val token = uuid("token")
    val isAdmin = bool("is_admin")


    override fun rowToEntity(row: ResultRow): Entity = Author(
        id = row[id],
        name = row[name],
        login = row[login],
        password = row[password],
        token = row[token].toString(),
        isAdmin = row[isAdmin]
    )
}

