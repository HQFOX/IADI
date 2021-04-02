package pt.unl.fct.grantapp.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
class SecurityConfig(val customUserDetailsService: CustomUserDetailsService) : WebSecurityConfigurerAdapter() {

    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(passwordEncoder().encode("admin"))
                .authorities(emptyList())
                .roles("ADMIN")
                .and().passwordEncoder(passwordEncoder())
                .and().userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder())
    }

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/v2/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                //.anyRequest().authenticated()
                .and()
                .addFilterBefore(UserPasswordAuthenticationFilterToJWT ("/login", super.authenticationManagerBean()),
                        BasicAuthenticationFilter::class.java)
                .addFilterBefore(JWTAuthenticationFilter(),
                        BasicAuthenticationFilter::class.java)
                //.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }


}