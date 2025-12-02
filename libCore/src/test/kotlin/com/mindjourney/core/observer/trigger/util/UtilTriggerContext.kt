package com.mindjourney.core.observer.trigger.util

import com.mindjourney.core.observer.trigger.model.IAppTrigger
import com.mindjourney.core.observer.trigger.model.TriggerDescription

object UtilTriggerContext {

    /**
     * Creates a simple TriggerContext for testing.
     * No polling config is included unless provided.
     */
    fun createSimpleTriggerContext(
        trigger: IAppTrigger,
        description: TriggerDescription,
        pollCycles: Int? = null,
        pollIntervalSec: Int? = null
    ): TriggerContext {
        return TriggerContext(
            description = description,
            trigger = trigger,
            pollCycles = pollCycles,
            pollIntervalSec = pollIntervalSec
        )
    }
}
