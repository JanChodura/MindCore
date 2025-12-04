package com.mindjourney.core.eventbus.observer.initializer

import com.mindjourney.core.eventbus.model.event.context.FlowObserverContext
import com.mindjourney.core.eventbus.model.event.context.ObserverContext
import com.mindjourney.core.eventbus.model.event.context.TimeObserverContext
import com.mindjourney.core.eventbus.observer.FlowObserver
import com.mindjourney.core.eventbus.observer.TimeObserver
import com.mindjourney.core.eventbus.service.IEventManager

/**
 * Abstract base class for initializing observers declared through ObserverContexts.
 *
 * Implementations (GlobalObserverInitializer, DomainObserverInitializer) supply
 * the list of contexts relevant to their scope, while this class performs the
 * mechanical work of mapping each ObserverContext to its corresponding Observer
 * implementation and connecting it to the EventManager.
 *
 * This initializer stores no state, performs no filtering, and manages no
 * lifecycle. Its job is purely to wire context → observer → EventManager.
 */
abstract class ObserverInitializer(
    private val eventManager: IEventManager,
    private val flowObserver: FlowObserver,
    private val timeObserver: TimeObserver,
) : IObserverInitializer {

    /**
     * Activates observers for every supplied ObserverContext by delegating
     * to the correct Observer implementation.
     */
    override fun initialize(contexts: List<ObserverContext>) {
        contexts.forEach { ctx ->
            when (ctx) {
                is FlowObserverContext<*> -> flowObserver.start(eventManager, ctx)
                is TimeObserverContext -> timeObserver.start(eventManager, ctx)
            }
        }
    }
}
