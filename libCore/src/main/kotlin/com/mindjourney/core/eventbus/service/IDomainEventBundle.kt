package com.mindjourney.core.eventbus.service

import com.mindjourney.core.eventbus.model.event.context.ObserverContext
import com.mindjourney.core.eventbus.model.trigger.context.TriggerContext
import com.mindjourney.core.eventbus.observer.initializer.DomainObserverInitializer

interface IDomainEventBundle {
    val observerContexts: List<ObserverContext>
    val triggerContexts: List<TriggerContext>
    val observerInitializer: DomainObserverInitializer
    val triggerInitializer: DomainObserverInitializer
}
