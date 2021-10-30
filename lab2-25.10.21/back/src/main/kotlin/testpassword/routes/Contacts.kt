package testpassword.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.transactions.transaction
import testpassword.models.Contact
import testpassword.plugins.*

fun Route.contacts() {
    route("/contacts/") {

        get {
            call.respond(
                transaction {
                    Contact
                        .all()
                        .toMutableList()
                        .also {
                            call.request.queryFilters.forEach { k, v ->
                                it.retainAll { r ->
                                    when (k) {
                                        "name" -> r.name.contains(v)
                                        "surname" -> r.surname.contains(v)
                                        "phone" -> r.phone.startsWith(v)
                                        "email" -> r.email.contains(v)
                                        else -> throw NotAcceptedKeyFoundException(k)
                                    }
                                }
                            }
                        }
                        .map(Contact::toDTO)
                }
            )
        }

        get("{id}") {
            call.respond(
                transaction {
                    Contact.findById(call.id)!!.toDTO()
                }
            )
        }

        post {
            call.respondText(status = HttpStatusCode.Created) {
                val payload = call.receive<Contact.ContactDTO>()
                transaction {
                    Contact.new {
                        fromDTO(payload)
                    }
                }.id.toString()
            }
        }
    }
}