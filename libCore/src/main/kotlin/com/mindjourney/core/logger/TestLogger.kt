package com.mindjourney.core.logger

import com.mindjourney.core.logger.service.LogDebugSwitch
import java.util.logging.ConsoleHandler
import java.util.logging.Formatter
import java.util.logging.Level
import java.util.logging.LogRecord
import java.util.logging.Logger

class TestLogger : ILogger {
    private val logger: Logger = Logger.getLogger(TestLogger::class.java.name)

    init {
        val consoleHandler = ConsoleHandler().apply {
            level = Level.ALL
            formatter = object : Formatter() {
                override fun format(record: LogRecord): String {
                    //${record.level.name}: - can be added to log level but there is no need
                    return "${record.message}\n" //one line per log
                }
            }
        }
        logger.apply {
            useParentHandlers = false // don't pass logs to parent - duplication
            level = Level.ALL
            handlers.forEach { removeHandler(it) } // remove default handler
            addHandler(consoleHandler)
        }

        logger.level = Level.ALL
        consoleHandler.level = Level.ALL
    }

    override fun d(tag: String, message: String, switch: LogDebugSwitch) {
        logger.fine("DEBUG: $tag - $message")
    }

    override fun w(tag: String, message: String, throwable: Throwable?) {
        logger.warning("WARNING: $tag - $message")
        throwable?.let { logger.warning("Throwable: ${it.message}") }
    }

    override fun e(tag: String, message: String, throwable: Throwable?) {
        logger.severe("ERROR: $tag - $message")
        throwable?.let { logger.severe("Throwable: ${it.message}") }
    }
}
