package testpassword.plugins

import io.ktor.application.*
import io.ktor.request.*

class QueryFilterException(msg: String = "Filters should match 'filters=field:pattern;field:pattern'"): Exception(msg)

val ApplicationRequest.queryFilters: Map<String, String>
    get() =
        try {
            this.queryParameters["filters"]
                ?.split(";")
                ?.map { it.split(":") }
                ?.associate { it[0] to it[1] }
                ?: emptyMap()
        } catch (e: Exception) {
            throw QueryFilterException()
        }

val ApplicationCall.id: Long
    get() = this.parameters["id"]!!.toLong().also { if (it <= 0) throw NumberFormatException() }

class NotAcceptedKeyFoundException(vararg keys: String): Exception("Not acceptable keys is ${keys.joinToString(", ")}")