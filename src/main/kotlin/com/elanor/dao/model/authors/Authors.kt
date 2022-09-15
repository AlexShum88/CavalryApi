package com.elanor.dao.model.authors

import com.elanor.dao.model.TableBase
import com.elanor.dao.model.entity.Author
import com.elanor.dao.model.entity.Entity
import com.elanor.dao.model.users.Users
import org.jetbrains.exposed.sql.ResultRow

object Authors: TableBase() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 16)
    val isAdmin = bool("is_admin")
    val userId = reference("user_id", Users.id)


    override fun rowToEntity(row: ResultRow): Entity = Author(
        id = row[id],
        name = row[name],
        isAdmin = row[isAdmin],
        userId = row[userId]
    )
}

