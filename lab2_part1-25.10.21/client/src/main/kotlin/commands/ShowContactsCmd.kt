package commands

import api.ContactsAPI

class ShowContactsCmd: Command<Nothing?> {

    override val key = "View all contacts"

    override fun toString() = "View all contacts"

    override suspend fun invoke(args: Nothing?) = ContactsAPI.getAllContacts().forEach { println(it.toString()) }
}