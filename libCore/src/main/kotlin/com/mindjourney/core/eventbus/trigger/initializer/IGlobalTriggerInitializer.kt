package com.mindjourney.core.eventbus.trigger.initializer

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

interface IGlobalTriggerInitializer : ITriggerInitializer {
    fun initialize()
}
