package com.mindjourney.core.di

import com.mindjourney.common.BuildConfig
import com.mindjourney.core.logger.ILoggerConfig
import com.mindjourney.core.util.logging.AppLogger
import com.mindjourney.core.util.logging.ILogger
import com.mindjourney.core.util.logging.LoggingDelegate
import com.mindjourney.core.util.logging.processoring.ILoggingWrapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * A Dagger module providing different loggers and logging delegates.
 */
@Module
@InstallIn(SingletonComponent::class)
object LoggerModule {

    const val ENABLE_LOGGING = true

    @Provides
    @Singleton
    fun provideLoggerConfig(): ILoggerConfig = object : ILoggerConfig {
        override val tag: String = "MindriseLoggerTag"
        override val isDebug: Boolean = BuildConfig.DEBUG
    }

    /**
     * Provides the default application logger.
     *
     * @return An instance of [com.mindjourney.core.util.logging.ILogger] for logging messages.
     */
    @Provides
    @Singleton
    fun provideAppLogger(loggerConfig: ILoggerConfig): ILogger {
        return AppLogger(loggerConfig)
    }

    /**
     * Provides a logging delegate that wraps instances implementing [ILoggingWrapper].
     * If logging is enabled, it returns an instance wrapped in [LoggingDelegate].
     * Otherwise, it returns the original instance.
     *
     * @param instance The instance to be wrapped.
     * @return A logging delegate if enabled, otherwise the original instance.
     */
    @Provides
    @Singleton
    fun provideLoggingDelegate(instance: ILoggingWrapper, logger: ILogger): ILoggingWrapper {
        return if (ENABLE_LOGGING) LoggingDelegate(instance, logger) else instance
    }
}
