package com.mindjourney.core.eventbus.model.trigger

import com.mindjourney.core.eventbus.model.trigger.result.TriggerResultType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseTrigger : IAppTrigger {

    abstract override var description: TriggerDescription

    protected val completed = MutableStateFlow(false)
    override val isCompleted: StateFlow<Boolean> = completed

    abstract override suspend fun tryExecute(): TriggerResultType
}
