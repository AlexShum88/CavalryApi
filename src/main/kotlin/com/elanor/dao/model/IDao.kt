package com.elanor.dao.model

import com.elanor.controllers.dto.IdNameDTO
import com.elanor.controllers.dto.NameDTO
import com.elanor.dao.model.entity.Entity
import com.elanor.dao.model.entity.IdNameEntity

interface IDao {


    suspend fun insert(stringValue: String): Int

    suspend fun selectAll(): List<IdNameDTO>

    suspend fun selectNameById(id: Int): String?

    suspend fun selectIdByName(name: String): Int?

    suspend fun updateName(id: Int, newName: String): Boolean

    suspend fun delete(id: Int): Boolean
}