package repos

import org.jetbrains.exposed.dao.id.LongIdTable

object CoordinatesTable: LongIdTable("coordinates") {

    val x = integer("x").default(0)
    val y = integer("y").default(0)
    val figure = reference("figure", FiguresTable).nullable()
}