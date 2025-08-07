// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\common\repository\BaseCoroutineRepository.kt
// Base repository interface using Kotlin Coroutines for reactive operations

package com.kairos.hades.common.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.NoRepositoryBean
import java.util.*

@NoRepositoryBean
interface BaseCoroutineRepository<T, ID> : CoroutineCrudRepository<T, ID> {
    
    // Override findAll to return Flow (proper reactive type)
    override fun findAll(): Flow<T>
    
    // Override existsById to avoid hiding supertype member
    override suspend fun existsById(id: ID): Boolean
    
    // Use the built-in count() method from CoroutineCrudRepository
    // Remove the problematic countAll() method as count() already exists
}