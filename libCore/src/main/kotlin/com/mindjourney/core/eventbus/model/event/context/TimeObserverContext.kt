package com.mindjourney.core.eventbus.model.event.context

import com.mindjourney.core.eventbus.model.event.ObserverEvent
import com.mindjourney.core.eventbus.model.event.context.TimeObserverPolicy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * Configuration for a TimeObserver responsible for generating ObserverEvents
 * according to a declarative time-based policy.
 *
 * This context specifies:
 *  - which ObserverEvent type will be emitted
 *  - the [TimeObserverPolicy] controlling when and how often emission occurs
 *
 * The context itself performs no evaluation and contains no scheduling logic.
 * It is consumed by ObserverInitializers, which activate the TimeObserver
 * during the appropriate lifecycle phase.
 */
data class TimeObserverContext(
    /** Declarative time policy controlling the observer's emission schedule. */
    val policy: TimeObserverPolicy,

    /** The ObserverEvent produced by this time-based observer. */
    override val event: ObserverEvent,

    override val finishFlow: Flow<Unit> = emptyFlow()
) : ObserverContext()
