package com.kairos.hades.app.controller

import com.kairos.hades.app.dto.CreateAppRequest
import com.kairos.hades.app.dto.AppDto
import com.kairos.hades.app.service.AppService
import com.kairos.hades.security.jwt.JwtService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/apps")
class AppController(
    private val appService: AppService,
    private val jwtService: JwtService
) {
    
    @GetMapping
    suspend fun getApps(
        @RequestHeader("Authorization") authorization: String
    ): ResponseEntity<List<AppDto>> {
        val userId = jwtService.extractUserId(authorization)
        val apps = appService.getUserAccessibleApps(userId)
        return ResponseEntity.ok(apps)
    }
    
    @PostMapping
    suspend fun createApp(
        @RequestBody request: CreateAppRequest,
        @RequestHeader("Authorization") authorization: String
    ): ResponseEntity<AppDto> {
        val userId = jwtService.extractUserId(authorization)
        val app = appService.createApp(request, userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(app)
    }
    
    @GetMapping("/{id}")
    suspend fun getApp(
        @PathVariable id: UUID,
        @RequestHeader("Authorization") authorization: String
    ): ResponseEntity<AppDto> {
        val userId = jwtService.extractUserId(authorization)
        val app = appService.getAppById(id, userId)
        return ResponseEntity.ok(app)
    }
}
