package com.mindjourney.core.util.logging

inline fun <reified T> injectedLogger(
    logDebugSwitch: LogDebugSwitch = off
): LoggerDelegate {
    val rightLogger = GlobalLoggerHolder.get()
    return LoggerDelegate(rightLogger, T::class.simpleName ?: "Unknown", logDebugSwitch)
}
