package com.mindjourney.core.logger.helper

import com.mindjourney.core.logger.service.GlobalLoggerHolder
import com.mindjourney.core.logger.service.LogDebugSwitch
import com.mindjourney.core.logger.service.off

inline fun <reified T> injectedLogger(
    logDebugSwitch: LogDebugSwitch = off
): LoggerDelegate {
    val rightLogger = GlobalLoggerHolder.get()
    return LoggerDelegate(rightLogger, T::class.simpleName ?: "Unknown", logDebugSwitch)
}
