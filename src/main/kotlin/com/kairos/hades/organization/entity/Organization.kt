// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\organization\entity\Organization.kt
// Organization Entity - Top-level tenant in multi-app architecture

package com.kairos.hades.organization.entity

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table("organizations")
data class Organization(
    @Id
    val id: UUID = UUID.randomUUID(),
    
    val name: String,
    val domain: String? = null,
    
    @Column("subscription_type")
    val subscriptionType: SubscriptionType = SubscriptionType.FREE,
    
    val settings: JsonNode,
    
    @Column("is_active")
    val isActive: Boolean = true,
    
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column("updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

enum class SubscriptionType {
    FREE, PROFESSIONAL, ENTERPRISE
}
