package com.mindjourney.core.util.logging

import com.mindjourney.core.logger.LoggerProvider
import com.mindjourney.core.util.logging.processoring.ILoggingWrapper
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.isAccessible

/**
 * A delegate that logs method invocations before calling the actual method.
 * This delegate should be used with classes implementing [ILoggingWrapper].
 * This interface is inserted by the annotation processor
 *
 */

class LoggingDelegate<T : ILoggingWrapper>(
    private val instance: T, private val logger: ILogger = LoggerProvider.get()
) : ILoggingWrapper by instance {

    /**
     * Logs the invocation of a method and then calls it on the actual instance.
     *
     * @param method The method to be invoked.
     * @param args The arguments passed to the method.
     */
    fun invoke(method: KFunction<*>, vararg args: Any?) {
        logger.d("HiltLogger", "Method called: ${method.name} with args: ${args.joinToString()}")
        method.isAccessible = true
        method.call(instance, *args)
    }
}
