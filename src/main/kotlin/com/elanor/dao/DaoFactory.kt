package com.elanor.dao

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction


object DaoFactory {
    fun init() {
        val driverClassName = "org.postgresql.Driver"
        val jdbcURL = "jdbc:postgresql://ec2-34-247-72-29.eu-west-1.compute.amazonaws.com:5432/d48eh5bdptjufe"
        val username = "cgaxplsywoxkii"
        val password = "42e75946d98a0862c53e15dc3b6d825c9c4f7758d99b15ac032d4e444dae920d"

        val database = Database.connect(jdbcURL, driverClassName, user = username,
            password =  password)
//        val config = HikariConfig("hikari.properties")
//        val dataSource = HikariDataSource(config)
//        Database.connect(dataSource)

    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
