package com.myorg.demo.config
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
                .components(Components())
                .info(Info()
                        .title("Bozo Wonderland")
                        .description("This is the API documentation for my fantastic awesome incredible software."))
    }
}
