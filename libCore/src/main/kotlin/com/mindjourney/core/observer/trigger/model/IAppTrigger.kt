package com.mindjourney.core.observer.trigger.model

import kotlinx.coroutines.flow.StateFlow

/**
 * Base interface representing an application trigger.
 * A trigger is a unit that reacts to app lifecycle or data changes,
 * either periodically (polling-based) or reactively (event-based).
 */
interface IAppTrigger {

    /**
     * Evaluates whether the trigger’s conditions are currently met.
     * Should be lightweight and side-effect free unless the trigger fires.
     *
     * @return [TriggerResult] indicating the outcome of the trigger evaluation.
     */
    suspend fun tryExecute(): TriggerResult

    /** Emits true when the trigger is ready to listen events. */
    val isReady: StateFlow<Boolean>

    var description: TriggerDescription
}
