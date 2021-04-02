package pt.unl.fct.grantapp.model.dao

import pt.unl.fct.grantapp.dto.UserDTO
import pt.unl.fct.grantapp.dto.UserPasswordDTO
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class UserDAO(
        @Id
        @GeneratedValue
        open val id: Long,
        open var username: String,
        open var password: String,
        open var role: String
) {
    constructor(user: UserDTO) : this(user.id, user.username, user.password, "")

    constructor (user : UserPasswordDTO) : this(0,user.username, user.password, "")
}