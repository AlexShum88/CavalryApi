package com.elanor.controllers.dto

import com.elanor.dao.model.entity.Entity
import com.elanor.dao.model.entity.IdNameEntity
import kotlinx.serialization.Serializable

@Serializable
data class IdNameDTO(
    override val id: Int,
    override val name: String
): IdNameEntity {

}

