package com.mindjourney.core.observer.trigger.util

import com.mindjourney.core.observer.trigger.TriggerPoll
import com.mindjourney.core.observer.trigger.model.IAppTrigger
import com.mindjourney.core.observer.trigger.model.TriggerDescription
import kotlinx.coroutines.test.TestScope

object UtilTriggerPollingManager {

    /**
     * Creates a TriggerPollingManager with a single simple TriggerContext.
     *
     * @param trigger The trigger to use for this single test context.
     */
    fun createSimpleManager(
        scope: TestScope,
        trigger: IAppTrigger
    ): TriggerPoll {

        val context = UtilTriggerContext.createSimpleTriggerContext(
            trigger = trigger,
            scope = scope
        )

        return TriggerPoll(scope, TriggerDescription()).apply {

            val field = TriggerPoll::class.java.getDeclaredField("triggers")
            field.isAccessible = true
            field.set(this, listOf(context))
        }
    }


}
