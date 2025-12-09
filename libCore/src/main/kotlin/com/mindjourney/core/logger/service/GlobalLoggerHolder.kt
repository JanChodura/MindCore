package com.mindjourney.core.logger.service

import com.mindjourney.core.logger.ILogger
import com.mindjourney.core.logger.TestLogger

/**
 * A global logger holder to provide a logger instance throughout the app.
 * It can be initialized with a custom logger or defaults to TestLogger.
 */
object GlobalLoggerHolder {
    @Volatile
    private var logger: ILogger? = null

    fun init(logger: ILogger) {
        this.logger = logger
    }

    fun get(): ILogger {
        return logger ?: TestLogger()
    }
}
