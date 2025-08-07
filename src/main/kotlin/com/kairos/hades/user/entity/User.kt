// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\user\entity\User.kt
// User entity

package com.kairos.hades.user.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table("users")
data class User(
    @Id val id: UUID = UUID.randomUUID(),
    val organizationId: UUID,
    val email: String,
    val passwordHash: String,
    val firstName: String?,
    val lastName: String?,
    val globalRole: GlobalRole = GlobalRole.USER,
    val isActive: Boolean = true,
    val lastLoginAt: LocalDateTime?,
    val emailVerified: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun getFullName(): String = "${firstName ?: ""} ${lastName ?: ""}".trim()
}
