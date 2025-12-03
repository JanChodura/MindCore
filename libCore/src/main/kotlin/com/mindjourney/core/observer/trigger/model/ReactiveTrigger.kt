package com.mindjourney.core.observer.trigger.model

import com.mindjourney.core.util.logging.injectedLogger
import com.mindjourney.core.util.logging.off
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Base implementation of [IReactiveTrigger] backed by a [Flow] of source events.
 *
 * Each emission from [reactiveFlow] represents a “now evaluate” signal:
 * the trigger runs its internal logic (`tryExecute`) and forwards any
 * non-empty [TriggerResult] to consumers.
 *
 * This class encapsulates the common boilerplate for:
 *  - subscribing to a reactive stream
 *  - transforming events into trigger evaluations
 *  - maintaining readiness state
 *  - logging execution diagnostics
 *
 * Subclasses only implement `tryExecute`, meaning their responsibility
 * is to *decide what should happen*, not *when it happens*.
 */
abstract class ReactiveTrigger<T>(
    override val sourceFlow: Flow<T>,
) : FlowBasedTrigger<T>(), IReactiveTrigger {

    private val log = injectedLogger<ReactiveTrigger<T>>(off)

    /**
     * Internal readiness state exposed via [isReady] once the trigger
     * begins actively reacting to its data source.
     *
     * This enables orchestration layers or UI state to reflect
     * when the trigger has been initialized and is operational.
     */
    private val _isReady = MutableStateFlow(false)
    override val isReady: StateFlow<Boolean> get() = _isReady

    /**
     * Subscribes to the given flow and processes each emission
     * as a trigger evaluation request.
     *
     * Readiness becomes `true` upon receiving the first upstream event.
     *
     * @param scope Coroutine scope used to collect the flow.
     * @param description Updated metadata for diagnostics / logging.
     * @param onResult Callback invoked when `tryExecute` produces a meaningful result.
     */
    override fun startReactiveFlow(
        scope: CoroutineScope,
        description: TriggerDescription,
        onResult: (IAppTrigger, TriggerResultType) -> Unit
    ) {
        this.description = description
        log.d("Starting flow for: $description")
        val activeTrigger = this
        scope.launch {
            sourceFlow.collectLatest {
                log.d("Detected change in $description: $it")

                _isReady.value = true

                val result = tryExecute()
                log.d("$description produced result: $result")
                onResult(activeTrigger, result.type)
            }
        }
    }
}
