package com.mindjourney.core.logger.helper

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlin.coroutines.ContinuationInterceptor

/**
 * Logs the current coroutine dispatcher to the console.
 *
 * Useful for debugging coroutine context issues.
 */
suspend fun logCurrentDispatcher(prefix: String = "") {
    when (val dispatcher = currentCoroutineContext()[ContinuationInterceptor]) {
        Dispatchers.Main.immediate -> println("${prefix}: 🔵 Running on Main.immediate")
        Dispatchers.Main -> println("${prefix}: 🔵 Running on Main")
        Dispatchers.IO -> println("${prefix}: 🟢 Running on IO")
        Dispatchers.Default -> println("${prefix}: 🟠 Running on Default")
        else -> println("${prefix}: ⚪ Unknown dispatcher: $dispatcher")
    }
}
