package api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import models.ContactDTO

object ContactsAPI {

    var BASE: String = ""
    private val CLIENT: HttpClient
        get() =
            HttpClient(CIO) {
                install(JsonFeature) {
                    serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                        prettyPrint = true
                    })
                }
            }

    suspend infix fun getContact(id: Long) =
        CLIENT.use {
            it.get<ContactDTO>("$BASE/contacts/$id")
        }

    suspend fun getAllContacts() =
        CLIENT.use {
            it.get<Set<ContactDTO>>("$BASE/contacts/")
        }

    suspend fun searchContacts(vararg filters: Pair<String, Any>) =
        CLIENT.use {
            it.get<Set<ContactDTO>>("$BASE/contacts/") {
                parameter("filters", filters.joinToString(";") { f ->
                    "${f.first}:${f.second}"
                })
            }
        }

    suspend infix fun addContact(contact: ContactDTO) =
        contact.copy(
            id = CLIENT.use {
                it.post<HttpResponse>("$BASE/contacts/") {
                    contentType(ContentType.Application.Json)
                    body = contact
                }.receive<String>().toLong()
            }
        )
}