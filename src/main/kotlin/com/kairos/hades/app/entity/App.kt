// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\app\entity\App.kt
// App entity

package com.kairos.hades.app.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table("apps")
data class App(
    @Id val id: UUID = UUID.randomUUID(),
    val organizationId: UUID,
    val name: String,
    val displayName: String,
    val description: String?,
    val appType: AppType,
    val domain: String? = null,
    val isActive: Boolean = true,
    val createdBy: UUID,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
