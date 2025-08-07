// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\security\jwt\JwtAuthenticationToken.kt
// JWT authentication token - single clean implementation

package com.kairos.hades.security.jwt

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import java.util.*

class JwtAuthenticationToken(
    private val principal: UUID,
    private val authorities: Collection<GrantedAuthority>,
    private val isAppScopedFlag: Boolean = false,
    private val appIdValue: UUID? = null,
    private val organizationIdValue: UUID? = null,
    private val globalRoleValue: String? = null,
    private val appRoleValue: String? = null,
    private val permissionsValue: Set<String> = emptySet()
) : AbstractAuthenticationToken(authorities) {
    
    init {
        isAuthenticated = true
        details = mapOf(
            "isAppScoped" to isAppScopedFlag,
            "appId" to appIdValue,
            "organizationId" to organizationIdValue,
            "globalRole" to globalRoleValue,
            "appRole" to appRoleValue,
            "permissions" to permissionsValue
        )
    }
    
    override fun getCredentials(): Any? = null
    override fun getPrincipal(): UUID = principal
    
    // Custom getter methods to avoid conflicts
    fun getUserId(): UUID = principal
    fun getTokenAppId(): UUID? = appIdValue
    fun getTokenOrganizationId(): UUID? = organizationIdValue
    fun getTokenGlobalRole(): String? = globalRoleValue
    fun getTokenAppRole(): String? = appRoleValue
    fun getTokenPermissions(): Set<String> = permissionsValue
    fun isAppScoped(): Boolean = isAppScopedFlag
    
    fun hasPermission(permission: String): Boolean = permissionsValue.contains(permission)
    
    fun hasAnyPermission(vararg permissions: String): Boolean = 
        permissions.any { permissionsValue.contains(it) }
    
    fun hasAllPermissions(vararg permissions: String): Boolean = 
        permissions.all { permissionsValue.contains(it) }
}
