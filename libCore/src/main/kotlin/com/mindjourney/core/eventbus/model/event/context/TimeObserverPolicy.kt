package com.mindjourney.core.eventbus.model.event.context

import java.time.LocalTime

/**
 * Configuration describing when and how a TimeObserver should emit time-based events.
 *
 * This policy defines:
 *  - a daily time window ([start] → [end]) in which events may be emitted
 *  - the interval in minutes between event emissions
 *
 * The policy contains no logic; it is a declarative description used by the
 * TimeObserver to schedule ObserverEvent emissions.
 */
data class TimeObserverPolicy(
    /** Inclusive start of the active time window (e.g., 04:00). */
    val start: LocalTime,

    /** Exclusive end of the active time window (e.g., 06:00). */
    val end: LocalTime,

    /** Interval in minutes between emitted time events within the active window. */
    val intervalMinutes: Long
)
