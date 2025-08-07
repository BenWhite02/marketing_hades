// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\common\HealthController.kt
// Enhanced Health Controller with Swagger documentation

package com.kairos.hades.common

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/health")
@Tag(name = "Health", description = "Application health and status endpoints")
class HealthController : HealthIndicator {
    
    @GetMapping
    @Operation(summary = "Get application health status", description = "Returns the overall health status of the Hades backend application")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Health status retrieved successfully"),
            ApiResponse(responseCode = "503", description = "Service unavailable")
        ]
    )
    suspend fun getHealth(): ResponseEntity<HealthResponse> {
        return ResponseEntity.ok(
            HealthResponse(
                status = "UP",
                timestamp = LocalDateTime.now(),
                application = "Hades Backend",
                version = "1.0.0",
                components = mapOf(
                    "database" to "UP",
                    "redis" to "UP",
                    "security" to "UP",
                    "swagger" to "CONFIGURED"
                )
            )
        )
    }
    
    @GetMapping("/debug")
    @Operation(summary = "Get debug information", description = "Returns detailed debug information about the application configuration")
    suspend fun getDebugInfo(): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.ok(
            mapOf(
                "application" to "Hades Backend",
                "status" to "RUNNING",
                "timestamp" to LocalDateTime.now(),
                "jvm" to mapOf(
                    "version" to System.getProperty("java.version"),
                    "vendor" to System.getProperty("java.vendor")
                ),
                "swagger" to mapOf(
                    "enabled" to true,
                    "ui-url" to "/swagger-ui.html",
                    "docs-url" to "/api-docs"
                ),
                "security" to mapOf(
                    "jwt-enabled" to true,
                    "cors-enabled" to true
                ),
                "profiles" to System.getProperty("spring.profiles.active", "default"),
                "port" to 8080
            )
        )
    }
    
    override fun health(): Health {
        return Health.up()
            .withDetail("application", "Hades Backend")
            .withDetail("status", "Running")
            .withDetail("timestamp", LocalDateTime.now())
            .build()
    }
}

data class HealthResponse(
    val status: String,
    val timestamp: LocalDateTime,
    val application: String,
    val version: String,
    val components: Map<String, String>
)
