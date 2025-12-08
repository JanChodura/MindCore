package com.mindjourney.core.eventbus.trigger.initializer

import com.mindjourney.core.eventbus.model.trigger.context.TriggerContext
import com.mindjourney.core.eventbus.service.IEventManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Initializes globally scoped triggers during application startup.
 *
 * Global triggers are provided entirely via DI and are valid for the whole
 * lifetime of the application (e.g. time-based triggers, app-foreground
 * triggers, or any feature-independent logic).
 *
 * Calling [initialize] registers all DI-provided TriggerContexts through the
 * shared logic in the abstract [TriggerInitializer]. No additional behavior is
 * introduced here.
 */
@Singleton
class GlobalTriggerInitializer @Inject constructor(
    eventManager: IEventManager,
    private val contexts: List<TriggerContext>
) : TriggerInitializer(eventManager) {


    /** Registers all global triggers. */
    fun initialize() {
        initialize(contexts)
    }
}
