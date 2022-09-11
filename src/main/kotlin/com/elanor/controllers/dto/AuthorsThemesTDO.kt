package com.elanor.controllers.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthorsThemesTDO(val authors: List<IdNameDTO>, val themes: List<IdNameDTO>) {

}
