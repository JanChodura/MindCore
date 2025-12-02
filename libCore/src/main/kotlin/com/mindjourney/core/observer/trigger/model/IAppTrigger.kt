package com.mindjourney.core.observer.trigger.model

import kotlinx.coroutines.flow.StateFlow

/**
 * Core abstraction for any application trigger.
 *
 * A trigger represents a small, self-contained decision unit that evaluates
 * whether some condition is met — for example:
 *  - battery level state change
 *  - server sync availability
 *  - user interaction event
 *  - timed evaluation based on poll cycles
 *
 * The trigger **does not decide when it runs** — it only defines *what logic*
 * should execute when an external driver (e.g. [FlowBasedTrigger]) invokes it.
 *
 * In the new architecture:
 *  - triggers no longer manage timing, polling or subscriptions
 *  - they simply expose evaluation logic via [tryExecute]
 *  - the orchestration of “when triggers fire” is managed externally
 *
 * This keeps triggers testable, deterministic and easy to compose.
 */
interface IAppTrigger {

    /**
     * Executed whenever the trigger is asked to evaluate its condition.
     *
     * Implementations:
     *  - inspect state or external data
     *  - decide whether something meaningful happened
     *  - return either `TriggerResult.None` (no action) or a meaningful result
     *
     * This function should be lightweight and side-effect free
     * unless the trigger actually fires.
     *
     * @return a [TriggerResult] indicating whether the trigger produced an outcome.
     */
    suspend fun tryExecute(): TriggerResult

    /**
     * Exposes whether the trigger is ready to react to incoming events.
     *
     * This is useful for UI or orchestration layers that want to:
     *  - check whether prerequisite state is satisfied
     *  - avoid evaluating triggers before dependencies are set up
     *
     * The value changes via the orchestration layer (e.g. when its Flow starts collecting).
     */
    val isReady: StateFlow<Boolean>

    /**
     * Human-readable metadata identifying the trigger.
     *
     * Used for logging, debugging, analytics and diagnostics.
     */
    var description: TriggerDescription
}
