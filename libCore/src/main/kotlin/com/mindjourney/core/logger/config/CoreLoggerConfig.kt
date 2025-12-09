package com.mindjourney.core.logger.config

import com.mindjourney.core.logger.AppLogger
import com.mindjourney.core.logger.ILogger
import com.mindjourney.core.logger.service.GlobalLoggerHolder
import com.mindjourney.core.logger.service.LoggingDelegate
import com.mindjourney.core.logger.processoring.ILoggingWrapper

/** Toggle for enabling or disabling logging instrumentation. */
const val ENABLE_LOGGING: Boolean = true

/**
 * Core-level logging configuration and factory.
 *
 * This object defines how logging is configured inside the core module.
 * It contains no framework or platform-specific dependencies and does not
 * rely on any dependency injection mechanism. Platform modules (such as
 * Android) can wrap or override this configuration as needed.
 *
 * Responsibilities:
 *  - Provide a default [ILoggerConfig] usable by the entire core module.
 *  - Provide optional wrapping of [ILoggingWrapper] inside a delegating
 *    logger (e.g., for instrumentation or diagnostics).
 *  - Initialize and expose a core-level [com.mindjourney.core.logger.ILogger] implementation.
 *
 * External DI frameworks (Hilt, Dagger, Koin, etc.) may bind these
 * functions as providers, but this object itself is fully decoupled
 * from any DI system.
 */
open class CoreLoggerConfig {

    /**
     * Creates the default core logger configuration.
     *
     * Framework modules may override this behavior by wrapping the returned
     * [ILoggerConfig] in their dependency injection setup.
     */
    open fun loggerConfig(
        tag: String = "CoreLoggerTag",
        isDebug: Boolean = false
    ): ILoggerConfig = object : ILoggerConfig {
        override val tag: String = tag
        override val isDebug: Boolean = isDebug
    }

    /**
     * Optionally wraps an instance of [ILoggingWrapper] into a delegating
     * logger. Core does not impose any specific behavior; platform modules
     * may supply their own [com.mindjourney.core.logger.ILogger] and delegate type.
     *
     * If logging is disabled, the wrapper is returned unchanged.
     */
    open fun wrap(
        instance: ILoggingWrapper,
        logger: ILogger
    ): ILoggingWrapper {
        return if (ENABLE_LOGGING) {
            LoggingDelegate(instance, logger)
        } else instance
    }

    /**
     * Initializes and exposes a configured core logger. The implementation
     * may be replaced by platform modules, for example to attach Android-
     * specific sinks or formatting.
     *
     * Calling code may rely on [GlobalLoggerHolder] to obtain the active
     * logger instance.
     */
    open fun initLogger(appLogger: AppLogger): ILogger {
        GlobalLoggerHolder.init(appLogger)
        return appLogger
    }
}
