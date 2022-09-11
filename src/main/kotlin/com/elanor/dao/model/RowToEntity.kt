package com.elanor.dao.model

import com.elanor.dao.model.entity.Entity
import org.jetbrains.exposed.sql.ResultRow

interface RowToEntity {
    fun rowToEntity(row: ResultRow): Entity
}