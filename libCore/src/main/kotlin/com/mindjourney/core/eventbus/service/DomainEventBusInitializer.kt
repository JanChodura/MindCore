package com.mindjourney.core.eventbus.service

import com.mindjourney.core.di.CoreQualifier
import com.mindjourney.core.eventbus.observer.initializer.IDomainObserverInitializer
import com.mindjourney.core.eventbus.trigger.initializer.IDomainTriggerInitializer
import dagger.hilt.android.scopes.ViewModelScoped
import jakarta.inject.Inject

@ViewModelScoped
class DomainEventBusInitializer @Inject constructor(
    private val triggerInitializer: IDomainTriggerInitializer,
    private val observerInitializer: IDomainObserverInitializer,
    @param:CoreQualifier.Domain private val eventBundle: IDomainEventBundle
) : IDomainEventBusInitializer {

    override fun initialize() {
        triggerInitializer.initialize(eventBundle.triggerContexts)
        observerInitializer.initialize(eventBundle.observerContexts)
    }
}
