// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\debug\SwaggerDebugController.kt
// Debug controller to test Swagger functionality

package com.kairos.hades.debug

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/debug")
@Tag(name = "Debug", description = "Debug endpoints for testing Swagger functionality")
class SwaggerDebugController {
    
    @GetMapping("/test")
    @Operation(summary = "Test endpoint", description = "Simple test endpoint to verify Swagger is working")
    fun testEndpoint(): ResponseEntity<TestResponse> {
        return ResponseEntity.ok(
            TestResponse(
                message = "Swagger is working!",
                timestamp = LocalDateTime.now(),
                status = "SUCCESS"
            )
        )
    }
    
    @GetMapping("/swagger-info")
    @Operation(summary = "Swagger configuration info", description = "Returns information about Swagger configuration")
    fun getSwaggerInfo(): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.ok(
            mapOf(
                "swagger-ui" to "http://localhost:8080/swagger-ui.html",
                "api-docs" to "http://localhost:8080/api-docs",
                "springdoc-version" to "2.3.0",
                "openapi-version" to "3.0.1",
                "status" to "CONFIGURED",
                "timestamp" to LocalDateTime.now()
            )
        )
    }
    
    @GetMapping("/health-simple")
    @Operation(summary = "Simple health check", description = "Basic health check endpoint")
    fun simpleHealth(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(
            mapOf(
                "status" to "UP",
                "application" to "Hades Backend",
                "message" to "Application is running successfully"
            )
        )
    }
}

data class TestResponse(
    val message: String,
    val timestamp: LocalDateTime,
    val status: String
)