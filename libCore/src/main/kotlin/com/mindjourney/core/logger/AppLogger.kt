package com.mindjourney.core.logger

import com.mindjourney.core.logger.config.ILoggerConfig
import com.mindjourney.core.logger.service.LogDebugSwitch
import jakarta.inject.Inject
import javax.inject.Singleton

/**
 * Application logger implementation.
 *
 * @param config Logger configuration.
 * @param prefix Optional prefix to prepend to log messages for easier identification, short is better "MJ".
 */
@Singleton
class AppLogger @Inject constructor(private val config: ILoggerConfig, private val prefix: String = "") : ILogger {
    override fun d(tag: String, message: String, switch: LogDebugSwitch) {
        if (config.isDebug && switch.isLive()) {
            println("[DEBUG][$tag] $prefix: $message")
        }
    }

    override fun w(tag: String, message: String, throwable: Throwable?) {
        if (config.isDebug) {
            println("[WARN][$tag] $prefix: $message")
            throwable?.printStackTrace()
        }
    }

    override fun e(tag: String, message: String, throwable: Throwable?) {
        if (config.isDebug) {
            println("[ERROR][$tag] $prefix: $message")
            throwable?.printStackTrace()
        }
    }
}
