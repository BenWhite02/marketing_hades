// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\HadesApplication.kt
// Main Application Entry Point for Hades Backend

package com.kairos.hades

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableR2dbcAuditing
@EnableTransactionManagement
class HadesApplication

fun main(args: Array<String>) {
    runApplication<HadesApplication>(*args)
}
