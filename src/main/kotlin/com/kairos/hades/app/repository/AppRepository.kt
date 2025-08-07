// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\app\repository\AppRepository.kt
// App repository interface

package com.kairos.hades.app.repository

import com.kairos.hades.app.entity.App
import com.kairos.hades.app.entity.AppType
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.r2dbc.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AppRepository : CoroutineCrudRepository<App, UUID> {
    
    @Query("SELECT * FROM apps WHERE organization_id = :organizationId AND is_active = true")
    fun findByOrganizationId(organizationId: UUID): Flow<App>
    
    @Query("SELECT * FROM apps WHERE organization_id = :organizationId AND app_type = :appType AND is_active = true")
    suspend fun findByOrganizationIdAndAppType(organizationId: UUID, appType: AppType): App?
    
    @Query("SELECT * FROM apps WHERE id IN (:ids) AND is_active = true")
    fun findAllById(ids: List<UUID>): Flow<App>
    
    @Query("SELECT * FROM apps WHERE is_active = true")
    override fun findAll(): Flow<App>
}
