package pt.unl.fct.grantapp.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfiguration {
    @Bean
    fun api(): Docket =
            Docket(DocumentationType.SWAGGER_2)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("pt.unl.fct.grantapp"))
                    .paths(PathSelectors.any())
                    .build().apiInfo(apiEndPointsInfo())

    fun apiEndPointsInfo(): ApiInfo =
            ApiInfoBuilder()
                    .title("Grant Application")
                    .description("Grant Application - Group 12 - IADI 2020/2021")
                    .contact(Contact("Group 12", "https://www.fct.unl.pt/", "g12@iadi"))
                    .version("1.0.0")
                    .build()
}