package com.mindjourney.core.eventbus.service

import com.mindjourney.core.eventbus.observer.initializer.DomainObserverInitializer
import com.mindjourney.core.eventbus.trigger.initializer.DomainTriggerInitializer
import dagger.hilt.android.scopes.ViewModelScoped
import jakarta.inject.Inject

@ViewModelScoped
class DomainEventBusInitializer @Inject constructor(
    private val triggerInitializer: DomainTriggerInitializer,
    private val observerInitializer: DomainObserverInitializer,
    private val eventBundle: DomainEventBundle
) : IDomainEventBusInitializer {

    override fun initialize() {
        triggerInitializer.initialize(eventBundle.triggerContexts)
        observerInitializer.initialize(eventBundle.observerContexts)
    }
}
