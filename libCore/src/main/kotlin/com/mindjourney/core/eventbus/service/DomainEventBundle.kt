package com.mindjourney.core.eventbus.service

import com.mindjourney.core.eventbus.model.event.context.ObserverContext
import com.mindjourney.core.eventbus.model.trigger.context.TriggerContext
import com.mindjourney.core.eventbus.observer.initializer.DomainObserverInitializer
import dagger.hilt.android.scopes.ViewModelScoped
import jakarta.inject.Inject

@ViewModelScoped
class DomainEventBundle @Inject constructor(
    override val observerContexts: List<ObserverContext>,
    override val triggerContexts: List<TriggerContext>,
    override val observerInitializer: DomainObserverInitializer,
    override val triggerInitializer: DomainObserverInitializer
) : IDomainEventBundle {

    fun initialize() {
        observerInitializer.initialize(observerContexts)
    }
}
