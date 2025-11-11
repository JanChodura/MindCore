package com.mindjourney.core.observer.trigger.model

import java.time.LocalTime

/**
 * Represents a single tick emitted during a trigger polling cycle.
 *
 * Used by [com.mindjourney.core.observer.trigger.TriggerPoll] to carry basic metadata about each emission —
 * including which polling job produced it, the current cycle index,
 * and the exact timestamp of emission.
 *
 * It provides clearer context for debugging, logging, and testing
 * compared to using raw timestamps alone.
 * "isFinal" indicates if this tick is the last one in a polling sequence.
 *
 * It is created and updated by [com.mindjourney.core.observer.trigger.util.TickFactory].
 */
data class Tick(
    val jobDescription: TriggerDescription,
    val cycle: Int = 0,
    val timestamp: LocalTime,
    val isFinal: Boolean = false
)
