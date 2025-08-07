// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\auth\dto\AppContextResponse.kt
// App context response DTO

package com.kairos.hades.auth.dto

import com.kairos.hades.app.entity.AppRole
import com.kairos.hades.app.entity.AppPermission
import java.time.Instant
import java.util.*

data class AppContextResponse(
    val appId: UUID,
    val userId: UUID,
    val role: AppRole,
    val permissions: Set<AppPermission>,
    val selectedAt: Instant
)
