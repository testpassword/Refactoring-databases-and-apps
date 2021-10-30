package testpassword.repos

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.or

object ContactsTable: LongIdTable("contacts") {

    val name = text("name").check { it neq "" }
    val surname = text("surname").default("")
    val phone = text("phone").check { (it like "+___________") or (it like "___________") }
    val email = text("email").check { (it like "%@%.%") or (it like "") }
}