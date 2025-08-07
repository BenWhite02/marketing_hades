// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\config\OpenAPIConfig.kt
// OpenAPI/Swagger Configuration for Hades Backend

package com.kairos.hades.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenAPIConfig {
    
    @Value("${'$'}{server.port:8080}")
    private val serverPort: String = "8080"
    
    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(apiInfo())
            .servers(
                listOf(
                    Server().url("http://localhost:$serverPort").description("Local Development Server")
                )
            )
            .components(
                Components().addSecuritySchemes(
                    "Bearer Authentication",
                    SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .`in`(SecurityScheme.In.HEADER)
                        .name("Authorization")
                )
            )
            .addSecurityItem(
                SecurityRequirement().addList("Bearer Authentication")
            )
    }
    
    private fun apiInfo(): Info {
        return Info()
            .title("Hades Backend API")
            .description("""
                Next-generation marketing platform backend with AI-driven personalization.
                
                ## Authentication
                Most endpoints require JWT authentication. Use the /api/v1/auth/login endpoint to obtain a token.
                
                ## Multi-App Architecture
                After authentication, select an app context using /api/v1/auth/apps/{appId}/select
            """.trimIndent())
            .version("1.0.0")
            .contact(
                Contact()
                    .name("Kairos Development Team")
                    .email("dev@kairos.com")
                    .url("https://kairos.com")
            )
            .license(
                License()
                    .name("Proprietary")
                    .url("https://kairos.com/license")
            )
    }
}