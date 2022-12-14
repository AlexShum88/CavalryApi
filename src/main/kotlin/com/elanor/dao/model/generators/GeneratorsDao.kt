package com.elanor.dao.model.generators

import com.elanor.controllers.dto.GeneratorFacadeDTO
import com.elanor.controllers.dto.GeneratorInsertDTO
import com.elanor.controllers.dto.GeneratorUpdateDTO
import com.elanor.dao.DaoFactory.dbQuery
import com.elanor.dao.model.authors.Authors
import com.elanor.dao.model.entity.Generator
import com.elanor.dao.model.themes.Themes
import org.jetbrains.exposed.sql.*

object GeneratorsDao : IGeneratorsDao {


    override suspend fun getAllGenerators(): List<Generator> = dbQuery {
        Generators.selectAll().map { Generators.rowToEntity(it) as Generator }
    }

    override suspend fun insertGenerator(generator: GeneratorInsertDTO): Int = dbQuery {
        Generators.insert {
            it[name] = generator.name
            it[authorId] = generator.authorId
            it[themeId] = generator.themeId
            it[description] = generator.description
        }[Generators.id]
    }

    override suspend fun selectGeneratorById(id: Int): Generator? = dbQuery {
        Generators.select { Generators.id.eq(id) }
            .map { Generators.rowToEntity(it) as Generator }
            .singleOrNull()
    }

    override suspend fun selectGeneratorByName(name: String): Generator? = dbQuery {
        Generators.select { Generators.name.eq(name) }
            .map { Generators.rowToEntity(it) as Generator }
            .singleOrNull()
    }

    override suspend fun selectGeneratorsByAuthor(authorId: Int): List<Generator> = dbQuery {
        Generators.select { Generators.authorId.eq(authorId) }
            .map { Generators.rowToEntity(it) as Generator }

    }

    override suspend fun selectGeneratorsByTheme(themeId: Int): List<Generator> = dbQuery {
        Generators.select { Generators.themeId.eq(themeId) }
            .map { Generators.rowToEntity(it) as Generator }

    }

    override suspend fun isAuthorPresent(authorId: Int): Boolean = dbQuery {
        !
        Authors.select {
            Authors.id.eq(authorId)
        }.empty()
    }

    override suspend fun isThemePresent(themeId: Int): Boolean = dbQuery {
        !
        Themes.select {
            Themes.id.eq(themeId)
        }.empty()
    }

    suspend fun getGeneratorsFacade(): List<GeneratorFacadeDTO> = dbQuery {
        Generators.join(Authors, JoinType.INNER, additionalConstraint = { Generators.authorId.eq(Authors.id) })
            .join(Themes, JoinType.INNER, additionalConstraint = { Generators.themeId.eq(Themes.id) })
            .slice(Generators.name, Generators.description, Authors.name, Themes.name)
            .selectAll().map {
                GeneratorFacadeDTO.mapRowToGeneratorFacade(it)
            }

    }

    suspend fun updateGenerator(generator: GeneratorUpdateDTO) = dbQuery {
        Generators.update({ Generators.id.eq(generator.id) }) {
            if (generator.name != null) {
                it[name] = generator.name
            }
            if (generator.description != null) {
                it[description] = generator.description
            }

        }
    }

    suspend fun deleteGenerator(id: Int) = dbQuery {
        Generators.deleteWhere { Generators.id.eq(id) }
    }


    suspend fun isAuthorHasGenerator(generatorId: Int, authorOfGeneratorId: Int): Boolean = dbQuery {
        !
        with(Generators) {
            select { id.eq(generatorId) and authorId.eq(authorOfGeneratorId) }
        }.empty()
    }
}
