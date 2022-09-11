package com.elanor.dao.model.generators

import com.elanor.dao.model.entity.Generator

interface IGeneratorsDao {
    suspend fun getAllGenerators(): List<Generator>

    suspend fun insertGenerator(generator: Generator): Int

    suspend fun selectGeneratorById(id: Int): Generator?

    suspend fun selectGeneratorByName(name: String): Generator?

    suspend fun selectGeneratorsByAuthor(authorId: Int): List<Generator>

    suspend fun selectGeneratorsByTheme(themeId: Int): List<Generator>

    suspend fun isAuthorPresent(authorId: Int): Boolean

    suspend fun isThemePresent(themeId: Int): Boolean
}