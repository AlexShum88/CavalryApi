package com.elanor.dao.model.authors

import com.elanor.controllers.dto.AuthorIdTokenDTO
import com.elanor.controllers.dto.AuthorSignUpDTO
import com.elanor.controllers.dto.IdNameDTO
import com.elanor.dao.DaoFactory.dbQuery
import com.elanor.dao.model.IDao
import org.jetbrains.exposed.sql.*
import java.util.*

object AuthorsDao : IDao {

    private val authors = Authors

    override suspend fun insert(stringValue: String): Int = dbQuery {
        with(authors) {
            insert {
                it[name] = stringValue
            }[id]
        }
    }

    suspend fun insertAuthor(author: AuthorSignUpDTO): Int = dbQuery {
        with(authors) {
            insert {
                it[name] = author.name
                it[login] = author.login
                it[password] = encryptPassword(author.password)
                it[token] = tokenGenerator(author.login)
                it[isAdmin] = author.isAdmin
            }[id]
        }
    }

    override suspend fun selectAll(): List<IdNameDTO> = dbQuery {
        with(authors) {
            slice(id, name)
            selectAll().map {
                IdNameDTO(
                    it[id],
                    it[name]
                )
            }
        }

    }

    suspend fun login(userLogin: String, userPassword: String): AuthorIdTokenDTO? = dbQuery {
        with(authors) {
            slice(id, token)
            select {
                login.eq(userLogin) and password.eq(encryptPassword(userPassword))
            }.map {
                AuthorIdTokenDTO(it[id], it[token].toString())
            }.singleOrNull()
        }
    }

    override suspend fun selectNameById(id: Int): String? = dbQuery {
        with(authors) {
            slice(name)
                .select { authors.id.eq(id) }
                .map { it[name] }.singleOrNull()
        }
    }

    override suspend fun selectIdByName(name: String): Int? = dbQuery {
        with(authors) {
            slice(id)
                .select { authors.name.eq(name) }
                .map { it[id] }.singleOrNull()
        }
    }

    override suspend fun updateName(id: Int, newName: String): Boolean = dbQuery{
        Authors.update({ Authors.id.eq(id) }) {
            it[name] = newName
        }>0
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        Authors.deleteWhere { Authors.id.eq(id) } > 0
    }


    suspend fun tokenById(userId: Int): String? = dbQuery {
        with(Authors){
            slice(token)
            select{
                id.eq(userId)
            }.map {
                it[token].toString()
            }.singleOrNull()
        }
    }

    suspend fun idByToken(userToken: String): Int? = dbQuery {
        with(Authors){
            slice(id)
            select {
                token.eq(UUID.fromString(userToken))
            }.map{
                it[id]
            }.singleOrNull()
        }
    }

    suspend fun isAdminToken(userToken: String): Boolean = dbQuery {
        with(Authors){
            slice(isAdmin)
            select {
                token.eq(UUID.fromString(userToken))
            }.map {
                it[isAdmin]
            }.singleOrNull() ?: false
        }
    }


}

