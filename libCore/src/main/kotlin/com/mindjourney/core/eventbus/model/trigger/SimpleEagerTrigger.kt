package com.mindjourney.core.eventbus.model.trigger

import com.mindjourney.core.eventbus.model.trigger.result.TriggerResultType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.mindjourney.core.eventbus.model.trigger.context.TriggerContext

/**
 * A minimalistic trigger implementation that performs no evaluation or domain logic.
 *
 * This trigger simply returns [TriggerResultType.Success] whenever it is executed.
 * It is always ready, contains no internal state, and does not depend on any external
 * inputs or observers. The EventManager may associate it with any event type through
 * a [TriggerContext].
 *
 * Use this trigger when the mere occurrence of an observer event is sufficient
 * to signal completion, without requiring additional checks or processing.
 */
class SimpleEagerTrigger : IAppTrigger {

    override var description: TriggerDescription =
        TriggerDescription("SimpleTrigger")

    private val ready = MutableStateFlow(true)
    override val isReady: StateFlow<Boolean> = ready

    override suspend fun tryExecute(
    ): TriggerResultType {
        return TriggerResultType.Success
    }
}
