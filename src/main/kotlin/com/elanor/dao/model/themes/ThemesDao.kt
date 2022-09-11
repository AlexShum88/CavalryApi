package com.elanor.dao.model.themes

import com.elanor.controllers.dto.IdNameDTO
import com.elanor.controllers.dto.ThemeDTO
import com.elanor.dao.DaoFactory.dbQuery
import com.elanor.dao.model.IDao
import com.elanor.dao.model.authors.Authors
import com.elanor.dao.model.entity.Theme
import org.jetbrains.exposed.sql.*

object ThemesDao : IDao {

    private val theme = Themes

    override suspend fun insert(stringValue: String): Int = dbQuery {
        with(theme) {
            insert {
                it[name] = stringValue
            }[id]
        }
    }

    suspend fun insertTheme(newTheme: ThemeDTO): Int = dbQuery {
        with(theme) {
            insert {
                it[name] = newTheme.name
                it[param1] = newTheme.param1
                it[param2] = newTheme.param2
                it[param3] = newTheme.param3
                it[param4] = newTheme.param4
                it[param5] = newTheme.param5
                it[param6] = newTheme.param6
            }[id]
        }
    }

    suspend fun updateAll(updateTheme: Theme): Boolean = dbQuery {
        Themes.update({ Themes.id.eq(updateTheme.id) }) {
            it[name] = updateTheme.name
            it[param1] = updateTheme.param1
            it[param2] = updateTheme.param2
            it[param3] = updateTheme.param3
            it[param4] = updateTheme.param4
            it[param5] = updateTheme.param5
            it[param6] = updateTheme.param6
        } > 0

    }

    override suspend fun selectAll(): List<IdNameDTO> = dbQuery {
        with(theme) {
            slice(id, name)
            selectAll().map {
                IdNameDTO(
                    it[id],
                    it[name]
                )
            }
        }
    }

    override suspend fun selectNameById(id: Int): String? = dbQuery {
        with(theme) {
            slice(Authors.name)
                .select { theme.id.eq(id) }
                .map { it[name] }.singleOrNull()
        }


    }

    override suspend fun selectIdByName(name: String): Int? = dbQuery {
        with(theme) {
            slice(Authors.id)
                .select { theme.name.eq(name) }
                .map { it[id] }.singleOrNull()
        }

    }

    override suspend fun updateName(id: Int, newName: String): Boolean = dbQuery{
        Themes.update({ Themes.id.eq(id) }) {
            it[name] = newName
        }>0
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        Themes.deleteWhere { Themes.id.eq(id) } > 0
    }
}