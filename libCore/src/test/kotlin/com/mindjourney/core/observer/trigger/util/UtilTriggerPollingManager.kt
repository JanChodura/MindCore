package com.mindjourney.core.observer.trigger.util

import com.mindjourney.core.observer.trigger.model.IAppTrigger
import com.mindjourney.core.observer.trigger.TriggerPollingManager
import com.mindjourney.core.util.logging.ILogger
import kotlinx.coroutines.test.TestScope

object UtilTriggerPollingManager {

    /**
     * Creates a TriggerPollingManager with a single simple TriggerContext.
     *
     * @param logger Logger to use in polling and triggers.
     * @param trigger The trigger to use for this single test context.
     */
    fun createSimpleManager(
        logger: ILogger,
        scope: TestScope,
        trigger: IAppTrigger
    ): TriggerPollingManager {

        val context = UtilTriggerContext.createSimpleTriggerContext(
            event = "testEvent",
            trigger = trigger,
            scope = scope
        )

        return TriggerPollingManager(scope, logger).apply {
            addTriggerPolls(null, "sourceOfTriggerTest")

            val field = TriggerPollingManager::class.java.getDeclaredField("triggers")
            field.isAccessible = true
            field.set(this, listOf(context))
        }
    }



}
