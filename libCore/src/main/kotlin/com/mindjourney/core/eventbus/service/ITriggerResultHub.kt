package com.mindjourney.core.eventbus.service

import com.mindjourney.core.eventbus.model.trigger.TriggerResult

/**
 * A neutral delivery hub for TriggerResult dispatching.
 *
 * This hub receives every TriggerResult produced by the EventManager and
 * forwards it to *all* registered domain consumers without performing
 * any filtering, routing, or business logic.
 *
 * Architectural purpose:
 * - Decouples EventManager from the actual list of consumers.
 * - Prevents EventManager from knowing how many consumers exist.
 * - Ensures every consumer receives all results and decides independently
 *   whether to react or ignore the event.
 *
 * Think of it as a "mail drop-off point": it throws all letters into one place,
 * and individual consumers pick only the letters addressed to them.
 */
interface ITriggerResultHub {

    /**
     * Delivers the given [result] to all registered consumers.
     * EventManager must call this method after any trigger execution.
     *
     * @param result The output of a trigger, including metadata and type.
     * @param useCaseCallback Domain-specific callback passed down unchanged.
     */
    fun dispatch(result: TriggerResult, useCaseCallback: () -> Unit)
}
