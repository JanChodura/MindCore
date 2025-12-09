package com.mindjourney.core.eventbus.trigger.initializer

import com.mindjourney.core.eventbus.model.CoreQualifier
import com.mindjourney.core.eventbus.model.trigger.context.TriggerContext
import com.mindjourney.core.eventbus.service.IEventManager
import jakarta.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalTriggerInitializer @Inject constructor(
    eventManager: IEventManager,
    @param:CoreQualifier.Global private val contexts: List<TriggerContext>
) : TriggerInitializer(eventManager), IGlobalTriggerInitializer {

    override fun initialize() {
        initialize(contexts)
    }
}
