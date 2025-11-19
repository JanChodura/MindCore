package com.mindjourney.core.observer.trigger

import com.mindjourney.common.BuildConfig
import com.mindjourney.core.observer.trigger.model.PollingTrigger
import com.mindjourney.core.observer.trigger.model.IReactiveTrigger
import com.mindjourney.core.observer.trigger.model.TriggerDescription
import com.mindjourney.core.observer.trigger.model.TriggerResult
import com.mindjourney.core.observer.trigger.util.TriggerContext
import com.mindjourney.core.util.logging.injectedLogger
import com.mindjourney.core.util.logging.off
import kotlinx.coroutines.CoroutineScope

/**
 * Initializes and starts appropriate trigger type (Polling or Reactive)
 * based on TriggerContext.
 *
 * The class separates initialization logic into dedicated methods
 * for better readability and testability.
 */
class TriggerInitializer(
    private val scope: CoroutineScope,
    private val onResult: (TriggerResult) -> Unit
) {

    private val log = injectedLogger<TriggerInitializer>(off)

    /** Entry point – determines and initializes the correct trigger type. */
    fun init(context: TriggerContext) {
        val (description, trigger, triggerPoll) = context
        when (trigger) {
            is PollingTrigger -> initPollingTrigger(triggerPoll, description, context, trigger)
            is IReactiveTrigger -> initReactiveTrigger(description, trigger)
            else -> log.w("Trigger:$description does not implement PollingTrigger or ReactiveTrigger")
        }
    }

    /** Initializes and starts a PollingTrigger. */
    private fun initPollingTrigger(
        triggerPoll: TriggerPoll,
        description: TriggerDescription,
        context: TriggerContext,
        trigger: PollingTrigger
    ): PollingTrigger {
        log.d("Initializing PollingTrigger: $description")
        attachPollProperties(triggerPoll, description, context)

        log.d("Starting PollingTrigger: $description")
        triggerPoll.start()
        trigger.startPollingObservation(scope, description, triggerPoll) { result ->
            if (result !is TriggerResult.None) onResult(result)
        }
        return trigger
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

    /** Configures polling parameters before start. */
    private fun attachPollProperties(
        triggerPoll: TriggerPoll,
        description: TriggerDescription,
        context: TriggerContext
    ) {
        triggerPoll.attachDescription(description)
        triggerPoll.attachScope(scope)
        triggerPoll.attachCycles(context.pollCycles ?: BuildConfig.POLL_CYCLES)
        triggerPoll.attachInterval(context.pollIntervalSec ?: BuildConfig.POLL_INTERVAL_SEC)
    }
}
