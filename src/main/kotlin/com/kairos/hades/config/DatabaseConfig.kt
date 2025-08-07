// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\config\DatabaseConfig.kt
// Database configuration with proper R2DBC setup

package com.kairos.hades.config

import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.ConnectionFactoryOptions.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.transaction.ReactiveTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableR2dbcRepositories(basePackages = ["com.kairos.hades"])
@EnableTransactionManagement
class DatabaseConfig(
    @Value("\${spring.r2dbc.host:localhost}") private val host: String,
    @Value("\${spring.r2dbc.port:5432}") private val port: Int,
    @Value("\${spring.r2dbc.database:kairos_db}") private val database: String,
    @Value("\${spring.r2dbc.username:kairos_user}") private val username: String,
    @Value("\${spring.r2dbc.password:kairos_pass}") private val password: String
) : AbstractR2dbcConfiguration() {
    
    @Bean
    override fun connectionFactory(): ConnectionFactory {
        return io.r2dbc.spi.ConnectionFactories.get(
            ConnectionFactoryOptions.builder()
                .option(DRIVER, "postgresql")
                .option(HOST, host)
                .option(PORT, port)
                .option(USER, username)
                .option(PASSWORD, password)
                .option(DATABASE, database)
                .build()
        )
    }
    
    @Bean
    fun transactionManager(connectionFactory: ConnectionFactory): ReactiveTransactionManager {
        return R2dbcTransactionManager(connectionFactory)
    }
}