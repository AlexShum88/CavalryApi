package com.elanor.controllers.dto

import com.elanor.dao.model.authors.Authors
import com.elanor.dao.model.generators.Generators
import com.elanor.dao.model.themes.Themes
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class GeneratorFacadeDTO(
    val generatorName: String,
    val author: String,
    val theme: String,
    val description: String?
) {

    companion object {
        fun mapRowToGeneratorFacade(row: ResultRow): GeneratorFacadeDTO = GeneratorFacadeDTO(
            generatorName = row[Generators.name],
            author = row[Authors.name],
            theme = row[Themes.name],
            description = row[Generators.description]
        )
    }
}