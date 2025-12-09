package com.mindjourney.core.eventbus.trigger.initializer

import com.mindjourney.core.eventbus.service.IEventManager
import jakarta.inject.Inject

class DomainTriggerInitializer @Inject constructor(
    eventManager: IEventManager
) : TriggerInitializer(eventManager), IDomainTriggerInitializer
