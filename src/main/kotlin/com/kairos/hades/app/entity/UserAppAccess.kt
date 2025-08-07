// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\app\entity\UserAppAccess.kt
// User app access entity

package com.kairos.hades.app.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table("user_app_access")
data class UserAppAccess(
    @Id val id: UUID = UUID.randomUUID(),
    val userId: UUID,
    val appId: UUID,
    val role: AppRole,
    val permissions: Set<AppPermission> = emptySet(),
    val grantedBy: UUID,
    val grantedAt: LocalDateTime = LocalDateTime.now(),
    val expiresAt: LocalDateTime? = null,
    val isActive: Boolean = true
)
