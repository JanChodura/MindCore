package com.mindjourney.core.observer.trigger.model

import java.time.LocalTime

/**
 * Represents an informational event emitted during time-driven trigger orchestration.
 *
 * Triggers themselves do not care about tick structure —
 * they are invoked whenever an external driver decides. However, polling orchestration
 * can still emit `Tick` objects for:
 *  - logging
 *  - debugging
 *  - profiling trigger execution timing
 *  - test verification of polling behavior
 *
 * Each Tick carries lightweight context:
 *  - which trigger or job produced it
 *  - the cycle index within the polling sequence
 *  - a timestamp for diagnostics
 *  - an `isFinal` flag indicating the end of the sequence
 *
 * Although consumer logic may ignore Tick contents, the type exists to provide
 * richer observability and traceability than plain timestamps or raw unit signals.
 *
 * Tick instances are created and evolved by [com.mindjourney.core.observer.trigger.util.TickFactory].
 */
data class Tick(
    val jobDescription: TriggerDescription,
    val cycle: Int = 0,
    val timestamp: LocalTime,
    val isFinal: Boolean = false
)
