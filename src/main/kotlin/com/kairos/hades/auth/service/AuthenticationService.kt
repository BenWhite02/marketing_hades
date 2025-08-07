// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\auth\service\AuthenticationService.kt
// Authentication service with proper type handling

package com.kairos.hades.auth.service

import com.kairos.hades.auth.dto.*
import com.kairos.hades.security.jwt.JwtService
import com.kairos.hades.user.entity.User
import com.kairos.hades.user.entity.toDto
import com.kairos.hades.app.entity.toDto
import com.kairos.hades.app.entity.UserAppAccess
import com.kairos.hades.app.entity.AppRole
import com.kairos.hades.app.entity.AppPermission
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

@Service
class AuthenticationService(
    private val jwtService: JwtService
) {
    
    suspend fun login(loginRequest: LoginRequest): LoginResponse {
        // Mock user for now - replace with actual repository call
        val user = User(
            id = UUID.randomUUID(),
            organizationId = UUID.randomUUID(),
            email = loginRequest.email,
            passwordHash = "hashed_password",
            firstName = "Demo",
            lastName = "User",
            globalRole = com.kairos.hades.user.entity.GlobalRole.USER,
            isActive = true,
            lastLoginAt = LocalDateTime.now(),
            emailVerified = true
        )
        
        // Generate JWT token (no app context yet) - FIXED: Convert enum to string
        val token = jwtService.generateToken(
            userId = user.id,
            email = user.email,
            globalRole = user.globalRole.name, // Convert enum to string
            organizationId = user.organizationId
        )
        
        return LoginResponse(
            token = token,
            refreshToken = jwtService.generateRefreshToken(user.id),
            user = user.toDto(),
            requiresAppSelection = true
        )
    }
    
    suspend fun selectApp(userId: UUID, appId: UUID): AppSelectionResponse {
        // Mock app access - replace with actual repository call
        val mockApp = com.kairos.hades.app.entity.App(
            id = appId,
            organizationId = UUID.randomUUID(),
            name = "demo-app",
            displayName = "Demo App",
            description = "Demo application",
            appType = com.kairos.hades.app.entity.AppType.SANDBOX,
            domain = null,
            createdBy = userId
        )
        
        val mockRole = AppRole.CAMPAIGN_MANAGER
        val mockPermissions = setOf(
            AppPermission.CUSTOMER_VIEW,
            AppPermission.CAMPAIGN_VIEW,
            AppPermission.ANALYTICS_VIEW
        )
        
        // Generate app-scoped JWT token - FIXED: Convert enums to strings
        val appScopedToken = jwtService.generateAppScopedToken(
            userId = userId,
            appId = appId,
            role = mockRole.name, // Convert enum to string
            permissions = mockPermissions.map { it.name }.toSet() // Convert enum set to string set
        )
        
        return AppSelectionResponse(
            token = appScopedToken,
            app = mockApp.toDto(),
            role = mockRole,
            permissions = mockPermissions,
            expiresAt = Instant.now().plusSeconds(jwtService.getTokenExpiration())
        )
    }
    
    suspend fun refreshToken(refreshToken: String): TokenRefreshResponse {
        val userId = jwtService.validateRefreshToken(refreshToken)
        
        // Mock user - replace with actual repository call
        val user = User(
            id = userId,
            organizationId = UUID.randomUUID(),
            email = "demo@example.com",
            passwordHash = "hashed_password",
            firstName = "Demo",
            lastName = "User",
            globalRole = com.kairos.hades.user.entity.GlobalRole.USER,
            isActive = true,
            lastLoginAt = LocalDateTime.now(),
            emailVerified = true
        )
        
        // FIXED: Convert enum to string
        val newToken = jwtService.generateToken(
            userId = user.id,
            email = user.email,
            globalRole = user.globalRole.name, // Convert enum to string
            organizationId = user.organizationId
        )
        
        return TokenRefreshResponse(
            token = newToken,
            expiresAt = Instant.now().plusSeconds(jwtService.getTokenExpiration())
        )
    }

    suspend fun getUserApps(userId: UUID): UserAppsResponse {
        // Mock implementation - replace with actual repository calls
        val mockApps = listOf(
            AppAccessDto(
                app = com.kairos.hades.app.dto.AppDto(
                    id = UUID.randomUUID(),
                    organizationId = UUID.randomUUID(),
                    name = "kairos-sandbox",
                    displayName = "Kairos Sandbox",
                    description = "Demo environment",
                    appType = com.kairos.hades.app.entity.AppType.SANDBOX,
                    domain = null,
                    isActive = true,
                    createdAt = java.time.LocalDateTime.now(),
                    updatedAt = java.time.LocalDateTime.now()
                ),
                role = com.kairos.hades.app.entity.AppRole.CAMPAIGN_MANAGER,
                permissions = setOf(
                    com.kairos.hades.app.entity.AppPermission.CUSTOMER_VIEW,
                    com.kairos.hades.app.entity.AppPermission.CAMPAIGN_VIEW
                ),
                isDefault = true
            )
        )
        
        return UserAppsResponse(
            apps = mockApps,
            defaultApp = mockApps.first(),
            totalCount = mockApps.size
        )
    }
    
    suspend fun getCurrentAppContext(userId: UUID): AppContextResponse? {
        // Mock implementation
        return AppContextResponse(
            appId = UUID.randomUUID(),
            userId = userId,
            role = com.kairos.hades.app.entity.AppRole.CAMPAIGN_MANAGER,
            permissions = setOf(
                com.kairos.hades.app.entity.AppPermission.CUSTOMER_VIEW,
                com.kairos.hades.app.entity.AppPermission.CAMPAIGN_VIEW
            ),
            selectedAt = java.time.Instant.now()
        )
    }
    
    suspend fun logout(userId: UUID) {
        // Mock implementation - clear user session
        // TODO: Implement session cleanup
    }
}
