package com.mindjourney.core.eventbus.trigger.initializer

import com.mindjourney.core.eventbus.model.trigger.context.TriggerContext
import com.mindjourney.core.eventbus.service.IEventManager

/**
 * Default implementation of [ITriggerInitializer] that mechanically wires
 * TriggerContexts into the EventManager.
 *
 * This class contains no business logic and makes no assumptions about
 * trigger readiness or domain behavior. It simply forwards each context to
 * the EventManager.
 */
abstract class TriggerInitializer(
    private val eventManager: IEventManager
) : ITriggerInitializer {

    override fun initialize(contexts: List<TriggerContext>) {
        contexts.forEach { eventManager.register(it) }
    }
}
