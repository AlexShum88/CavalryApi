package com.elanor.dao

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction


object DaoFactory {
    fun init() {
//        val driverClassName = "org.postgresql.Driver"
//        val jdbcURL = "jdbc:postgresql://localhost:5432/cavalry_db"
//        val username = "postgres"
//        val password = "root"

//        val database = Database.connect(jdbcURL, driverClassName, user = username,
//            password =  password)
        val config = HikariConfig("hikari.properties")
        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)

    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
