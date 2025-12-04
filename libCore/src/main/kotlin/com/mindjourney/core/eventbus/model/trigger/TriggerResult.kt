package com.mindjourney.core.eventbus.model.trigger

import com.mindjourney.core.eventbus.model.event.ObserverEvent
import com.mindjourney.core.eventbus.model.trigger.result.TriggerResultType
import java.time.Instant

/**
 * A full result produced by the EventManager after invoking a trigger.
 *
 * Contains:
 * - [type] outcome of the trigger
 * - [description] identifying metadata
 * - [triggeredBy] event that activated the trigger
 * - [timestamp] when the result was created
 * - [metadata] optional diagnostic information
 */
data class TriggerResult(
    val type: TriggerResultType,
    val description: TriggerDescription,
    val triggeredBy: ObserverEvent,
    val timestamp: Instant = Instant.now(),
    val metadata: Map<String, Any?> = emptyMap()
)
