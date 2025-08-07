// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\auth\dto\RegisterRequest.kt
// Register request DTO

package com.kairos.hades.auth.dto

data class RegisterRequest(
    val email: String,
    val password: String,
    val firstName: String?,
    val lastName: String?,
    val organizationName: String?
)
