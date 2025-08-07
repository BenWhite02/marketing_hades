// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\user\entity\UserExtensions.kt
// User entity extensions

package com.kairos.hades.user.entity

import com.kairos.hades.user.dto.UserDto

fun User.toDto(): UserDto {
    return UserDto(
        id = this.id,
        organizationId = this.organizationId,
        email = this.email,
        firstName = this.firstName,
        lastName = this.lastName,
        globalRole = this.globalRole,
        isActive = this.isActive,
        emailVerified = this.emailVerified,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
