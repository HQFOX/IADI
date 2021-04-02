package pt.unl.fct.grantapp.service

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import pt.unl.fct.grantapp.exceptions.NotFoundException
import pt.unl.fct.grantapp.model.UserRepository
import pt.unl.fct.grantapp.model.dao.UserDAO
import java.util.*

@Service
class UserService(var users: UserRepository) {

    fun getAuthorities(name: String): MutableList<GrantedAuthority> {
        val authorities: MutableList<GrantedAuthority> = mutableListOf()

        getUserRoles(name).forEach { p ->
            val authority = SimpleGrantedAuthority("ROLE_$p")
            authorities.add(authority)
        }

        return authorities
    }

    fun getUserRoles(name: String): List<String> {
        val u: UserDAO = getOneUserByName(name)
        return if (u.role.isNotEmpty()) {
            u.role.split(",")
        } else emptyList()

    }

    fun getOneUserByName(name: String): UserDAO =
            users.findUserByUsername(name)
                    .orElseThrow { NotFoundException("There is no user with username $name") }


    fun addUser(user: UserDAO) : Optional<UserDAO> {
        val aUser = getOneUserByName(user.username)

        /**
        return if ( aUser.isPresent )
            Optional.empty()
        else {
        **/
            user.password = BCryptPasswordEncoder().encode(user.password)
            return Optional.of(users.save(user))
        /**
        }
        **/
    }
}