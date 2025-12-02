package com.mindjourney.core.observer.trigger

import com.mindjourney.core.observer.trigger.TriggerPoll.tickFlow
import com.mindjourney.core.observer.trigger.model.PollConfig
import com.mindjourney.core.observer.trigger.model.Tick
import com.mindjourney.core.observer.trigger.model.TriggerDescription
import com.mindjourney.core.observer.trigger.util.TickFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Produces timed diagnostic ticks used to drive polling-based triggers.
 *
 * It emits a sequence of [Tick] values at the requested interval until
 * the configured number of cycles is reached.
 *
 * Consumers observe [tickFlow] and treat each emission as a signal
 * to evaluate trigger logic.
 *
 * This class concerns itself only with timing — it performs no trigger logic.
 */
object TriggerPoll {

    private var actualTick: Tick = TickFactory.empty()

    private val _tickFlow = MutableStateFlow(actualTick)
    val tickFlow: StateFlow<Tick> get() = _tickFlow

    fun createTickStream(
        scope: CoroutineScope,
        config: PollConfig,
        description: TriggerDescription
    ): StateFlow<Tick> {

        val state = MutableStateFlow(TickFactory.init(description))

        scope.launch {
            repeat(config.cycles) { index ->
                if (index > 0) {
                    state.value = TickFactory.next(state.value)
                }
                delay(config.intervalSec * 1000L)
            }
            state.value = TickFactory.markFinal(state.value)
        }

        return state
    }
}
