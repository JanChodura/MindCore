package com.mindjourney.core.eventbus.service

import com.mindjourney.core.eventbus.observer.initializer.DomainObserverInitializer
import com.mindjourney.core.eventbus.trigger.initializer.DomainTriggerInitializer
import dagger.hilt.android.scopes.ViewModelScoped
import jakarta.inject.Inject

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
@ViewModelScoped
class DomainEventBusInitializer @Inject constructor(
    private val triggerInitializer: DomainTriggerInitializer,
    private val observerInitializer: DomainObserverInitializer,
    private val eventBundle: DomainEventBundle
) {

    fun initialize() {
        triggerInitializer.initialize(eventBundle.triggerContexts)
        observerInitializer.initialize(eventBundle.observerContexts)
    }
}
