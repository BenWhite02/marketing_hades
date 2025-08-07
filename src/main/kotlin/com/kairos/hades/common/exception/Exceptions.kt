// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\common\exception\Exceptions.kt
// All custom exception classes

package com.kairos.hades.common.exception

// Base application exception
abstract class ApplicationException(
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause)

// Entity not found exception
class EntityNotFoundException(
    message: String = "Entity not found"
) : ApplicationException(message)

// Validation exception with details
class ValidationException(
    message: String = "Validation failed",
    val details: Any? = null
) : ApplicationException(message)

// Authentication exception
class AuthenticationException(
    message: String = "Authentication failed"
) : ApplicationException(message)

// Access denied exception
class AccessDeniedException(
    message: String = "Access denied"
) : ApplicationException(message)

// Channel connection exception
class ChannelConnectionException(
    message: String = "Channel connection failed",
    cause: Throwable? = null
) : ApplicationException(message, cause)

// Insufficient permission exception
class InsufficientPermissionException(
    message: String = "Insufficient permissions"
) : ApplicationException(message)

// App context exception
class AppContextException(
    message: String = "App context error"
) : ApplicationException(message)

// JWT related exception
class JwtException(
    message: String = "JWT processing error",
    cause: Throwable? = null
) : ApplicationException(message, cause)