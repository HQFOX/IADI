package pt.unl.fct.grantapp.config

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import pt.unl.fct.grantapp.model.UserRepository
import pt.unl.fct.grantapp.service.UserService

public class CustomUserDetails(
        private val aUserId: Long,
        private val aUsername: String,
        private val aPassword: String,
        private val someAuthorities: MutableCollection<out GrantedAuthority>) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = someAuthorities

    override fun isEnabled(): Boolean = true

    override fun getUsername(): String = aUsername

    override fun isCredentialsNonExpired(): Boolean = true

    override fun getPassword(): String = aPassword

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    fun getUserId(): Long = aUserId
}


@Service
class CustomUserDetailsService(
        val user: UserService,
        val usersRep: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {

        username?.let {
            val userDAO = usersRep.findUserByUsername(it)
            if (userDAO.isPresent) {
                return CustomUserDetails(userDAO.get().id,userDAO.get().username, userDAO.get().password, user.getAuthorities(userDAO.get().username)) // users.getAuthorities(userDAO.get().username)
            } else
                throw UsernameNotFoundException(username)
        }
        throw UsernameNotFoundException(username)
    }
}