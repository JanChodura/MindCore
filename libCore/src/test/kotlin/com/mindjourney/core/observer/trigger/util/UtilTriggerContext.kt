package com.mindjourney.core.observer.trigger.util

import com.mindjourney.core.observer.trigger.model.IAppTrigger
import com.mindjourney.core.observer.trigger.model.TriggerDescription
import kotlinx.coroutines.CoroutineScope

object UtilTriggerContext {

    /**
     * Creates a simple TriggerContext for testing.
     * No polling config is included unless provided.
     */
    fun createSimpleTriggerContext(
        trigger: IAppTrigger,
        name: String = "testTrigger",
        pollCycles: Int? = null,
        pollIntervalSec: Int? = null
    ): TriggerContext {
        return TriggerContext(
            description = TriggerDescription(name),
            trigger = trigger,
            pollCycles = pollCycles,
            pollIntervalSec = pollIntervalSec
        )
    }
}
