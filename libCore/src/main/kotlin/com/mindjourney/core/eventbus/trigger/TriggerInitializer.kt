package com.mindjourney.core.eventbus.trigger

import com.mindjourney.core.eventbus.model.trigger.context.TriggerContext
import com.mindjourney.core.eventbus.service.EventManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Initializes all globally scoped triggers at application startup.
 *
 * This initializer receives a DI-provided list of [TriggerContext] instances, each
 * representing a trigger–event binding. Its responsibility is to register all these
 * triggers into the [EventManager], ensuring that:
 *
 *  - each trigger is assigned to exactly one ObserverEvent
 *  - the EventManager correctly maps event → list of triggers
 *  - global triggers become active before any observer starts emitting events
 *
 * The method [initialize] must be invoked once during the application lifecycle,
 * typically in the application's startup phase (e.g. Application.onCreate).
 *
 * This class performs no evaluation, no filtering, and no logic beyond registration.
 */
@Singleton
class TriggerInitializer @Inject constructor(
    private val eventManager: EventManager,
    private val triggerContexts: List<TriggerContext>
) : ITriggerInitializer {

    /**
     * Registers all global triggers into the EventManager.
     *
     * The registration uses the event defined inside each TriggerContext.
     * After this point, incoming ObserverEvents will correctly dispatch
     * to their associated triggers.
     */
    override fun initialize() {
        triggerContexts.forEach { context ->
            eventManager.register(context)
        }
    }
}
