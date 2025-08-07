// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\security\jwt\JwtService.kt
// JWT Service compatible with JJWT 0.11.x

package com.kairos.hades.security.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService(
    @Value("\${kairos.security.jwt.secret}") private val jwtSecret: String,
    @Value("\${kairos.security.jwt.expiration}") private val jwtExpiration: Long,
    @Value("\${kairos.security.jwt.refresh-expiration}") private val refreshExpiration: Long
) {
    
    private val secretKey: SecretKey = Keys.hmacShaKeyFor(jwtSecret.toByteArray())
    
    // Generate initial token (no app context)
    fun generateToken(
        userId: UUID,
        email: String,
        globalRole: String,
        organizationId: UUID
    ): String {
        val now = Date()
        val expiryDate = Date(now.time + jwtExpiration * 1000)
        
        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .claim("email", email)
            .claim("globalRole", globalRole)
            .claim("organizationId", organizationId.toString())
            .claim("tokenType", "GLOBAL")
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .compact()
    }
    
    // Generate app-scoped token
    fun generateAppScopedToken(
        userId: UUID,
        appId: UUID,
        role: String,
        permissions: Set<String>
    ): String {
        val now = Date()
        val expiryDate = Date(now.time + jwtExpiration * 1000)
        
        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .claim("appId", appId.toString())
            .claim("role", role)
            .claim("permissions", permissions.toList())
            .claim("tokenType", "APP_SCOPED")
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .compact()
    }
    
    // Generate refresh token
    fun generateRefreshToken(userId: UUID): String {
        val now = Date()
        val expiryDate = Date(now.time + refreshExpiration * 1000)
        
        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .claim("tokenType", "REFRESH")
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .compact()
    }
    
    // Extract user ID from any token type
    fun extractUserId(authorization: String): UUID {
        val token = extractToken(authorization)
        val claims = validateAndExtractClaims(token)
        return UUID.fromString(claims.subject)
    }
    
    // Extract app ID from app-scoped token
    fun extractAppId(authorization: String): UUID? {
        val token = extractToken(authorization)
        val claims = validateAndExtractClaims(token)
        val appIdClaim = claims["appId"] as? String
        return appIdClaim?.let { UUID.fromString(it) }
    }
    
    // Extract user permissions from app-scoped token
    fun extractPermissions(authorization: String): Set<String> {
        val token = extractToken(authorization)
        val claims = validateAndExtractClaims(token)
        val permissionsClaim = claims["permissions"] as? List<*>
        return permissionsClaim?.mapNotNull { it.toString() }?.toSet() ?: emptySet()
    }
    
    // Extract role from token
    fun extractRole(authorization: String): String? {
        val token = extractToken(authorization)
        val claims = validateAndExtractClaims(token)
        return claims["role"] as? String
    }
    
    // Extract global role from token
    fun extractGlobalRole(authorization: String): String? {
        val token = extractToken(authorization)
        val claims = validateAndExtractClaims(token)
        return claims["globalRole"] as? String
    }
    
    // Extract organization ID from token
    fun extractOrganizationId(authorization: String): UUID? {
        val token = extractToken(authorization)
        val claims = validateAndExtractClaims(token)
        val orgIdClaim = claims["organizationId"] as? String
        return orgIdClaim?.let { UUID.fromString(it) }
    }
    
    // Validate token and get claims (0.11.x compatible)
    fun validateAndExtractClaims(token: String): Claims {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .body
        } catch (ex: Exception) {
            throw RuntimeException("Invalid or expired token: ${ex.message}", ex)
        }
    }
    
    // Validate refresh token specifically
    fun validateRefreshToken(refreshToken: String): UUID {
        val claims = validateAndExtractClaims(refreshToken)
        val tokenType = claims["tokenType"] as? String
        
        if (tokenType != "REFRESH") {
            throw RuntimeException("Invalid refresh token")
        }
        
        return UUID.fromString(claims.subject)
    }
    
    // Check if token is app-scoped
    fun isAppScopedToken(authorization: String): Boolean {
        return try {
            val token = extractToken(authorization)
            val claims = validateAndExtractClaims(token)
            claims["tokenType"] == "APP_SCOPED"
        } catch (e: Exception) {
            false
        }
    }
    
    // Check if token is valid (not expired)
    fun isTokenValid(authorization: String): Boolean {
        return try {
            val token = extractToken(authorization)
            val claims = validateAndExtractClaims(token)
            val expiration = claims.expiration
            expiration.after(Date())
        } catch (ex: Exception) {
            false
        }
    }
    
    // Extract token from Authorization header
    private fun extractToken(authorization: String): String {
        if (!authorization.startsWith("Bearer ")) {
            throw RuntimeException("Invalid authorization header format")
        }
        return authorization.substring(7)
    }
    
    // Get token expiration in seconds
    fun getTokenExpiration(): Long = jwtExpiration
    
    // Get refresh token expiration in seconds
    fun getRefreshTokenExpiration(): Long = refreshExpiration
}
