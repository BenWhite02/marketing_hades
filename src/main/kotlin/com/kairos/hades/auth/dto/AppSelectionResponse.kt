// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\auth\dto\AppSelectionResponse.kt
// App selection response DTO

package com.kairos.hades.auth.dto

import com.kairos.hades.app.dto.AppDto
import com.kairos.hades.app.entity.AppRole
import com.kairos.hades.app.entity.AppPermission
import java.time.Instant

data class AppSelectionResponse(
    val token: String,
    val app: AppDto,
    val role: AppRole,
    val permissions: Set<AppPermission>,
    val expiresAt: Instant
)
