package com.elanor.dao.model.generators

import com.elanor.dao.model.RowToEntity
import com.elanor.dao.model.authors.Authors
import com.elanor.dao.model.entity.Entity
import com.elanor.dao.model.entity.Generator
import com.elanor.dao.model.themes.Themes
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object Generators: Table(), RowToEntity {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 128)
    val authorId = reference("author_id", Authors.id)
    val themeId = reference("theme_id", Themes.id)
    val minVal = integer("min_val")
    val maxVal = integer("max_val")


    override fun rowToEntity(row: ResultRow): Entity = Generator(
        id =  row[id],
        name = row[name],
        authorId = row[authorId],
        themeId = row[themeId],
        minVal = row[minVal],
        maxVal = row[maxVal],
    )

}