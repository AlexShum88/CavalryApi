package com.elanor.controllers.dto

import com.elanor.dao.model.entity.Generator
import com.elanor.dao.model.entity.Item
import kotlinx.serialization.Serializable

@Serializable
data class GeneratorItemsTDO(val generator: Generator, val items: List<Item>) {

}
