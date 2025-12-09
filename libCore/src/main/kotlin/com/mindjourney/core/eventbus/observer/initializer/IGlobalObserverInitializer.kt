package com.mindjourney.core.eventbus.observer.initializer

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

interface IGlobalObserverInitializer : IObserverInitializer {
    /** Activates all global observers. */
    fun initialize()
}
