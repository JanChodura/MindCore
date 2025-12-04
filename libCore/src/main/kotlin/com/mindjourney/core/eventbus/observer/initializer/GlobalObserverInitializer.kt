package com.mindjourney.core.eventbus.observer.initializer

import com.mindjourney.core.eventbus.model.event.context.ObserverContext
import com.mindjourney.core.eventbus.observer.FlowObserver
import com.mindjourney.core.eventbus.observer.TimeObserver
import com.mindjourney.core.eventbus.service.IEventManager
import jakarta.inject.Inject
import jakarta.inject.Singleton

/**
 * Initializes globally scoped observers at application startup.
 *
 * All ObserverContexts are supplied by DI and represent observers that are
 * active for the entire lifetime of the application (e.g. time ticks,
 * application lifecycle events, global system flows).
 *
 * GlobalObserverInitializer does not contain any observer logic — it merely
 * forwards the DI-provided contexts to the base ObserverInitializer, which
 * activates the appropriate observer implementations.
 *
 * Calling [initialize] wires all global observers into the EventManager.
 * No dynamic or per-ViewModel behavior is handled here.
 */

@Singleton
class GlobalObserverInitializer @Inject constructor(
    private val contexts: List<ObserverContext>,
    private val eventManager: IEventManager,
    private val flowObserver: FlowObserver,
    private val timeObserver: TimeObserver,
) : ObserverInitializer(eventManager, flowObserver, timeObserver) {


    /** Activates all global observers. */
    fun initialize() {
        initialize(contexts)
    }
}
