package com.mindjourney.core.eventbus.service

import com.mindjourney.core.eventbus.observer.initializer.DomainObserverInitializer
import com.mindjourney.core.eventbus.trigger.initializer.DomainTriggerInitializer
import com.mindjourney.core.eventbus.model.DomainContext
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
class DomainEventInitializer @Inject constructor(
    private val triggerInitializer: DomainTriggerInitializer,
    private val observerInitializer: DomainObserverInitializer,
) {
    fun initialize(context: DomainContext) {
        triggerInitializer.initialize(context.triggerContexts)
        observerInitializer.initialize(context.observerContexts)
    }
}
