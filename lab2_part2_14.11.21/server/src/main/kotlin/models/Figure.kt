package models

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import repos.CoordinatesTable
import repos.FiguresTable
import repos.UsersTable

class Figure(id: EntityID<Long>): LongEntity(id)  {

    companion object: LongEntityClass<Figure>(FiguresTable) {

        fun createFigure(coords: List<Coordinates>, master: User): Figure? {
            val lastLine = Line(coords[coords.size - 2], coords[coords.size - 1])
            for (i in 0..coords.size - 3) {
                val line = Line(coords[i], coords[i + 1])
                val intersection = lastLine.getIntersection(line)
                if (intersection != null && line.ifPointLiesOnLine(intersection) && lastLine.ifPointLiesOnLine(intersection))
                    return transaction {
                        Figure.new {
                            owner = master
                            name = calcFigureName()
                        }.apply { coords.forEach { it.figure = this } }
                    }
            }
            return null
        }
    }
    var name by FiguresTable.name
    val points by Coordinates optionalReferrersOn CoordinatesTable.figure
    var owner by User referencedOn FiguresTable.owner

    override fun toString() = "$name with coordinates ${points.joinToString(prefix = "{", postfix = "}") { "(${it.x}; ${it.y})" }}"

    fun calcFigureName() =
        when (points.toList().size) {
            3 -> "Triangle"
            4 -> if (checkIfRectangle()) "Rectangle" else "Quadrangle"
            5 -> "Pentagon"
            else -> "Polygon"
    }

    private fun checkIfRectangle(): Boolean {
        val points = this.points.toList()
        val line1 = Line(points[0], points[1])
        val line2 = Line(points[1], points[2])
        val line3 = Line(points[2], points[3])
        val line4 = Line(points[3], points[0])
        return (line1.checkIfParallel(line3) && line2.checkIfParallel(line4))
    }
}