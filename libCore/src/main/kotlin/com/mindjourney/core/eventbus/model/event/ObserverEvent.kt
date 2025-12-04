package com.mindjourney.core.eventbus.model.event

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

/**
 * Represents the standardized set of events emitted by observers in the system.
 *
 * ObserverEvent is the only event type that flows from EventSources
 * (FlowObserver, TimeObserver, etc.) into the EventManager, where it is then
 * dispatched to appropriate triggers based on TriggerContext bindings.
 *
 * Each event carries system-level information only — no business logic,
 * no filtering, and no interpretation. Triggers are responsible for domain
 * evaluation of these events.
 */
sealed class ObserverEvent {

    /**
     * Emitted by FlowObserver when the observed Flow<T> produces a new value.
     * Carries the emitted value so triggers can react meaningfully.
     */
    data class FlowChanged<T>(
        val value: T
    ) : ObserverEvent()

    /**
     * Rich time-based system event carrying exact timestamp information.
     *
     * Emitted by TimeObserver according to a declarative TimeObserverPolicy.
     * Contains enough metadata for triggers to evaluate:
     *  - daily or weekly routines (e.g., weekday morning start)
     *  - time-of-day–based actions
     *  - scheduled domain workflows
     *
     * This event is purely informational. All interpretation — such as
     * “is this 07:00 on a weekday?” — belongs exclusively to triggers.
     */
    data class TimeTick(
        val date: LocalDate,
        val time: LocalTime,
        val dayOfWeek: DayOfWeek
    ) : ObserverEvent()
}
