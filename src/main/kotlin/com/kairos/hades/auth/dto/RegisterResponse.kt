// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\auth\dto\RegisterResponse.kt
// Register response DTO

package com.kairos.hades.auth.dto

import com.kairos.hades.user.dto.UserDto
import java.time.Instant

data class RegisterResponse(
    val user: UserDto,
    val message: String = "User registered successfully",
    val emailVerificationRequired: Boolean = true,
    val createdAt: Instant = Instant.now()
)
