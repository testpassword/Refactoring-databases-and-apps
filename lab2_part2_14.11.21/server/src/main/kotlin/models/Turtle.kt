package models

import models.Figure.Companion.createFigure
import org.jetbrains.exposed.sql.transactions.transaction

enum class Color { BLACK, GREEN }

enum class ListType { STEPS, FIGURES }

class Turtle {

    private var coordinates = transaction {
        Coordinates.new {
            x = 0
            y = 0
        }
    }
    private val linePoints = mutableListOf<Coordinates>()

    var angle = 0
        set(value) {
            if (value % 90 != 0) throw IllegalArgumentException("Angle must be divided by 90")
            else field = value
        }

    private var isPenDown = false
    private val penState
        get() = if (isPenDown) "down" else "up"

    var color = Color.GREEN

    private val commands = mutableListOf<String>()
    private val figures = mutableListOf<Figure>()

    val state
        get() = "Current color: ${color.name.lowercase()}, pen state: $penState, location: $coordinates, direction: $angle degrees"

    fun move(steps: Int, master: User): String {
        coordinates = when (angle % 360) {
            0 -> coordinates.change(steps, Direction.UP)
            90 -> coordinates.change(steps, Direction.RIGHT)
            180 -> coordinates.change(steps, Direction.DOWN)
            270 -> coordinates.change(steps, Direction.LEFT)
            else -> coordinates
        }
        linePoints.add(coordinates)
        commands.add("move $steps")
        if (linePoints.size > 4) {
            val figure = createFigure(linePoints, master)

            if (figure != null) {
                figures.add(figure)
                return "You created ${figure.name}"
            }
        }
        return ""
    }

    fun penUp(): String {
        isPenDown = false
        linePoints.clear()
        commands.add("pu")
        return "Pen is up"
    }

    fun penDown(): String {
        isPenDown = true
        linePoints.add(coordinates)
        commands.add("pd")
        return "Pen is down"
    }

    fun listCommands() = commands.apply { add("list commands") }.joinToString("\n")

    fun listFigures(): String {
        commands.add("list figures")
        return figures.joinToString("\n")
    }
}