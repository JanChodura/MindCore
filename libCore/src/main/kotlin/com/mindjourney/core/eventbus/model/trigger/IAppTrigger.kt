package com.mindjourney.core.eventbus.model.trigger

import com.mindjourney.core.eventbus.model.trigger.result.TriggerResultType
import kotlinx.coroutines.flow.StateFlow


/**
 * Contract for all triggers in the event-driven architecture.
 *
 * A trigger reacts to exactly one ObserverEvent type, as declared in its TriggerContext.
 * The EventManager invokes the trigger and wraps the returned TriggerResultType
 * into a full TriggerResult containing metadata.
 *
 * Triggers MUST NOT observe flows, handle scheduling, or depend on Android lifecycle.
 * They hold pure business logic and declare only whether they are currently ready.
 */
interface IAppTrigger {


    /**
     * Executes the trigger's business logic.
     * Returns only the outcome (TriggerResultType). The EventManager constructs
     * the full TriggerResult wrapper.
     */
    suspend fun tryExecute(): TriggerResultType


    /** Indicates whether this trigger is allowed to run. Controlled externally. */
    val isReady: StateFlow<Boolean>


    /** Metadata used for diagnostics and logging. */
    var description: TriggerDescription
}
