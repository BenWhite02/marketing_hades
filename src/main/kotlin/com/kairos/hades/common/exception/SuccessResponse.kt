// C:\03Marketing\Hades\src\main\kotlin\com\kairos\hades\common\exception\SuccessResponse.kt
// Success response wrapper classes

package com.kairos.hades.common.exception

import java.time.Instant

data class SuccessResponse<T>(
    val success: Boolean = true,
    val data: T,
    val meta: MetaInfo
)

data class MetaInfo(
    val timestamp: Instant,
    val requestId: String
) {
    // Additional constructor for string timestamp (backward compatibility)
    constructor(timestamp: String, requestId: String) : this(
        timestamp = Instant.parse(timestamp),
        requestId = requestId
    )
}