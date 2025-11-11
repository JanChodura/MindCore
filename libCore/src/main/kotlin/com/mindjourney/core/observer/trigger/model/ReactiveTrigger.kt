package com.mindjourney.core.observer.trigger.model

/**
* Represents a reactive trigger which emits results based on
* reactive data flows (e.g., Flow, LiveData, external event streams).
*/
interface ReactiveTrigger : IAppTrigger {

    /**
     * Starts a reactive flow observation.
     * Implementations should collect from their respective data sources
     * and call [onResult] when a condition is met.
     *
     * @param scope The coroutine scope used for reactive observation.
     * @param description Name of the trigger for logging/debugging.
     * @param onResult Callback for publishing [TriggerResult] events.
     */
    fun startReactiveFlow(
        scope: kotlinx.coroutines.CoroutineScope,
        description: TriggerDescription,
        onResult: (TriggerResult) -> Unit
    )
}
