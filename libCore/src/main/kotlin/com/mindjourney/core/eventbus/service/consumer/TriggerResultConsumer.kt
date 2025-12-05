package com.mindjourney.core.eventbus.service.consumer

import com.mindjourney.core.eventbus.model.trigger.TriggerResult

/**
 * Receives TriggerResult objects produced by the EventManager.
 *
 * Implementations may:
 *  - log results
 *  - forward them to higher application layers
 *  - accumulate them for testing
 */
fun interface TriggerResultConsumer {

    /** Called whenever a trigger has produced a TriggerResult. */
    fun consume(result: TriggerResult)
}
