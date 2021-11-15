package models

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import repos.CoordinatesTable

class Coordinates(id: EntityID<Long>): LongEntity(id)  {

    companion object: LongEntityClass<Coordinates>(CoordinatesTable)
    var x by CoordinatesTable.x
    var y by CoordinatesTable.y
    var figure by Figure optionalReferencedOn CoordinatesTable.figure

    fun change(steps: Int, direction: Direction) =
        transaction {
            Coordinates.new {
                x = this@Coordinates.x
                y = this@Coordinates.y
                when (direction) {
                    Direction.UP -> y += steps
                    Direction.RIGHT -> x += steps
                    Direction.DOWN -> y -= steps
                    Direction.LEFT -> x -= steps
                }
            }
        }

    override fun toString() = "($x; $y)"
}

enum class Direction { UP, RIGHT, DOWN, LEFT }