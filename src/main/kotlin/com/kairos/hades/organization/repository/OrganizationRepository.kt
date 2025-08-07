// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\organization\repository\OrganizationRepository.kt
// Organization repository interface

package com.kairos.hades.organization.repository

import com.kairos.hades.organization.entity.Organization
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.r2dbc.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OrganizationRepository : CoroutineCrudRepository<Organization, UUID> {
    
    @Query("SELECT * FROM organizations WHERE domain = :domain AND is_active = true")
    suspend fun findByDomain(domain: String): Organization?
    
    @Query("SELECT * FROM organizations WHERE is_active = true")
    fun findAllActive(): Flow<Organization>
}
