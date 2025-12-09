package com.mindjourney.core.eventbus.service

import com.mindjourney.core.eventbus.model.event.context.ObserverContext
import com.mindjourney.core.eventbus.model.trigger.context.TriggerContext
import com.mindjourney.core.eventbus.observer.initializer.IDomainObserverInitializer
import com.mindjourney.core.eventbus.trigger.initializer.ITriggerInitializer

interface IDomainEventBundle {
    val observerContexts: List<ObserverContext>
    val triggerContexts: List<TriggerContext>
}
