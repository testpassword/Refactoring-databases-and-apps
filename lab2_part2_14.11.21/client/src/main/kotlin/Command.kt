import kotlin.system.exitProcess

interface Command { fun execute(): String }

val client = RestClient()

class MoveCommand(private val steps: Int): Command {
    override fun execute() = client.get("/turtle/move", Pair("steps", steps))
}

class AngleCommand(private val angle: Int): Command {
    override fun execute() = client.get("/turtle/angle", Pair("a", angle))
}

class PdCommand: Command {
    override fun execute() = client.get("/turtle/pd")
}

class PuCommand: Command {
    override fun execute() = client.get("/turtle/pu")
}

enum class Color { BLACK, GREEN }

class ColorCommand(private val color: Color): Command {
    override fun execute() = client.get("/turtle/color", Pair("c", color))
}

enum class ListType { STEPS, FIGURES }

class ListCommand(private val type: ListType): Command {
    override fun execute() = client.get("/turtle/list", Pair("type", type))
}

class ExitCommand: Command {
    override fun execute() = exitProcess(0)
}

abstract class AuthCommand(private val creds: String): Command {

    fun credsRequest(endpoint: String): String {
        val (login, password) = creds.split("|")
        return client.post(
            "/user/$endpoint", mapOf(
                "login" to login,
                "password" to password
            )
        )
    }
}

class RegisterCommand(creds: String): AuthCommand(creds) {
    override fun execute() = this.credsRequest("register")
}

class LoginCommand(creds: String): AuthCommand(creds) {
    override fun execute(): String {
        val token = this.credsRequest("login")
        return if (token != "") {
            client.bearer = token
            "successfully login"
        } else "login or password isn't correct"
    }
}
