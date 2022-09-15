package com.elanor.controllers.dto

import com.elanor.dao.model.entity.Generator
import com.elanor.dao.model.entity.Item
import com.elanor.dao.model.entity.Theme
import kotlinx.serialization.Serializable

@Serializable
data class GeneratorThemeItemsTDO(val generator: Generator,
                                  val theme: Theme,
                                  val items: List<Item>) {

}
