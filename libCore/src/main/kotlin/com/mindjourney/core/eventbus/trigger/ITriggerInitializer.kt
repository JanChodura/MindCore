package com.mindjourney.core.eventbus.trigger

/**
 * Contract for initializing trigger registrations within the event-driven system.
 *
 * Implementations (global or screen-specific) receive a DI-provided list of
 * {@link TriggerContext} instances in their constructor. This initializer's job
 * is to register each trigger with the {@link EventManager}, binding every trigger
 * to exactly one event type.
 *
 * Responsibilities:
 *  - Iterate over all trigger contexts
 *  - Register each trigger in the EventManager under its associated event type
 *  - Perform no business logic, evaluation, or filtering
 *
 * This method should be called once during the lifecycle phase where the initializer
 * becomes active (e.g., application startup for global triggers, screen initialization
 * for custom triggers).
 */
interface ITriggerInitializer {

    /**
     * Registers all provided triggers into the EventManager.
     *
     * Implementations use internally injected TriggerContexts to populate
     * the event → trigger mapping. No parameters are required.
     */
    fun initialize()
}
