import models.Turtle

object SessionStorage {

    var JWT_SECRET = ""

    val turtles = emptyMap<String, Turtle>().toMutableMap()
}