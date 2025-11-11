package com.mindjourney.core.observer.trigger

import com.mindjourney.core.observer.trigger.model.PollingTrigger
import com.mindjourney.core.observer.trigger.model.TriggerDescription
import com.mindjourney.core.observer.trigger.model.TriggerResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Default implementation of [PollingTrigger] handling tick-based observation logic.
 * It collects ticks from [TriggerPoll] and executes [tryExecute] for each emission.
 */
abstract class PollingTriggerImpl : PollingTrigger {

    override fun startPollingObservation(
        scope: CoroutineScope,
        description: TriggerDescription,
        triggerPoll: TriggerPoll,
        onResult: (TriggerResult) -> Unit
    ) {
        this.description = description
        scope.launch(start = kotlinx.coroutines.CoroutineStart.UNDISPATCHED) {
            triggerPoll.tickFlow.collectLatest { tick ->
                if (tick.isFinal) {
                    cancel()
                } else {
                    val result = tryExecute()
                    if (result !is TriggerResult.None) {
                        onResult(result)
                    }
                }
            }
        }
    }
}
