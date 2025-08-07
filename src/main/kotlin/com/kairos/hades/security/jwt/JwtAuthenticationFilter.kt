// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\security\jwt\JwtAuthenticationFilter.kt
// JWT authentication filter - single clean implementation

package com.kairos.hades.security.jwt

import org.springframework.http.HttpHeaders
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.util.*

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService
) : WebFilter {
    
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val request = exchange.request
        val authHeader = request.headers.getFirst(HttpHeaders.AUTHORIZATION)
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange)
        }
        
        return try {
            val token = authHeader.substring(7)
            
            if (!jwtService.isTokenValid(authHeader)) {
                return chain.filter(exchange)
            }
            
            val claims = jwtService.validateAndExtractClaims(token)
            val userId = UUID.fromString(claims.subject)
            
            // Determine token type
            val tokenType = claims["tokenType"] as? String
            val isAppScoped = tokenType == "APP_SCOPED"
            
            // Create authentication object
            val authentication = if (isAppScoped) {
                createAppScopedAuthentication(userId, claims)
            } else {
                createGlobalAuthentication(userId, claims)
            }
            
            // Set security context and continue
            chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
                
        } catch (ex: Exception) {
            // Invalid token - continue without authentication
            chain.filter(exchange)
        }
    }
    
    private fun createAppScopedAuthentication(userId: UUID, claims: io.jsonwebtoken.Claims): JwtAuthenticationToken {
        val appId = UUID.fromString(claims["appId"] as String)
        val role = claims["role"] as? String
        val permissions = (claims["permissions"] as? List<*>)?.mapNotNull { it.toString() }?.toSet() ?: emptySet()
        val organizationId = (claims["organizationId"] as? String)?.let { UUID.fromString(it) }
        
        val authorities = mutableListOf<SimpleGrantedAuthority>()
        role?.let { authorities.add(SimpleGrantedAuthority("ROLE_$it")) }
        permissions.forEach { permission ->
            authorities.add(SimpleGrantedAuthority("PERMISSION_$permission"))
        }
        
        return JwtAuthenticationToken(
            principal = userId,
            authorities = authorities,
            isAppScopedFlag = true,
            appIdValue = appId,
            organizationIdValue = organizationId,
            appRoleValue = role,
            permissionsValue = permissions
        )
    }
    
    private fun createGlobalAuthentication(userId: UUID, claims: io.jsonwebtoken.Claims): JwtAuthenticationToken {
        val globalRole = claims["globalRole"] as? String
        val organizationId = (claims["organizationId"] as? String)?.let { UUID.fromString(it) }
        
        val authorities = mutableListOf<SimpleGrantedAuthority>()
        globalRole?.let { authorities.add(SimpleGrantedAuthority("GLOBAL_ROLE_$it")) }
        
        return JwtAuthenticationToken(
            principal = userId,
            authorities = authorities,
            isAppScopedFlag = false,
            organizationIdValue = organizationId,
            globalRoleValue = globalRole
        )
    }
}
