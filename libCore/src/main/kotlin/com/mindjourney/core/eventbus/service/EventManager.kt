package com.mindjourney.core.eventbus.service

import com.mindjourney.core.eventbus.model.event.ObserverEvent
import com.mindjourney.core.eventbus.model.trigger.IAppTrigger
import com.mindjourney.core.eventbus.model.trigger.TriggerResult
import com.mindjourney.core.eventbus.model.trigger.context.TriggerContext
import com.mindjourney.core.eventbus.service.reactive.TriggerResultBus
import com.mindjourney.core.logger.helper.injectedLogger
import com.mindjourney.core.logger.service.on
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Default implementation of the central dispatcher in the trigger architecture.
 *
 * Responsibilities:
 *  - register trigger/event bindings
 *  - dispatch incoming ObserverEvents to the correct triggers
 *  - invoke triggers
 *  - wrap returned TriggerResultType into a full TriggerResult
 *  - deliver results to bus for further processing
 */
class EventManager @Inject constructor(
    private val scope: CoroutineScope,
    private val triggerResultBus: TriggerResultBus
) : IEventManager {

    private val log = injectedLogger<EventManager>(on)
    private val bindings = mutableMapOf<Class<out ObserverEvent>, MutableList<IAppTrigger>>()

    /** Register trigger-event association */
    override fun register(context: TriggerContext) {
        bindings.computeIfAbsent(context.event::class.java) { mutableListOf() }
            .add(context.trigger)
    }

    /** Dispatch event to all registered triggers */
    override fun onEvent(event: ObserverEvent) {
        log.d("Event received: $event")
        val triggers = bindings[event::class.java] ?: return

        triggers.forEach { trigger ->
            if (trigger.isCompleted.value) {
                log.d("Trigger already completed: ${trigger.description}, skipping execution.")
                executeTrigger(trigger, event)
            }
        }
    }

    /** Executes a trigger and forwards the result */
    private fun executeTrigger(trigger: IAppTrigger, event: ObserverEvent) {
        scope.launch {
            val type = trigger.tryExecute()

            val result = TriggerResult(
                type = type,
                description = trigger.description,
                triggeredBy = event
            )

            log.d("Trigger executed: ${trigger.description}, Result: $result")
            triggerResultBus.publish(result)
        }
    }
}
