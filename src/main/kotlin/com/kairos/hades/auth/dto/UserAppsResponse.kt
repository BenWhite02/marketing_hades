// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\auth\dto\UserAppsResponse.kt
// User apps response DTO

package com.kairos.hades.auth.dto

data class UserAppsResponse(
    val apps: List<AppAccessDto>,
    val defaultApp: AppAccessDto?,
    val totalCount: Int
)
