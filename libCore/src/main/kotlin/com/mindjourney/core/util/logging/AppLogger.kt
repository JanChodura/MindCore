package com.mindjourney.core.util.logging

import android.util.Log
import com.mindjourney.core.logger.ILoggerConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppLogger @Inject constructor(private val config: ILoggerConfig) : ILogger {
    override fun d(tag: String, message: String, switch: LogDebugSwitch) {
        if (config.isDebug && switch.isLive()) {
            Log.d(tag, "MJ:$message")
        }
    }

    override fun w(tag: String, message: String, throwable: Throwable?) {
        if (config.isDebug) {
            Log.w(tag, "MJ:$message", throwable)
        }
    }

    override fun e(tag: String, message: String, throwable: Throwable?) {
        if (config.isDebug) {
            Log.e(tag, "MJ:$message", throwable)
        }
    }
}
