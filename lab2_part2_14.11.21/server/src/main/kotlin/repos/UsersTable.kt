package repos

import org.jetbrains.exposed.dao.id.LongIdTable

object UsersTable: LongIdTable("users") {

    val login = text("login")
    val password = text("password")
}