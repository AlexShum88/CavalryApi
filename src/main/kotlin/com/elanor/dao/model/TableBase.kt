package com.elanor.dao.model

import org.jetbrains.exposed.sql.Table

abstract class TableBase: Table, RowToEntity {
    constructor(): super()
    constructor(name: String) : super(name = name)
}