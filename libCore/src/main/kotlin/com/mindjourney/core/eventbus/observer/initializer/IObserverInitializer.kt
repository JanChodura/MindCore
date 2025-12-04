package com.mindjourney.core.eventbus.observer.initializer

/**
 * Base contract for initializing event sources within the observer system.
 *
 * An ObserverInitializer is responsible for activating one or more EventSources
 * using DI-provided ObserverContexts. Each EventSource begins emitting
 * standardized ObserverEvents once initialized.
 *
 * Key responsibilities:
 *  - read ObserverContexts supplied from dependency injection
 *  - start each EventSource and connect it to the EventManager
 *  - perform no filtering, business logic, or trigger evaluation
 *
 * This abstraction allows for multiple initializer types:
 *  - GlobalObserverInitializer → starts global observers at application startup
 *  - CustomObserverInitializer → starts screen-specific observers scoped to a ViewModel
 *
 * Implementations must ensure that observers are started exactly once within
 * their intended lifecycle scope.
 */
interface IObserverInitializer {

    /**
     * Activates the observers described by the initializer's ObserverContexts.
     *
     * Implementations should:
     *  - start all EventSources contained in their ObserverContext set
     *  - ensure the event system becomes operational within the expected lifecycle phase
     *
     * No parameters are required — implementations retrieve everything from DI.
     */
    fun initialize()
}
