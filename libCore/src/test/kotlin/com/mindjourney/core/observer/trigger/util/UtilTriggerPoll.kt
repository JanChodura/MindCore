package com.mindjourney.core.observer.trigger.util

import com.mindjourney.core.observer.trigger.TriggerPoll
import com.mindjourney.core.observer.trigger.model.PollConfig
import com.mindjourney.core.observer.trigger.model.Tick
import com.mindjourney.core.observer.trigger.model.TriggerDescription
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

object UtilTriggerPoll {

    /**
     * Creates a tick stream suitable for unit testing.
     *
     * @param scope CoroutineScope of the test.
     * @param cycles Number of ticks before final tick.
     * @param intervalSec Delay between ticks in seconds.
     */
    fun createTestTickStream(
        scope: CoroutineScope,
        cycles: Int = 2,
        intervalSec: Int = 0,   // fast tests default
        name: String = "testTickStream"
    ): StateFlow<Tick> {
        return TriggerPoll.createTickStream(
            scope = scope,
            config = PollConfig(cycles, intervalSec),
            description = TriggerDescription(name)
        )
    }
}
