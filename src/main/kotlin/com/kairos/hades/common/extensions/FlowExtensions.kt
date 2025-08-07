// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\common\extensions\FlowExtensions.kt
// Kotlin Flow extensions for reactive operations

package com.kairos.hades.common.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList as flowToList
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.collect

/**
 * Safely convert Flow to List with proper coroutine context
 * This is the correct way to handle Flow -> List conversion
 */
suspend fun <T> Flow<T>.toList(): List<T> = this.flowToList()

/**
 * Convert Flow to List with size limit to prevent memory issues
 */
suspend fun <T> Flow<T>.toList(maxSize: Int): List<T> {
    val result = mutableListOf<T>()
    var count = 0
    this.collect { item ->
        if (count < maxSize) {
            result.add(item)
            count++
        }
    }
    return result
}

/**
 * Check if Flow has any elements
 */
suspend fun <T> Flow<T>.hasElements(): Boolean {
    return try {
        this.first()
        true
    } catch (e: NoSuchElementException) {
        false
    }
}