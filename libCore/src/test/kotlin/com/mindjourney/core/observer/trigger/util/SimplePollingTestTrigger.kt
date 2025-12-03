package com.mindjourney.core.observer.trigger.util

import com.mindjourney.core.observer.trigger.model.IPollingTrigger
import com.mindjourney.core.observer.trigger.model.TriggerDescription
import com.mindjourney.core.observer.trigger.model.TriggerResult
import com.mindjourney.core.observer.trigger.model.TriggerResultType
import kotlinx.coroutines.flow.MutableStateFlow

class SimplePollingTestTrigger(
    private val result: TriggerResult
) : IPollingTrigger {

    override val isReady = MutableStateFlow(true)
    override var description = TriggerDescription("testPolling")

    override suspend fun tryExecute(): TriggerResult = result
}
