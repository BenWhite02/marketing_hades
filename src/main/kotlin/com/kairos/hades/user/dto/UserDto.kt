// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\user\dto\UserDto.kt
// User DTO

package com.kairos.hades.user.dto

import com.kairos.hades.user.entity.GlobalRole
import java.time.LocalDateTime
import java.util.*

data class UserDto(
    val id: UUID,
    val organizationId: UUID,
    val email: String,
    val firstName: String?,
    val lastName: String?,
    val globalRole: GlobalRole,
    val isActive: Boolean,
    val emailVerified: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
