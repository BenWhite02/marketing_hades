// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\common\exception\GlobalExceptionHandler.kt
// Fixed Global Exception Handler that doesn't interfere with Swagger

package com.kairos.hades.common.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.server.ServerWebExchange
import java.time.Instant

@RestControllerAdvice
class GlobalExceptionHandler {
    
    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    
    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFound(
        ex: EntityNotFoundException, 
        exchange: ServerWebExchange
    ): ResponseEntity<ErrorResponse> {
        logger.warn("Entity not found: {}", ex.message)
        
        // Don't handle Swagger-related requests
        if (isSwaggerRequest(exchange)) {
            throw ex
        }
        
        return createErrorResponse(
            code = "ENTITY_NOT_FOUND",
            message = ex.message ?: "Entity not found",
            status = HttpStatus.NOT_FOUND,
            exchange = exchange
        )
    }
    
    @ExceptionHandler(ValidationException::class)
    fun handleValidation(
        ex: ValidationException, 
        exchange: ServerWebExchange
    ): ResponseEntity<ErrorResponse> {
        logger.warn("Validation error: {}", ex.message)
        
        if (isSwaggerRequest(exchange)) {
            throw ex
        }
        
        return createErrorResponse(
            code = "VALIDATION_ERROR",
            message = ex.message ?: "Validation failed",
            status = HttpStatus.BAD_REQUEST,
            exchange = exchange,
            details = ex.details  // Fixed: use ex.details instead of ex.violations
        )
    }
    
    @ExceptionHandler(WebExchangeBindException::class)
    fun handleBindException(
        ex: WebExchangeBindException,
        exchange: ServerWebExchange
    ): ResponseEntity<ErrorResponse> {
        logger.warn("Binding error: {}", ex.message)
        
        if (isSwaggerRequest(exchange)) {
            throw ex
        }
        
        val errors = ex.bindingResult.fieldErrors.map { fieldError ->
            mapOf(
                "field" to fieldError.field,
                "message" to (fieldError.defaultMessage ?: "Invalid value"),
                "rejectedValue" to fieldError.rejectedValue
            )
        }
        
        return createErrorResponse(
            code = "VALIDATION_ERROR",
            message = "Request validation failed",
            status = HttpStatus.BAD_REQUEST,
            exchange = exchange,
            details = errors
        )
    }
    
    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDenied(
        ex: AccessDeniedException,
        exchange: ServerWebExchange
    ): ResponseEntity<ErrorResponse> {
        logger.warn("Access denied: {}", ex.message)
        
        if (isSwaggerRequest(exchange)) {
            throw ex
        }
        
        return createErrorResponse(
            code = "ACCESS_DENIED",
            message = ex.message ?: "Access denied",
            status = HttpStatus.FORBIDDEN,
            exchange = exchange
        )
    }
    
    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthentication(
        ex: AuthenticationException,
        exchange: ServerWebExchange
    ): ResponseEntity<ErrorResponse> {
        logger.warn("Authentication error: {}", ex.message)
        
        if (isSwaggerRequest(exchange)) {
            throw ex
        }
        
        return createErrorResponse(
            code = "AUTHENTICATION_ERROR",
            message = ex.message ?: "Authentication failed",
            status = HttpStatus.UNAUTHORIZED,
            exchange = exchange
        )
    }
    
    @ExceptionHandler(Exception::class)
    fun handleGeneral(
        ex: Exception,
        exchange: ServerWebExchange
    ): ResponseEntity<ErrorResponse> {
        logger.error("Unexpected error", ex)
        
        // CRITICAL: Don't handle Swagger requests
        if (isSwaggerRequest(exchange)) {
            throw ex
        }
        
        return createErrorResponse(
            code = "INTERNAL_ERROR",
            message = "An unexpected error occurred",
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            exchange = exchange
        )
    }
    
    private fun isSwaggerRequest(exchange: ServerWebExchange): Boolean {
        val path = exchange.request.path.value()
        return path.startsWith("/swagger-ui") ||
               path.startsWith("/v3/api-docs") ||
               path.startsWith("/api-docs") ||
               path.startsWith("/webjars") ||
               path.startsWith("/swagger-resources") ||
               path.startsWith("/configuration") ||
               path.startsWith("/swagger-config") ||
               path.contains("favicon.ico")
    }
    
    private fun createErrorResponse(
        code: String,
        message: String,
        status: HttpStatus,
        exchange: ServerWebExchange,
        details: Any? = null
    ): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            success = false,
            error = ErrorDetails(
                code = code,
                message = message,
                details = details
            ),
            meta = MetaInfo(
                timestamp = Instant.now(),
                requestId = generateRequestId()
            )
        )
        
        return ResponseEntity.status(status).body(response)
    }
    
    private fun generateRequestId(): String {
        return "req_${System.currentTimeMillis()}"
    }
}

data class ErrorResponse(
    val success: Boolean = false,
    val error: ErrorDetails,
    val meta: MetaInfo
)

data class ErrorDetails(
    val code: String,
    val message: String,
    val details: Any? = null
)