package com.mindjourney.core.eventbus.trigger

import com.mindjourney.core.eventbus.model.trigger.context.TriggerContext

/**
 * Contract for components capable of registering TriggerContexts
 * into the EventManager.
 *
 * Implementations do not execute triggers, evaluate logic,
 * or handle lifecycle. Their sole responsibility is to connect
 * declarative TriggerContexts with the EventManager.
 */
interface ITriggerInitializer {

    /**
     * Registers all provided trigger contexts.
     */
    fun initialize(contexts: List<TriggerContext>)
}
