package com.mindjourney.core.observer.trigger.util

import com.mindjourney.core.observer.trigger.model.ReactiveTrigger
import com.mindjourney.core.observer.trigger.model.TriggerResult
import kotlinx.coroutines.flow.Flow

class SimpleReactiveTestTrigger<T>(
    override val sourceFlow: Flow<T>,
    private val result: TriggerResult,
) : ReactiveTrigger<T>(sourceFlow) {

    override suspend fun tryExecute(): TriggerResult = result
}
