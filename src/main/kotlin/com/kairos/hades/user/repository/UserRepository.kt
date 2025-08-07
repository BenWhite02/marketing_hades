// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\user\repository\UserRepository.kt
// User repository using proper Kotlin Coroutines

package com.kairos.hades.user.repository

import com.kairos.hades.common.repository.BaseCoroutineRepository
import com.kairos.hades.user.entity.User
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : BaseCoroutineRepository<User, UUID> {
    
    @Query("SELECT * FROM users WHERE email = :email AND is_active = :isActive")
    suspend fun findByEmailAndIsActive(email: String, isActive: Boolean): User?
    
    @Query("SELECT * FROM users WHERE organization_id = :organizationId AND is_active = true")
    fun findByOrganizationId(organizationId: UUID): Flow<User>
    
    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun findByEmail(email: String): User?
    
    @Query("UPDATE users SET last_login_at = :lastLogin, updated_at = NOW() WHERE id = :id")
    suspend fun updateLastLogin(id: UUID, lastLogin: java.time.LocalDateTime): Int
    
    @Query("SELECT COUNT(*) FROM users WHERE organization_id = :organizationId AND is_active = true")
    suspend fun countByOrganizationId(organizationId: UUID): Long
    
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)")
    suspend fun existsByEmail(email: String): Boolean
}
