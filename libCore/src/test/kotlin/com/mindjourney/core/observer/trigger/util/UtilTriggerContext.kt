package com.mindjourney.core.observer.trigger.util

import com.mindjourney.core.observer.trigger.model.IAppTrigger
import kotlinx.coroutines.CoroutineScope

object UtilTriggerContext {

    /**
     * Creates a basic TriggerContext with a one-shot polling setup for testing.
     *
     * @param scope CoroutineScope for the polling.
     * @param event Name of the event.
     * @param trigger Trigger implementation to execute.
     */
    fun createSimpleTriggerContext(
        scope: CoroutineScope,
        event: String = "test",
        trigger: IAppTrigger,
    ): TriggerContext {
        val poll = UtilTriggerPoll.createSimpleTestingTriggerPoll(scope = scope)
        return TriggerContext(event, trigger, poll, "test")
    }
}
