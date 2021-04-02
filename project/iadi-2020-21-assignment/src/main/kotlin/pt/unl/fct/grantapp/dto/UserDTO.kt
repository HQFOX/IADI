package pt.unl.fct.grantapp.dto

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema

abstract class UserDTO(
        open val id: Long,
        open val username: String,
        open val password: String
)

data class UserPasswordDTO (
        val username: String,
        val password: String
)
