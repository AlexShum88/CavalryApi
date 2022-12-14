package com.elanor.dao.model.themes

import com.elanor.controllers.dto.IdNameDTO
import com.elanor.controllers.dto.ThemeDTO
import com.elanor.dao.DaoFactory.dbQuery
import com.elanor.dao.model.IDao
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
                it[description] = newTheme.description
            }[id]
        }
    }

    suspend fun updateTheme(updateTheme: Theme): Boolean = dbQuery {
        Themes.update({ Themes.id.eq(updateTheme.id) }) {
            it[name] = updateTheme.name
            if (!updateTheme.param1.isNullOrBlank()) it[param1] = updateTheme.param1
            if (!updateTheme.param2.isNullOrBlank()) it[param2] = updateTheme.param2
            if (!updateTheme.param3.isNullOrBlank()) it[param3] = updateTheme.param3
            if (!updateTheme.param4.isNullOrBlank()) it[param4] = updateTheme.param4
            if (!updateTheme.param5.isNullOrBlank()) it[param5] = updateTheme.param5
            if (!updateTheme.param6.isNullOrBlank()) it[param6] = updateTheme.param6
            if (!updateTheme.description.isNullOrBlank()) it[description] = updateTheme.description
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
            slice(name)
                .select { theme.id.eq(id) }
                .map { it[name] }.singleOrNull()
        }


    }

    override suspend fun selectIdByName(name: String): Int? = dbQuery {
        with(theme) {
            slice(id)
                .select { theme.name.eq(name) }
                .map { it[id] }.singleOrNull()
        }

    }

    override suspend fun updateName(id: Int, newName: String): Boolean = dbQuery {
        Themes.update({ Themes.id.eq(id) }) {
            it[name] = newName
        } > 0
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        Themes.deleteWhere { Themes.id.eq(id) } > 0
    }

    suspend fun selectThemeById(id: Int): Theme? = dbQuery {
        Themes
            .select { Themes.id.eq(id) }
            .map { Themes.rowToEntity(it) as Theme }.singleOrNull()
    }

    suspend fun getAllThemes(): List<Theme> = dbQuery {
        Themes
            .selectAll()
            .map { Themes.rowToEntity(it) as Theme }
    }
}