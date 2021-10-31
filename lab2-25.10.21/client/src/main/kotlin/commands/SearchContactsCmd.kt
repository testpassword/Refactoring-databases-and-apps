package commands

import api.ContactsAPI
import models.ContactDTO
import plugins.errln
import plugins.readUntilMatchesPattern
import java.lang.reflect.Field

class QueryFilterException(msg: String = "Filters should match 'filters=field:pattern;field:pattern'"): Exception(msg)

class SearchContactsCmd: Command<Nothing?> {

    override val key = "Search"

    override fun toString() = "Search contacts by filters"

    override suspend fun invoke(args: Nothing?) {
        val acceptedKeys = ContactDTO::class.java.declaredFields.map(Field::getName)
        println("Type fields and values which field should contain in form: 'filters=field:pattern;field:pattern'")
        try {
            val filters = readUntilMatchesPattern("filters")
                .split(";")
                .map { it.split(":") }
                .associate { it[0] to it[1] }
            if (filters.keys.any { it !in acceptedKeys }) throw QueryFilterException()
            ContactsAPI.searchContacts(
                *filters.map { it.key to it.value }.toTypedArray()
            ).forEach { println(it) }
        } catch (e: IndexOutOfBoundsException) {
            errln("The entered expression does not match the rules")
        }
    }
}