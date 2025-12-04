package com.mindjourney.core.eventbus.model.trigger.context

import com.mindjourney.core.eventbus.model.event.ObserverEvent
import com.mindjourney.core.eventbus.model.trigger.IAppTrigger

/**
 * Declarative configuration describing how a trigger is bound to the event system.
 *
 * TriggerContext is consumed by TriggerInitializers, which register each trigger
 * into the EventManager. The context provides:
 *
 *  - the trigger instance to evaluate
 *  - the specific ObserverEvent type that should activate the trigger
 *
 * Architectural rules:
 *  - Each trigger MUST be associated with exactly one ObserverEvent type.
 *  - No filtering or evaluation happens inside this context.
 *  - The EventManager is the only component responsible for dispatching events
 *    to the appropriate triggers.
 *
 * This sealed base class allows for future extension (e.g., different trigger
 * categories), while guaranteeing that all trigger bindings follow the same model.
 */
data class TriggerContext(

    /** The trigger to be executed when the associated event occurs. */
    val trigger: IAppTrigger,

    /** The event type that activates this trigger. */
    val event: ObserverEvent
)
