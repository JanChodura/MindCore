package com.mindjourney.core.eventbus.model.trigger

import com.mindjourney.core.eventbus.model.trigger.result.TriggerResultType
import jakarta.inject.Inject
import jakarta.inject.Singleton

/**
 * A minimalistic trigger implementation that performs no evaluation or domain logic.
 *
 * This trigger simply returns [TriggerResultType.Success] whenever it is executed.
 *
 * Use this trigger when the mere occurrence of an observer event is sufficient
 * to signal completion, without requiring additional checks or processing.
 */
@Singleton
class SimpleEagerTrigger @Inject constructor() : BaseTrigger() {

    override var description: TriggerDescription =
        TriggerDescription("SimpleTrigger")

    override suspend fun tryExecute(
    ): TriggerResultType {
        completed.value = true
        return TriggerResultType.Success
    }
}
