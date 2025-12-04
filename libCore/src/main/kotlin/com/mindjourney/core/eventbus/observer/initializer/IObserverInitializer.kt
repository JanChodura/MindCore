package com.mindjourney.core.eventbus.observer.initializer

import com.mindjourney.core.eventbus.model.event.context.ObserverContext

/**
 * Contract for components capable of activating observers described
 * by ObserverContexts.
 *
 * Implementations do not evaluate business logic, manage lifecycle,
 * or filter events. Their sole responsibility is to take a list of
 * ObserverContexts and connect them to the underlying Observer
 * implementations (FlowObserver, TimeObserver, ...).
 */
interface IObserverInitializer {

    /**
     * Activates all observers declared in the provided contexts.
     */
    fun initialize(contexts: List<ObserverContext>)
}
