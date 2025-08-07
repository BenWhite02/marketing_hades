// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\config\SecurityConfig.kt
// Simplified Security Configuration for Debugging Swagger Issues

package com.kairos.hades.config

import com.kairos.hades.security.jwt.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {
    
    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .csrf { it.disable() }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .authorizeExchange { exchanges ->
                exchanges
                    // SWAGGER/OPENAPI - COMPLETELY OPEN FOR DEBUGGING
                    .pathMatchers(
                        "/swagger-ui.html", 
                        "/swagger-ui/**", 
                        "/swagger-ui/index.html"
                    ).permitAll()
                    .pathMatchers(
                        "/v3/api-docs/**", 
                        "/api-docs/**", 
                        "/api-docs"
                    ).permitAll()
                    .pathMatchers(
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/configuration/**",
                        "/swagger-config/**"
                    ).permitAll()
                    .pathMatchers("/favicon.ico").permitAll()
                    
                    // ACTUATOR ENDPOINTS
                    .pathMatchers("/actuator/**").permitAll()
                    
                    // HEALTH ENDPOINTS  
                    .pathMatchers("/api/v1/health/**").permitAll()
                    
                    // AUTH ENDPOINTS
                    .pathMatchers("/api/v1/auth/login", "/api/v1/auth/register").permitAll()
                    .pathMatchers("/api/v1/auth/refresh").permitAll()
                    
                    // TEMPORARY: Make all other auth endpoints public for debugging
                    .pathMatchers("/api/v1/auth/**").permitAll()
                    .pathMatchers("/api/v1/apps/**").permitAll()
                    
                    // Default: require authentication for everything else
                    .anyExchange().authenticated()
            }
            .addFilterBefore(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .build()
    }
    
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            // Allow all origins for debugging
            allowedOriginPatterns = listOf("*")
            allowedOrigins = listOf("*")
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH")
            allowedHeaders = listOf("*")
            allowCredentials = false // Set to false when allowing all origins
            maxAge = 3600L
            exposedHeaders = listOf("Authorization", "Content-Type")
        }
        
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
    
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}