import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.serialization.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import repos.CoordinatesTable
import repos.FiguresTable
import repos.UsersTable
import routes.turtle
import routes.user

fun Application.configureDatabase(jdbcUrl: String) {
    Database.connect(jdbcUrl, "org.sqlite.JDBC")
    transaction { SchemaUtils.create(CoordinatesTable, FiguresTable, UsersTable) }
}

fun Application.configureJwt(secret: String) {
    SessionStorage.JWT_SECRET = secret
    install(Authentication) {
        jwt("auth-jwt") {
            verifier(JWT
                .require(Algorithm.HMAC256(secret))
                .build())
            validate {
                if (it.payload.getClaim("login").asString() != "") JWTPrincipal(it.payload) else null
            }
        }
    }
}

fun Application.configureConverters() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        })
    }
}

fun Application.configureRoutes() {
    routing {
        route("/api") {
            authenticate("auth-jwt") {
                turtle()
            }
            user()
        }
    }
}

fun main(args: Array<String>) {
    try {
        embeddedServer(Netty, port = 8080) {
            configureDatabase(args.first())
            configureJwt(args[1])
            configureConverters()
            configureRoutes()
        }.start(wait = true)
    } catch (e: Exception) {
        System.err.println(when (e) {
            is NoSuchElementException -> "you should provide url to database"
            is IndexOutOfBoundsException -> "you should provide jwt secret"
            else -> "Unexpected error: ${e.stackTraceToString()}"
        })
    }
}