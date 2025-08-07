// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\auth\dto\UserProfileResponse.kt
// User profile response DTO

package com.kairos.hades.auth.dto

import com.kairos.hades.user.entity.GlobalRole
import java.time.LocalDateTime
import java.util.*

data class UserProfileResponse(
    val id: UUID,
    val organizationId: UUID,
    val email: String,
    val firstName: String?,
    val lastName: String?,
    val globalRole: GlobalRole,
    val isActive: Boolean,
    val emailVerified: Boolean,
    val lastLoginAt: LocalDateTime?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
