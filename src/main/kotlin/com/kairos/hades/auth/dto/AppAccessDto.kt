// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\auth\dto\AppAccessDto.kt
// App access DTO

package com.kairos.hades.auth.dto

import com.kairos.hades.app.dto.AppDto
import com.kairos.hades.app.entity.AppRole
import com.kairos.hades.app.entity.AppPermission
import java.time.LocalDateTime

data class AppAccessDto(
    val app: AppDto,
    val role: AppRole,
    val permissions: Set<AppPermission>,
    val isDefault: Boolean = false,
    val grantedAt: LocalDateTime = LocalDateTime.now(),
    val expiresAt: LocalDateTime? = null
)
