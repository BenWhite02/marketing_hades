// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\app\entity\AppPermission.kt
// App-specific permission enum

package com.kairos.hades.app.entity

enum class AppPermission {
    // Customer Management
    CUSTOMER_VIEW, CUSTOMER_CREATE, CUSTOMER_UPDATE, CUSTOMER_DELETE,
    CUSTOMER_EXPORT, CUSTOMER_IMPORT,
    
    // Channel Management
    CHANNEL_VIEW, CHANNEL_CREATE, CHANNEL_UPDATE, CHANNEL_DELETE,
    CHANNEL_CONFIGURE, CHANNEL_TEST,
    
    // Campaign Management
    CAMPAIGN_VIEW, CAMPAIGN_CREATE, CAMPAIGN_UPDATE, CAMPAIGN_DELETE,
    CAMPAIGN_EXECUTE, CAMPAIGN_PAUSE,
    
    // Moment & Experience
    MOMENT_VIEW, MOMENT_CREATE, MOMENT_UPDATE, MOMENT_DELETE,
    EXPERIENCE_VIEW, EXPERIENCE_CREATE, EXPERIENCE_UPDATE, EXPERIENCE_DELETE,
    
    // Analytics
    ANALYTICS_VIEW, ANALYTICS_EXPORT, ANALYTICS_CREATE_REPORTS,
    
    // Settings
    APP_SETTINGS_VIEW, APP_SETTINGS_UPDATE, USER_MANAGEMENT,
    
    // API Access
    API_ACCESS, WEBHOOK_CONFIGURE
}
