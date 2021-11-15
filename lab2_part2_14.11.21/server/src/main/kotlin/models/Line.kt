package models

import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.math.max
import kotlin.math.min

class Line(private val point1: Coordinates, private val point2: Coordinates) {
    private val a = point2.y - point1.y
    private val b = point1.x - point2.x
    private val c = a * point1.x + b * point1.y

    fun checkIfParallel(line: Line) = det(line) == 0

    fun getIntersection(line: Line): Coordinates? {
        val det = det(line)
        return if (det != 0)
            transaction {
                Coordinates.new {
                    x = (line.b * this@Line.c - this@Line.b * line.c) / det
                    y = (this@Line.a * line.c - line.a * this@Line.c) / det
                }
            }
         else null
    }

    fun ifPointLiesOnLine(point: Coordinates) =
        min(point1.x, point2.x) <= point.x && point.x <= max(point1.x, point2.x) &&
                min(point1.y, point2.y) <= point.y && point.y <= max(point1.y, point2.y)

    private fun det(line: Line) = this.a * line.b - line.a * this.b
}