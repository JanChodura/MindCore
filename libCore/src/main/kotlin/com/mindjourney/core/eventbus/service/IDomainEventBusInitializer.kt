package com.mindjourney.core.eventbus.service

/**
 * Coordinates setup of event-architecture bindings defined inside a DomainContext.
 *
 * This initializer does not create observers or triggers on its own.
 * It simply delegates to:
 *  - TriggerInitializer to register trigger contexts
 *  - ObserverInitializer to start observers tied to this component
 *
 * It is typically called by ReactiveHandler during ViewModel activation.
 */

interface IDomainEventBusInitializer {
    fun initialize()
}
