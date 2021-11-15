package routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import models.Turtle
import models.User
import models.User.Companion.isUserExists
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

fun Route.user() {

    route("/user") {

        post("/register") {
            val newbie = call.receive<User.UserDto>()
            val (text, status) = if (isUserExists(newbie)) "User already exists" to HttpStatusCode.Conflict
            else {
                transaction {
                    User.new {
                        login = newbie.login
                        password = newbie.password
                    }
                }
                "you've been successfully register" to HttpStatusCode.Created
            }
            call.respondText(text, status = status)
        }

        post("/login") {
            val user = call.receive<User.UserDto>()
            val (text, status) = if (isUserExists(user)) {
                SessionStorage.turtles[user.login] = Turtle()
                JWT.create()
                    .withClaim("login", user.login)
                    .withExpiresAt(Date(System.currentTimeMillis() + 3600000))
                    .sign(Algorithm.HMAC256(SessionStorage.JWT_SECRET)) to HttpStatusCode.Accepted
            }
            else "User didn't exists" to HttpStatusCode.NotFound
            call.respondText(text, status = status)
        }
    }
}