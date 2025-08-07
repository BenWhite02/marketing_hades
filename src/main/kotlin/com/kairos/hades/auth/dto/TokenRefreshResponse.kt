// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\auth\dto\TokenRefreshResponse.kt
// Token refresh response DTO

package com.kairos.hades.auth.dto

import java.time.Instant

data class TokenRefreshResponse(
    val token: String,
    val expiresAt: Instant
)
