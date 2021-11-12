package commands

import api.ContactsAPI
import models.ContactDTO
import plugins.readUntilMatchesPattern

class AddContactCmd: Command<Nothing?> {

    override val key = "New contact"

    override fun toString() = "Create new contact and save it"

    override suspend fun invoke(args: Nothing?) {
        println("Name:")
        val name = readUntilMatchesPattern("name")
        println("Surname [optional]:")
        val surname = readLine().orEmpty()
        println("Email [optional]:")
        val email = readUntilMatchesPattern("email", Regex("(^$|^.*@.*..*$)"))
        println("Phone:")
        val phone = readUntilMatchesPattern("phone", Regex("^((\\+7|7|8)+([0-9]){10})\$"))
        ContactsAPI.addContact(ContactDTO(name, phone, surname, email))
        println("Contact created")
    }
}