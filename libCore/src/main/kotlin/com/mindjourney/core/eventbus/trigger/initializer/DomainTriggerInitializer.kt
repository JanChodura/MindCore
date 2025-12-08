package com.mindjourney.core.eventbus.trigger.initializer

import com.mindjourney.core.eventbus.service.IEventManager
import jakarta.inject.Inject

/**
 * Initializes triggers that belong to a specific domain component, typically
 * a ViewModel.
 *
 * Unlike the global initializer, this class does not receive TriggerContexts
 * from DI. Instead, the caller (commonly ReactiveHandler) supplies the list of
 * contexts associated with the current DomainContext.
 *
 * Domain triggers are therefore isolated, short-lived, and scoped to a single
 * feature or screen. Calling [initialize] registers only those triggers that
 * are local to the component.
 */
class DomainTriggerInitializer @Inject constructor(
    eventManager: IEventManager
) : TriggerInitializer(eventManager)
