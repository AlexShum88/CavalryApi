package com.elanor.dao

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction


object DaoFactory {
    fun init() {
        val driverClassName = "com.mysql.cj.jdbc.Driver"
//        val driverClassName = "org.postgresql.Driver"
//        val jdbcURL = "jdbc:postgresql://ec2-34-247-72-29.eu-west-1.compute.amazonaws.com:5432/d48eh5bdptjufe"
//        val username = "cgaxplsywoxkii"
//        val password = "42e75946d98a0862c53e15dc3b6d825c9c4f7758d99b15ac032d4e444dae920d"//
//        val jdbcURL = "jdbc:mysql://oliadkuxrl9xdugh.chr7pe7iynqr.eu-west-1.rds.amazonaws.com:3306/u8d2kcohih5fga3n"
//        val username = "ze450iycjtthy0ex"
//        val password = "rq20zdp07lzo9tpm"
//        val jdbcURL = "jdbc:mysql://containers-us-west-30.railway.app:7873/railway"
//        val username = "root"
//        val password = "FYUdgmsa1tDStMnVoda6"
//        val jdbcURL = "jdbc:mysql://eu-central.connect.psdb.cloud/cavalrydb?sslMode=VERIFY_IDENTITY"
//        val username = "03h6ib19v53figqmhvzw"
//        val password = "pscale_pw_wV3EcrLSSI1OMBsJTLYyxYjSL5VfsR4grlhKzc25wdl"
        val jdbcURL = "jdbc:mysql://sql.freedb.tech:3306/freedb_cavalrydb"
        val username = "freedb_elentin"
        val password = "5zB7gg**?k#cvYB"



        val database = Database.connect(jdbcURL, driverClassName, user = username,
            password =  password)
//        val config = HikariConfig("hikari.properties")
//        val dataSource = HikariDataSource(config)
//        Database.connect(dataSource)

    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
