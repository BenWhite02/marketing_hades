// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\app\dto\AppDto.kt
// App DTO

package com.kairos.hades.app.dto

import com.kairos.hades.app.entity.AppType
import java.time.LocalDateTime
import java.util.*

data class AppDto(
    val id: UUID,
    val organizationId: UUID,
    val name: String,
    val displayName: String,
    val description: String?,
    val appType: AppType,
    val domain: String?,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
