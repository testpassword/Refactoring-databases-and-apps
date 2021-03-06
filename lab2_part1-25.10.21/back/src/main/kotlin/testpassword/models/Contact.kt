package testpassword.models

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import testpassword.repos.ContactsTable

class Contact(id: EntityID<Long>): LongEntity(id) {

    companion object: LongEntityClass<Contact>(ContactsTable)
    var name by ContactsTable.name
    var surname by ContactsTable.surname
    var phone by ContactsTable.phone
    var email by ContactsTable.email

    @Serializable data class ContactDTO(@Required val name: String,
                                        @Required val phone: String,
                                        val surname: String = "",
                                        val email: String = "",
                                        val id: Long = 0)

    fun toDTO() = ContactDTO(this.name, this.phone, this.surname, this.email, this.id.value)

    infix fun fromDTO(contactDTO: ContactDTO) =
        this.also {
            name = contactDTO.name
            surname = contactDTO.surname
            phone = contactDTO.phone
            email = contactDTO.email
        }
}