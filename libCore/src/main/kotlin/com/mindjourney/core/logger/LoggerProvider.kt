package com.mindjourney.core.logger

import com.mindjourney.core.util.logging.ILogger
import com.mindjourney.core.util.logging.TestLogger

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
