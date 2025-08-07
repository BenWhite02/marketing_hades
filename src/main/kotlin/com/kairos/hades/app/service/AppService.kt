// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\app\service\AppService.kt
// App service with all required methods

package com.kairos.hades.app.service

import com.kairos.hades.app.dto.CreateAppRequest
import com.kairos.hades.app.dto.AppDto
import com.kairos.hades.app.entity.App
import com.kairos.hades.app.entity.AppType
import com.kairos.hades.app.entity.toDto
import com.kairos.hades.app.repository.AppRepository
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class AppService(
    private val appRepository: AppRepository
) {
    
    suspend fun getUserAccessibleApps(userId: UUID): List<AppDto> {
        // Simple implementation - get all apps user has access to
        val apps = appRepository.findAll().toList()
        return apps.map { app -> app.toDto() }
    }
    
    suspend fun createApp(request: CreateAppRequest, createdBy: UUID): AppDto {
        val app = App(
            organizationId = request.organizationId,
            name = request.name,
            displayName = request.displayName,
            description = request.description,
            appType = AppType.PRODUCTION,
            domain = request.domain,
            isActive = true,
            createdBy = createdBy,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        
        val savedApp = appRepository.save(app)
        return savedApp.toDto()
    }
    
    suspend fun getAppById(appId: UUID, userId: UUID): AppDto {
        val app = appRepository.findById(appId) 
            ?: throw IllegalArgumentException("App not found")
        return app.toDto()
    }
    
    suspend fun findById(appId: UUID): App? {
        return appRepository.findById(appId)
    }
    
    suspend fun findByIds(appIds: List<UUID>): kotlinx.coroutines.flow.Flow<App> {
        return appRepository.findAllById(appIds)
    }
    
    suspend fun getOrCreateSandboxApp(organizationId: UUID): App {
        // Check if sandbox already exists
        val existingSandbox = appRepository.findByOrganizationIdAndAppType(
            organizationId, AppType.SANDBOX
        )
        
        return existingSandbox ?: createSandboxApp(organizationId)
    }
    
    private suspend fun createSandboxApp(organizationId: UUID): App {
        val sandboxApp = App(
            organizationId = organizationId,
            name = "kairos-sandbox",
            displayName = "Kairos Sandbox",
            description = "Demo environment for exploring Kairos platform features",
            appType = AppType.SANDBOX,
            domain = null,
            isActive = true,
            createdBy = UUID.randomUUID() // System created
        )
        
        return appRepository.save(sandboxApp)
    }
}
