package com.mindjourney.core.eventbus.trigger.initializer

import com.mindjourney.core.eventbus.model.trigger.context.TriggerContext
import com.mindjourney.core.eventbus.service.IEventManager

/**
 * Abstract base class for initializing TriggerContexts.
 *
 * A TriggerInitializer does not hold trigger contexts itself; concrete
 * implementations (GlobalTriggerInitializer, DomainTriggerInitializer)
 * provide the list of contexts to initialize.
 *
 * The base class performs the mechanical work of registering each trigger
 * into the EventManager. No filtering, lifecycle handling, or business logic
 * occurs here: this class has exactly one responsibility — wiring trigger
 * definitions into the event system.
 */
abstract class TriggerInitializer(
    protected val eventManager: IEventManager
) {
    /**
     * Registers all provided trigger contexts into the EventManager.
     * Implementations supply the contexts.
     */
    protected fun registerAll(contexts: List<TriggerContext>) {
        contexts.forEach { eventManager.register(it) }
    }
}
