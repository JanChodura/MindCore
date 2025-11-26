package com.mindjourney.core.observer.trigger.util

import com.mindjourney.core.observer.trigger.model.IAppTrigger
import com.mindjourney.core.observer.trigger.model.TriggerDescription
import kotlinx.coroutines.CoroutineScope

object UtilTriggerContext {

    /**
     * Creates a basic TriggerContext with a one-shot polling setup for testing.
     *
     * @param scope CoroutineScope for the polling.
     * @param trigger Trigger implementation to execute.
     */
    fun createSimpleTriggerContext(
        scope: CoroutineScope,
        trigger: IAppTrigger,
    ): TriggerContext {
        return TriggerContext(TriggerDescription("screenReadiness"), trigger)
    }
}
