package pt.unl.fct.grantapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GrantSystemApplication

fun main(args: Array<String>) {
    runApplication<GrantSystemApplication>(*args)
}