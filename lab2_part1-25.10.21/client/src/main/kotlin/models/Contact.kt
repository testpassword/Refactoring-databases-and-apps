package models

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable data class ContactDTO(@Required val name: String,
                                    @Required val phone: String,
                                    val surname: String = "",
                                    val email: String = "",
                                    val id: Long = 0) {

    override fun toString() = Json.encodeToString(this)
}