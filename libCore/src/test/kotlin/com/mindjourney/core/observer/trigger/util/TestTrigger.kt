package com.mindjourney.core.observer.trigger.util

import com.mindjourney.core.observer.trigger.model.ReactiveTrigger
import com.mindjourney.core.observer.trigger.model.TriggerDescription
import com.mindjourney.core.observer.trigger.model.TriggerResult
import kotlinx.coroutines.flow.MutableStateFlow

class TestTrigger(
) : ReactiveTrigger<Boolean>(
    reactiveFlow = MutableStateFlow(true),
    description = TriggerDescription(
        name = "test",
    )
) {
    override suspend fun tryExecute(): TriggerResult {

        return TriggerResult.Completed(true)
    }
}
