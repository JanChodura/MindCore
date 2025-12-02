package com.mindjourney.core.observer.trigger.util

import com.mindjourney.core.observer.trigger.TriggerManager
import com.mindjourney.core.observer.trigger.model.IAppTrigger
import com.mindjourney.core.observer.trigger.util.TriggerContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope

object UtilTriggerManager {

    fun createWithContexts(
        testScope: TestScope,
        contexts: List<TriggerContext>
    ): TriggerManager {

        val dispatcher = StandardTestDispatcher(testScope.testScheduler)
        val scope = CoroutineScope(dispatcher)
        val manager = TriggerManager(scope)

        val flow = MutableStateFlow(contexts)

        manager.initTriggers(flow)

        return manager
    }

    fun createWithSingleTrigger(
        scope: TestScope,
        trigger: IAppTrigger
    ): TriggerManager {
        val context = UtilTriggerContext.createSimpleTriggerContext(trigger)
        return createWithContexts(scope, listOf(context))
    }
}
