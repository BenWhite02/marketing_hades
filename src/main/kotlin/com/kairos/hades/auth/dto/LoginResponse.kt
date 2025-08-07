// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\auth\dto\LoginResponse.kt
// Login response DTO

package com.kairos.hades.auth.dto

import com.kairos.hades.user.dto.UserDto
import java.time.Instant

data class LoginResponse(
    val token: String,
    val refreshToken: String,
    val user: UserDto,
    val requiresAppSelection: Boolean = true,
    val expiresAt: Instant = Instant.now().plusSeconds(3600)
)
