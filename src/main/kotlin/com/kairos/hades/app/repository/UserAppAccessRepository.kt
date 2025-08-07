// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\app\repository\UserAppAccessRepository.kt
// User app access repository interface

package com.kairos.hades.app.repository

import com.kairos.hades.app.entity.UserAppAccess
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.r2dbc.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserAppAccessRepository : CoroutineCrudRepository<UserAppAccess, UUID> {
    
    @Query("SELECT * FROM user_app_access WHERE user_id = :userId AND is_active = :isActive")
    fun findByUserIdAndIsActive(userId: UUID, isActive: Boolean): Flow<UserAppAccess>
    
    @Query("SELECT * FROM user_app_access WHERE user_id = :userId AND app_id = :appId AND is_active = true")
    suspend fun findByUserIdAndAppId(userId: UUID, appId: UUID): UserAppAccess?
    
    @Query("SELECT EXISTS(SELECT 1 FROM user_app_access WHERE user_id = :userId AND app_id = :appId AND is_active = :isActive)")
    suspend fun existsByUserIdAndAppIdAndIsActive(userId: UUID, appId: UUID, isActive: Boolean): Boolean
    
    @Query("SELECT * FROM user_app_access WHERE app_id = :appId AND is_active = true")
    fun findByAppId(appId: UUID): Flow<UserAppAccess>
}
