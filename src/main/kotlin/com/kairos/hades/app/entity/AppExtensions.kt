// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\app\entity\AppExtensions.kt
// App entity extensions

package com.kairos.hades.app.entity

import com.kairos.hades.app.dto.AppDto

fun App.toDto(): AppDto {
    return AppDto(
        id = this.id,
        organizationId = this.organizationId,
        name = this.name,
        displayName = this.displayName,
        description = this.description,
        appType = this.appType,
        domain = this.domain,
        isActive = this.isActive,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
