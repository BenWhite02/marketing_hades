// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\user\service\UserService.kt
// User service with proper Flow handling and coroutines

package com.kairos.hades.user.service

import com.kairos.hades.user.entity.User
import com.kairos.hades.user.entity.GlobalRole
import com.kairos.hades.user.entity.toDto
import com.kairos.hades.user.repository.UserRepository
import com.kairos.hades.organization.repository.OrganizationRepository
import com.kairos.hades.auth.dto.RegisterRequest
import com.kairos.hades.auth.dto.RegisterResponse
import com.kairos.hades.auth.dto.UserProfileResponse
import com.kairos.hades.common.extensions.toList
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val organizationRepository: OrganizationRepository,
    private val passwordEncoder: PasswordEncoder
) {
    
    suspend fun createUser(
        organizationId: UUID,
        email: String,
        password: String,
        firstName: String?,
        lastName: String?,
        globalRole: GlobalRole = GlobalRole.USER
    ): User {
        
        // Check if organization exists and use it for validation
        val organization = organizationRepository.findById(organizationId)
            ?: throw IllegalArgumentException("Organization not found with ID: $organizationId")
        
        // Validate organization is active
        if (!organization.isActive) {
            throw IllegalArgumentException("Organization '${organization.name}' is not active")
        }
        
        // Check if user already exists
        val existingUser = userRepository.findByEmail(email)
        if (existingUser != null) {
            throw IllegalArgumentException("User with email '$email' already exists")
        }
        
        // Create new user
        val user = User(
            organizationId = organizationId,
            email = email,
            passwordHash = passwordEncoder.encode(password),
            firstName = firstName,
            lastName = lastName,
            globalRole = globalRole,
            isActive = true,
            lastLoginAt = null,
            emailVerified = false,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        
        return userRepository.save(user)
    }
    
    suspend fun registerUser(request: RegisterRequest): RegisterResponse {
        val user = createUser(
            organizationId = UUID.randomUUID(), // For now, create a default org
            email = request.email,
            password = request.password,
            firstName = request.firstName,
            lastName = request.lastName,
            globalRole = GlobalRole.USER
        )
        
        return RegisterResponse(
            user = user.toDto(),
            message = "User registered successfully"
        )
    }
    
    suspend fun getUserProfile(userId: UUID): UserProfileResponse {
        val user = userRepository.findById(userId)
            ?: throw IllegalArgumentException("User not found with ID: $userId")
        
        return UserProfileResponse(
            id = user.id,
            organizationId = user.organizationId,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            globalRole = user.globalRole,
            isActive = user.isActive,
            emailVerified = user.emailVerified,
            lastLoginAt = user.lastLoginAt,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )
    }
    
    suspend fun getUserById(userId: UUID): User? {
        return userRepository.findById(userId)
    }
    
    suspend fun getUserByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }
    
    suspend fun updateLastLogin(userId: UUID): User? {
        val user = userRepository.findById(userId) ?: return null
        val updatedUser = user.copy(
            lastLoginAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        return userRepository.save(updatedUser)
    }
    
    // FIXED: Proper Flow handling using extension
    suspend fun getUsersByOrganization(organizationId: UUID): List<User> {
        return userRepository.findByOrganizationId(organizationId).toList()
    }
}
