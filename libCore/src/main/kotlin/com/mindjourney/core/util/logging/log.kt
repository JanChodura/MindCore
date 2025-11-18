package com.mindjourney.core.util.logging

class LoggerDelegate(
    private val logger: ILogger,
    private val tag: String,
    private val logDebugSwitch: LogDebugSwitch
) {

    fun d(message: String, switch: LogDebugSwitch = logDebugSwitch) =
        logger.d(tag, message, switch)

    fun w(message: String, throwable: Throwable? = null) =
        logger.w(tag, message, throwable)

    fun e(message: String, throwable: Throwable? = null) =
        logger.e(tag, message, throwable)

}
