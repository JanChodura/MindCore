package com.mindjourney.core.observer.trigger.util

import com.mindjourney.core.observer.trigger.TriggerPoll
import com.mindjourney.core.observer.trigger.model.IAppTrigger
import com.mindjourney.core.observer.trigger.model.TriggerDescription

/**
 * Represents the context in which a trigger is executed.
 *
 * @property description The name of the event that caused the trigger to execute.
 * @property trigger The trigger instance to be executed.
 * @property triggerPoll Configuration for polling behavior of the trigger.
 *      It needs to be added to a polling manager separately due to unknown scope at this point.
 * @property pollCycles Optional number of polling cycles to perform.
 * @property pollIntervalSec Optional interval in seconds between polling cycles.
 */
data class TriggerContext(
    val description: TriggerDescription,
    val trigger: IAppTrigger,
    var triggerPoll: TriggerPoll = TriggerPoll.empty(),
    val pollCycles: Int? = null,
    val pollIntervalSec: Int? = null
)
