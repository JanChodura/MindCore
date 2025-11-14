package com.mindjourney.core.observer.trigger

import com.mindjourney.core.logger.LoggerProvider
import com.mindjourney.core.observer.trigger.model.ReactiveTrigger
import com.mindjourney.core.observer.trigger.model.TriggerDescription
import com.mindjourney.core.observer.trigger.model.TriggerResult
import com.mindjourney.core.util.logging.ILogger
import com.mindjourney.core.util.logging.injectedLogger
import com.mindjourney.core.util.logging.off
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Base implementation of [ReactiveTrigger].
 * Provides helper to observe any [Flow] of data changes
 * and call [tryExecute] reactively when changes occur.
 */
abstract class ReactiveTriggerImpl<T>(
    private val reactiveFlow: Flow<T>,
    override var description: TriggerDescription
) : ReactiveTrigger {

    private val log = injectedLogger<ReactiveTrigger>(LoggerProvider.get(), off)
    private val _isReady = MutableStateFlow(false)
    override val isReady: StateFlow<Boolean> get() = _isReady

    override fun startReactiveFlow(
        scope: CoroutineScope,
        description: TriggerDescription,
        onResult: (TriggerResult) -> Unit
    ) {
        this.description = description
        log.d("Starting flow for: $description")
        scope.launch {
            reactiveFlow.collectLatest {
                log.d("Detected change in:$description in flow: $it")
                _isReady.value = true
                val result = tryExecute()
                if (result !is TriggerResult.None) {
                    log.d("$description produced result: $result")
                    onResult(result)
                }
            }
        }
    }

}
