// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\common\repository\AppScopedRepository.kt
// Repository interface for app-scoped entities - base interface only

package com.kairos.hades.common.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.NoRepositoryBean
import java.util.*

@NoRepositoryBean
interface AppScopedRepository<T> : BaseCoroutineRepository<T, UUID> {
    
    // Base interface for app-scoped entities
    // Concrete implementations will provide the @Query annotations
    // for specific table names to avoid dynamic table name issues
    
    // Common app-scoped method signatures that implementations will override
    fun findByAppId(appId: UUID): Flow<T>
    suspend fun findByIdAndAppId(id: UUID, appId: UUID): T?
    suspend fun deleteByIdAndAppId(id: UUID, appId: UUID): Long
    suspend fun countByAppId(appId: UUID): Long
    suspend fun existsByIdAndAppId(id: UUID, appId: UUID): Boolean
}