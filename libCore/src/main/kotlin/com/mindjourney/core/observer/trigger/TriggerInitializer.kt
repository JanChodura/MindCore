package com.mindjourney.core.observer.trigger

import com.mindjourney.common.BuildConfig
import com.mindjourney.core.observer.trigger.model.IPollingTrigger
import com.mindjourney.core.observer.trigger.model.IReactiveTrigger
import com.mindjourney.core.observer.trigger.model.PollConfig
import com.mindjourney.core.observer.trigger.model.TriggerDescription
import com.mindjourney.core.observer.trigger.model.TriggerResult
import com.mindjourney.core.observer.trigger.util.TriggerContext
import com.mindjourney.core.util.logging.injectedLogger
import com.mindjourney.core.util.logging.off
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Orchestration entry point responsible for wiring a trigger into execution.
 *
 * It:
 *  - configures polling flows when needed,
 *  - starts reactive triggers,
 *  - bridges results back to the consumer layer.
 *
 * This class does not implement trigger logic itself — it simply binds
 * the trigger instance to its execution mechanism.
 */

class TriggerInitializer(
    private val scope: CoroutineScope,
    private val onResult: (TriggerResult) -> Unit
) {

    private val log = injectedLogger<TriggerInitializer>(off)

    /** Entry point – determines and initializes the correct trigger type. */
    fun init(context: TriggerContext) {
        val description = context.description
        when (val trigger = context.trigger) {
            is IPollingTrigger -> initPollingTrigger( context, trigger)
            is IReactiveTrigger -> initReactiveTrigger(description, trigger)
            else -> log.w("Trigger:$description does not implement IPollingTrigger or IReactiveTrigger")
        }
    }

    /** Initializes and starts a PollingTrigger. */
    private fun initPollingTrigger(
        context: TriggerContext,
        trigger: IPollingTrigger
    ) {
        val tickStream = TriggerPoll.createTickStream(
                scope,
            PollConfig(
                cycles = context.pollCycles ?: PollConfig.DEFAULT.cycles,
                intervalSec = context.pollIntervalSec ?: PollConfig.DEFAULT.intervalSec
            ),
        context.description
        )

        println("ticks: ${tickStream.value}")
        scope.launch {
            tickStream.collectLatest {
                val result = trigger.tryExecute()
                if (result !is TriggerResult.None) onResult(result)
            }
        }
    }

    /** Initializes and starts a ReactiveTrigger. */
    private fun initReactiveTrigger(
        description: TriggerDescription,
        trigger: IReactiveTrigger
    ): IReactiveTrigger {
        log.d("Initializing ReactiveTrigger: $description")
        trigger.startReactiveFlow(scope, description) { result ->
            if (result !is TriggerResult.None) onResult(result)
        }
        return trigger
    }
}
