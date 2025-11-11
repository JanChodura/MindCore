package com.mindjourney.core.observer.trigger.model

/**
 * Represents a polling-based trigger which executes periodically
 * according to a defined interval and number of cycles.
 */
interface PollingTrigger : IAppTrigger {
    /**
     * Starts observing tick emissions from [com.mindjourney.core.observer.trigger.TriggerPoll]
     * and triggers execution accordingly.
     *
     * @param scope The coroutine scope used for observing.
     * @param description Name, source of the trigger for logging/debugging.
     * @param triggerPoll The poll configuration instance.
     * @param onResult Callback for publishing [TriggerResult] events.
     */
    fun startPollingObservation(
        scope: kotlinx.coroutines.CoroutineScope,
        description: TriggerDescription,
        triggerPoll: com.mindjourney.core.observer.trigger.TriggerPoll,
        onResult: (TriggerResult) -> Unit
    )
}
