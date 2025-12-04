package com.mindjourney.core.observer.trigger.util

import com.mindjourney.core.observer.trigger.TriggerManager
import com.mindjourney.core.observer.trigger.model.IAppTrigger
import com.mindjourney.core.observer.trigger.model.TriggerDescription
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

object UtilTriggerManager {

    fun createWithContexts(
        scope: CoroutineScope,
        contexts: List<TriggerContext>
    ): TriggerManager {

        val manager = TriggerManager(scope)

        val flow = MutableStateFlow(contexts)

        manager.registerTriggers(flow)

        return manager
    }

    fun createWithSingleTrigger(
        scope: CoroutineScope,
        trigger: IAppTrigger,
        description: TriggerDescription
    ): TriggerManager {
        val context = UtilTriggerContext.createSimpleTriggerContext(trigger, description)
        return createWithContexts(scope, listOf(context))
    }
}
