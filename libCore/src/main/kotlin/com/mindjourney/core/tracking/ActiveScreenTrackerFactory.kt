package com.mindjourney.core.tracking

import com.mindjourney.core.logger.LoggerProvider
import com.mindjourney.core.util.logging.ILogger
import com.mindjourney.core.util.logging.LogDebugSwitch
import com.mindjourney.core.util.logging.injectedLogger
import com.mindjourney.core.util.logging.off

/**
 * Factory for creating instances of [ScreenTracker]. When app is started or in preview/test.
 */
object ActiveScreenTrackerFactory {
    /**
     * Creates an empty/no-op instance for preview or test environments.
     */
    fun empty(): ScreenTracker {
        val log = injectedLogger<ScreenTracker>(LoggerProvider.get(), off)
        val emptyTAG = "EmptyActiveScreenTracker"
        return ScreenTracker(
            logger = object : ILogger {
                override fun d(tag: String, message: String, switch: LogDebugSwitch) {
                    log.d("DEBUG: $tag - $message")
                }

                override fun w(tag: String, message: String, throwable: Throwable?) {
                    log.w("WARN: $tag - $message")
                }

                override fun e(tag: String, message: String, throwable: Throwable?) {
                    log.e("ERROR: $tag - $message")
                }
            }
        )
    }
}
