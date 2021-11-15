package models

import io.ktor.auth.jwt.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import repos.FiguresTable
import repos.UsersTable

class User(id: EntityID<Long>): LongEntity(id) {

    companion object: LongEntityClass<User>(UsersTable) {

        fun isUserExists(candidate: UserDto) =
            transaction { User.all().find { it.login == candidate.login && it.password == candidate.password } } != null

        fun getByJwt(principal: JWTPrincipal) =
            transaction { User.all().find { it.login == principal.payload.getClaim("login").asString() } }
    }

    var login by UsersTable.login
    var password by UsersTable.password

    @Serializable data class UserDto(val login: String, val password: String)
}