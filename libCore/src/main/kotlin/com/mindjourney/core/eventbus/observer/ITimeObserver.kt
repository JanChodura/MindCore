package com.mindjourney.core.eventbus.observer

import com.mindjourney.core.eventbus.model.event.context.TimeObserverContext
import com.mindjourney.core.eventbus.service.IEventManager

/**
 * Contract for observers that emit time-based ObserverEvents according to
 * a TimeObserverContext.
 *
 * Implementations must:
 *  - generate events at intervals defined by the context's policy
 *  - forward those events to the EventManager
 *  - stop when finishFlow emits a termination signal
 *
 * No business logic or decision-making belongs here.
 */
interface ITimeObserver {

    /** Starts emitting time-based events according to the given context. */
    fun start(eventManager: IEventManager, context: TimeObserverContext)
}
