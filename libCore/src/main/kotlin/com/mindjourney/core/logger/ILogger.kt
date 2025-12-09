package com.mindjourney.core.logger

import com.mindjourney.core.logger.service.LogDebugSwitch

/**
 * An interface for different logging implementations.
 * It allows switching between different loggers (e.g., AppLogger, TestLogger) based on the environment.
 */
interface ILogger {

    /**
     * Logs a debug message.
     * @param tag The tag identifying the log source.
     * @param message The message to be logged.
     * @param switch (Optional) A switch to really log or not. Useful for developing purposes. Default is OFF.
     */
    fun d(tag: String, message: String, switch: LogDebugSwitch = LogDebugSwitch.OFF)

    /**
     * Logs a warning message.
     * @param tag The tag identifying the log source.
     * @param message The warning message.
     * @param throwable (Optional) The associated throwable.
     */
    fun w(tag: String, message: String, throwable: Throwable? = null)

    /**
     * Logs an error message.
     * @param tag The tag identifying the log source.
     * @param message The error message.
     * @param throwable (Optional) The associated throwable.
     */
    fun e(tag: String, message: String, throwable: Throwable? = null)
}
