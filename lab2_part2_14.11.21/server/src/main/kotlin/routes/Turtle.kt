package routes

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import models.*
import org.jetbrains.exposed.sql.transactions.transaction

val ApplicationCall.user: User
    get() = User.getByJwt(this.principal()!!)!!

val ApplicationCall.turtle: Turtle
    get() = SessionStorage.turtles[this.user.login]!!

fun Route.turtle() {

    route("/turtle") {

        get("/move") {
            call.run {
                val (text, status) = try {
                    val steps = request.queryParameters["steps"]?.toInt()
                        ?: throw IllegalArgumentException("Provide 'steps' param")
                    val moveResult = turtle.move(steps, user)
                    (if (moveResult != "") moveResult + "\n" + turtle.state else turtle.state) to HttpStatusCode.OK
                } catch (e: NumberFormatException) {
                    "'steps' param is not a valid integer" to HttpStatusCode.BadRequest
                } catch (e: Exception) {
                    e.printStackTrace()
                    (e.message ?: "") to HttpStatusCode.BadRequest
                }
                respondText(text, status = status)
            }
        }
        get("/angle") {
            call.run {
                val (text, status) = try {
                    val angle = request.queryParameters["a"]?.toInt() ?: throw IllegalArgumentException("Provide 'a' param")
                    turtle.angle = angle
                    turtle.state to HttpStatusCode.OK
                } catch (e: NumberFormatException) {
                    "'a' param is not a valid integer" to HttpStatusCode.BadRequest
                } catch (e: Exception) {
                    (e.message ?: "") to HttpStatusCode.BadRequest
                }
                respondText(text, status = status)
            }
        }
        get("/pd") {
            call.run {
                turtle.penDown()
                respondText(turtle.state)
            }
        }
        get("/pu") {
            call.run {
                turtle.penUp()
                respondText(turtle.state)
            }
        }
        get("/color") {
            call.run {
                val (text, status) = try {
                    turtle.color = request
                        .queryParameters["c"]
                        ?.let { Color.valueOf(it.uppercase()) }
                        ?: throw IllegalArgumentException("Provide 'c' param")
                    turtle.state to HttpStatusCode.OK
                } catch (e: IllegalArgumentException) {
                    "'c' param should be one of: 'green', 'black'" to HttpStatusCode.BadRequest
                } catch (e: Exception) {
                    (e.message ?: "") to HttpStatusCode.BadRequest
                }
                respondText(text, status = status)
            }
        }

        get("/list") {
            call.run {
                val (text, status) = try {
                    when(request
                        .queryParameters["type"]
                        ?.let { ListType.valueOf(it.uppercase()) }
                        ?: throw IllegalArgumentException("Provide 'c' param")
                    ) {
                        ListType.FIGURES -> transaction { Figure.all().filter { it.owner == call.user }.joinToString("\n") }
                        ListType.STEPS -> turtle.listCommands()
                    } to HttpStatusCode.OK
                } catch (e: IllegalArgumentException) {
                    "'type' param should be one of: 'steps', 'figures'" to HttpStatusCode.BadRequest
                } catch (e: Exception) {
                    e.printStackTrace()
                    (e.message ?: "") to HttpStatusCode.BadRequest
                }
                respondText(text, status = status)
            }
        }
    }
}