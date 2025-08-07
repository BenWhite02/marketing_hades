package com.kairos.hades.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
@EnableWebFlux
class WebConfig : WebFluxConfigurer {
    
    @Bean
    fun corsWebFilter(): CorsWebFilter {
        val corsConfig = CorsConfiguration()
        
        // Set allowed origins
        corsConfig.allowedOriginPatterns = listOf("http://localhost:5173", "http://localhost:3000", "http://localhost:8080")
        
        // Set allowed methods
        corsConfig.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        
        // Set allowed headers
        corsConfig.allowedHeaders = listOf("*")
        
        // Allow credentials
        corsConfig.allowCredentials = true
        
        // Set max age
        corsConfig.maxAge = 3600L
        
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfig)
        
        return CorsWebFilter(source)
    }
}
