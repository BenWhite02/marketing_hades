// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\app\dto\CreateAppRequest.kt
// Create app request DTO

package com.kairos.hades.app.dto

import com.fasterxml.jackson.databind.JsonNode
import java.util.*

data class CreateAppRequest(
    val name: String,
    val displayName: String,
    val description: String?,
    val organizationId: UUID,
    val domain: String?,
    val settings: JsonNode?
)
