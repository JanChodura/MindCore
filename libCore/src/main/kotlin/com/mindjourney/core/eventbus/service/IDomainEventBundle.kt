package com.mindjourney.core.eventbus.service

import com.mindjourney.core.eventbus.model.event.context.ObserverContext
import com.mindjourney.core.eventbus.model.trigger.context.TriggerContext

interface IDomainEventBundle {
    val observerContexts: List<ObserverContext>
    val triggerContexts: List<TriggerContext>
}
