package com.elanor.dao.model.users

import com.elanor.controllers.dto.AuthorIdTokenDTO
import com.elanor.controllers.dto.UserDTO
import com.elanor.dao.DaoFactory.dbQuery
import com.elanor.dao.model.authors.Authors
import com.elanor.dao.model.authors.encryptPassword
import com.elanor.dao.model.authors.tokenGenerator
import org.jetbrains.exposed.sql.*

object UsersDao {

    suspend fun signUp(user: UserDTO) = dbQuery {
        val idUser = Users.insert {
            it[login] = user.login
            it[password] = encryptPassword(user.password)
        }[Users.id]

        Tokens.insert {
            it[token] = tokenGenerator(user.login).toString()
            it[userId] = idUser
        }

        Authors.insert {
            it[userId] = idUser
            it[name] = user.login
        }

    }

    suspend fun logIn(user: UserDTO): AuthorIdTokenDTO? = dbQuery {
        Users
            .join(Tokens, JoinType.INNER, additionalConstraint = { Users.id.eq(Tokens.userId) })
            .join(Authors, joinType = JoinType.INNER, additionalConstraint = { Users.id.eq(Authors.id) })
            .slice(Authors.id, Tokens.token)
            .select { Users.login.eq(user.login) and Users.password.eq(Users.encryptPassword(user.password)) }
            .map {
                AuthorIdTokenDTO(
                    id = it[Authors.id],
                    token = it[Tokens.token]
                )
            }
            .singleOrNull()
    }

    suspend fun delete(id: Int): Boolean = dbQuery {
        Users.deleteWhere { Users.id.eq(id) } > 0
    }
}