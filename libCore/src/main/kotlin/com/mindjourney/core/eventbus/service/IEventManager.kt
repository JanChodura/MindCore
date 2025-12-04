package com.mindjourney.core.eventbus.service

import com.mindjourney.core.eventbus.model.event.ObserverEvent
import com.mindjourney.core.eventbus.model.trigger.context.TriggerContext

/**
 * Central dispatcher for the event-driven trigger system.
 *
 * Responsibilities:
 * - register TriggerContexts
 * - maintain mapping: EventType -> List<IAppTrigger>
 * - onEvent(event): invoke all triggers bound to this event
 * - wrap returned TriggerResultType into TriggerResult
 * - forward results to TriggerResultConsumer
 */
interface IEventManager {
    /**
     * Register trigger-event association.
     */
    fun register(context: TriggerContext)


    /**
     * Called by observers when a new event is emitted.
     */
    fun onEvent(event: ObserverEvent)
}
