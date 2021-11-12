package testpassword.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import testpassword.repos.ContactsTable
import testpassword.routes.contacts

fun Application.configureDatabase(jdbcUrl: String) {
    Database.connect(jdbcUrl, "org.sqlite.JDBC")
    transaction { SchemaUtils.create(ContactsTable) }
}

fun Application.configureHTTP() {
    install(CORS) {
        methods.addAll(HttpMethod.DefaultMethods)
        anyHost()
    }
    install(StatusPages) {
        exception<NumberFormatException> { call.respond(HttpStatusCode.BadRequest, "id should be positive Long number") }
        exception<NullPointerException> { call.respond(HttpStatusCode.NotFound, "entity with requested id didn't exists") }
        exception<QueryFilterException> { cause -> call.respond(HttpStatusCode.BadRequest, cause.localizedMessage) }
        exception<NotAcceptedKeyFoundException> { cause -> call.respond(HttpStatusCode.BadRequest, cause.localizedMessage) }
        exception<ExposedSQLException> { call.respond(HttpStatusCode.BadRequest, "failed body format, check data is correct") }
    }
}

fun Application.configureRouting() {
    routing {
        contacts()
    }
}

fun Application.configureModules() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        })
    }
}