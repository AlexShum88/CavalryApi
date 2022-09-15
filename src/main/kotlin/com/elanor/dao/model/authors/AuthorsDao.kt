package com.elanor.dao.model.authors

import com.elanor.controllers.dto.IdNameDTO
import com.elanor.dao.DaoFactory.dbQuery
import com.elanor.dao.model.IDao
import com.elanor.dao.model.users.Tokens
import org.jetbrains.exposed.sql.*

object AuthorsDao : IDao {

    private val authors = Authors

    override suspend fun insert(stringValue: String): Int = dbQuery {
        return@dbQuery 0
    }
//
//    fun insertAuthor(author: UserDTO): Int = dbQuery {
//        with(authors) {
//            insert {
//                it[login] = author.login
//                it[password] = encryptPassword(author.password)
//                it[token] = tokenGenerator(author.login)
////                it[isAdmin] = author.isAdmin
//                it[isAdmin] = false
//            }[id]
//        }
//    }

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
//
//    suspend fun login(userLogin: String, userPassword: String): AuthorIdTokenDTO? = dbQuery {
//        with(authors) {
//            slice(id, token)
//            select {
//                login.eq(userLogin) and password.eq(encryptPassword(userPassword))
//            }.map {
//                AuthorIdTokenDTO(it[id], it[token].toString())
//            }.singleOrNull()
//        }
//    }

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

    override suspend fun updateName(id: Int, newName: String): Boolean = dbQuery {
        Authors.update({ Authors.id.eq(id) }) {
            it[name] = newName
        } > 0
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        Authors.deleteWhere { Authors.id.eq(id) } > 0
    }

    suspend fun updateAdminStatus(id: Int, admin: Boolean) = dbQuery {
        Authors.update({ Authors.id.eq(id) }) {
            it[isAdmin] = admin
        }
    }


    suspend fun tokenById(authorId: Int): String? = dbQuery {
        Authors
            .join(Tokens, JoinType.INNER, additionalConstraint = { Authors.userId.eq(Tokens.userId) })
            .slice(Tokens.token)
            .select {
                Authors.id.eq(authorId)
            }.map {
                it[Tokens.token].toString()
            }.singleOrNull()
    }


    suspend fun idByToken(userToken: String): Int? = dbQuery {
        Authors.join(Tokens, JoinType.INNER, additionalConstraint = { Authors.userId.eq(Tokens.userId) })
            .slice(Authors.id)
            .select {
                Tokens.token.eq(userToken)
            }
            .map {
                it[Authors.id]
            }.singleOrNull()
    }

    suspend fun isAdminToken(userToken: String): Boolean = dbQuery {
        Authors.join(Tokens, JoinType.INNER, additionalConstraint = { Authors.userId.eq(Tokens.userId) })
            .slice(Authors.isAdmin)
            .select {
                Tokens.token.eq(userToken)
            }
            .map {
                it[Authors.isAdmin]
            }.singleOrNull() ?: false
    }

}

