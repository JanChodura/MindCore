package com.mindjourney.core.eventbus.observer.initializer

import com.mindjourney.core.eventbus.model.event.context.FlowObserverContext
import com.mindjourney.core.eventbus.model.event.context.ObserverContext
import com.mindjourney.core.eventbus.model.event.context.TimeObserverContext
import com.mindjourney.core.eventbus.observer.FlowObserver
import com.mindjourney.core.eventbus.observer.TimeObserver
import com.mindjourney.core.eventbus.service.IEventManager

/**
 * Abstract base class for initializing observers described by ObserverContexts.
 *
 * ObserverInitializer does not hold any list of contexts itself. Instead,
 * concrete subclasses (GlobalObserverInitializer, DomainObserverInitializer)
 * supply the appropriate contexts and delegate their activation to this class.
 *
 * The initializer maps each ObserverContext to its corresponding Observer
 * implementation (e.g., FlowObserver, TimeObserver) and connects it to the
 * EventManager. No business logic or filtering is performed here — the purpose
 * of this class is strictly mechanical wiring between context and observer.
 *
 * This initializer never stores state and never controls lifecycle. Its job is
 * purely: “for each provided ObserverContext, activate the correct observer”.
 */

abstract class ObserverInitializer(
    private val eventManager: IEventManager,
    private val flowObserver: FlowObserver,
    private val timeObserver: TimeObserver,
) {

    fun initialize(contexts: List<ObserverContext>) {
        contexts.forEach { ctx ->
            when (ctx) {
                is FlowObserverContext<*> -> flowObserver.start(eventManager, ctx)
                is TimeObserverContext -> timeObserver.start(eventManager, ctx)
            }
        }
    }
}
