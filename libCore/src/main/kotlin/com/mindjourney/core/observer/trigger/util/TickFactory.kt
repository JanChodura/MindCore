package com.mindjourney.core.observer.trigger.util

import com.mindjourney.core.observer.trigger.model.Tick
import com.mindjourney.core.observer.trigger.model.TriggerDescription
import java.time.LocalTime

/**
 * Factory for creating before start of poll and updating Tick instances during polling.
 */
object TickFactory {
    fun empty() = Tick(TriggerDescription.empty(), 0, LocalTime.now(), isFinal = false)
    fun init(triggerDescription: TriggerDescription) = Tick(triggerDescription, 0, LocalTime.now())
    fun next(prev: Tick) = prev.copy(cycle = prev.cycle + 1, timestamp = LocalTime.now())
    fun markFinal(tick: Tick) = tick.copy(isFinal = true)
}
