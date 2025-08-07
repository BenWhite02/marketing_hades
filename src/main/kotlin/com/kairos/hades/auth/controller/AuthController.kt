// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\auth\controller\AuthController.kt
// Fixed Authentication REST Controller

package com.kairos.hades.auth.controller

import com.kairos.hades.auth.dto.*
import com.kairos.hades.auth.service.AuthenticationService
import com.kairos.hades.common.exception.MetaInfo
import com.kairos.hades.common.exception.SuccessResponse
import com.kairos.hades.security.jwt.JwtService
import com.kairos.hades.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "User authentication and authorization endpoints")
class AuthController(
    private val authenticationService: AuthenticationService,
    private val userService: UserService,
    private val jwtService: JwtService
) {
    
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user with email and password")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Login successful"),
            ApiResponse(responseCode = "401", description = "Invalid credentials"),
            ApiResponse(responseCode = "400", description = "Invalid request format")
        ]
    )
    suspend fun login(@RequestBody @Valid request: LoginRequest): ResponseEntity<SuccessResponse<LoginResponse>> {
        val response = authenticationService.login(request)
        return ResponseEntity.ok(
            SuccessResponse(
                data = response,
                meta = createMeta()
            )
        )
    }
    
    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Register a new user account")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Registration successful"),
            ApiResponse(responseCode = "400", description = "Invalid registration data"),
            ApiResponse(responseCode = "409", description = "User already exists")
        ]
    )
    suspend fun register(@RequestBody @Valid request: RegisterRequest): ResponseEntity<SuccessResponse<RegisterResponse>> {
        val response = userService.registerUser(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            SuccessResponse(
                data = response,
                meta = createMeta()
            )
        )
    }
    
    @PostMapping("/refresh")
    @Operation(summary = "Refresh JWT token", description = "Get a new access token using refresh token")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            ApiResponse(responseCode = "401", description = "Invalid refresh token")
        ]
    )
    suspend fun refreshToken(@RequestBody request: RefreshTokenRequest): ResponseEntity<SuccessResponse<TokenRefreshResponse>> {
        val response = authenticationService.refreshToken(request.refreshToken)
        return ResponseEntity.ok(
            SuccessResponse(
                data = response,
                meta = createMeta()
            )
        )
    }
    
    @GetMapping("/me")
    @Operation(summary = "Get current user profile", description = "Get the profile of the currently authenticated user")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            ApiResponse(responseCode = "401", description = "Authentication required")
        ]
    )
    suspend fun getCurrentUser(
        @RequestHeader("Authorization") authorization: String
    ): ResponseEntity<SuccessResponse<UserProfileResponse>> {
        val userId = jwtService.extractUserId(authorization)
        val user = userService.getUserProfile(userId)
        return ResponseEntity.ok(
            SuccessResponse(
                data = user,
                meta = createMeta()
            )
        )
    }
    
    @GetMapping("/apps")
    @Operation(summary = "Get user accessible apps", description = "Get list of apps the user has access to")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Apps retrieved successfully"),
            ApiResponse(responseCode = "401", description = "Authentication required")
        ]
    )
    suspend fun getUserApps(
        @RequestHeader("Authorization") authorization: String
    ): ResponseEntity<SuccessResponse<UserAppsResponse>> {
        val userId = jwtService.extractUserId(authorization)
        val apps = authenticationService.getUserApps(userId)
        return ResponseEntity.ok(
            SuccessResponse(
                data = apps,
                meta = createMeta()
            )
        )
    }
    
    @PostMapping("/apps/{appId}/select")
    @Operation(summary = "Select app context", description = "Select an app and get app-scoped JWT token")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "App selected successfully"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
            ApiResponse(responseCode = "403", description = "Access denied to app"),
            ApiResponse(responseCode = "404", description = "App not found")
        ]
    )
    suspend fun selectApp(
        @PathVariable appId: UUID,
        @RequestHeader("Authorization") authorization: String
    ): ResponseEntity<SuccessResponse<AppSelectionResponse>> {
        val userId = jwtService.extractUserId(authorization)
        val response = authenticationService.selectApp(userId, appId)
        return ResponseEntity.ok(
            SuccessResponse(
                data = response,
                meta = createMeta()
            )
        )
    }
    
    @GetMapping("/current-app")
    @Operation(summary = "Get current app context", description = "Get the currently selected app context")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "App context retrieved"),
            ApiResponse(responseCode = "204", description = "No app context selected"),
            ApiResponse(responseCode = "401", description = "Authentication required")
        ]
    )
    suspend fun getCurrentAppContext(
        @RequestHeader("Authorization") authorization: String
    ): ResponseEntity<SuccessResponse<AppContextResponse?>> {
        val userId = jwtService.extractUserId(authorization)
        val context = authenticationService.getCurrentAppContext(userId)
        return ResponseEntity.ok(
            SuccessResponse(
                data = context,
                meta = createMeta()
            )
        )
    }
    
    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Logout the current user and invalidate tokens")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Logout successful"),
            ApiResponse(responseCode = "401", description = "Authentication required")
        ]
    )
    suspend fun logout(
        @RequestHeader("Authorization") authorization: String
    ): ResponseEntity<SuccessResponse<Map<String, String>>> {
        val userId = jwtService.extractUserId(authorization)
        authenticationService.logout(userId)
        return ResponseEntity.ok(
            SuccessResponse(
                data = mapOf("message" to "Logged out successfully"),
                meta = createMeta()
            )
        )
    }
    
    private fun createMeta(): MetaInfo {
        return MetaInfo(
            timestamp = Instant.now(),
            requestId = "req_${System.currentTimeMillis()}"
        )
    }
}