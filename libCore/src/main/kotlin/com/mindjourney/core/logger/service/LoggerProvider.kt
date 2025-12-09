package com.mindjourney.core.logger.service

import com.mindjourney.core.logger.ILogger
import com.mindjourney.core.logger.TestLogger

object LoggerProvider {

    private var logger: ILogger? = null

    /**
     * Initialize logger from DI.
     * This should be called in Application class.
     */
    fun init(loggerFromHilt: ILogger) {
        logger = loggerFromHilt
    }

    /**
     * Use for testing without DI.
     */
    fun initTestLogger() {
        logger = TestLogger()
    }

    fun get(): ILogger {
        if (logger  == null) {
            error("LoggerProvider not initialized. Call LoggerProvider.init() in Application.")
        }

        return logger!!
    }
}
