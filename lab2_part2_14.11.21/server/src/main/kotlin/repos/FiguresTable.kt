package repos

import org.jetbrains.exposed.dao.id.LongIdTable

object FiguresTable: LongIdTable("figures") {

    val name = text("name").default("")
    val owner = reference("owner", UsersTable)
}