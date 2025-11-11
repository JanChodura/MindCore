package com.mindjourney.core.observer.trigger.util

import com.mindjourney.core.observer.trigger.model.IAppTrigger
import com.mindjourney.core.observer.trigger.model.TriggerResult

class TestTrigger(
    private val result: TriggerResult
) : IAppTrigger {
    override suspend fun tryExecute(): TriggerResult = result
}
