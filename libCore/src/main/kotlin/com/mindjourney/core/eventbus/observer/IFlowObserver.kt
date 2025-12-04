package com.mindjourney.core.eventbus.observer

import com.mindjourney.core.eventbus.model.event.context.FlowObserverContext
import com.mindjourney.core.eventbus.service.IEventManager

/**
 * Contract for observers that watch a Flow<T> and emit ObserverEvents
 * defined by the supplied FlowObserverContext.
 *
 * Implementations must:
 *  - collect the provided Flow<T>
 *  - convert each emission into an ObserverEvent using context.mapToEvent
 *  - forward the event to the EventManager
 *  - support lifecycle termination via a finishFlow
 *
 * No business logic or filtering belongs here.
 */
interface IFlowObserver {

    /** Starts observing the provided flow according to the given context. */
    fun <T> start(eventManager: IEventManager, context: FlowObserverContext<T>)
}
