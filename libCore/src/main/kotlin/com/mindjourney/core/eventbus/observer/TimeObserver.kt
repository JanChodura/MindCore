package com.mindjourney.core.eventbus.observer

import com.mindjourney.core.eventbus.model.event.ObserverEvent
import com.mindjourney.core.eventbus.model.event.context.TimeObserverContext
import com.mindjourney.core.eventbus.observer.terminator.IObserverLifecycleTerminator
import com.mindjourney.core.eventbus.service.IEventManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

/**
 * Observer that produces time-based ObserverEvents according to a TimeObserverPolicy.
 *
 * Responsibilities:
 *  - Emit events at regular intervals (policy.intervalMinutes)
 *  - Respect the active time window (policy.start .. policy.end)
 *  - Forward each generated ObserverEvent.TimeTick to the EventManager
 *  - Stop observing when finishFlow emits (handled by IObserverLifecycleTerminator)
 *
 * This class contains *no* business or scheduling logic beyond the mechanical
 * task of ticking and emitting events.
 */
class TimeObserver @Inject constructor(
    private val scope: CoroutineScope,
    private val lifecycleTerminator: IObserverLifecycleTerminator
) : ITimeObserver {

    override fun start(eventManager: IEventManager, context: TimeObserverContext) {
        val policy = context.policy

        val job: Job = scope.launch {
            while (true) {
                val now = LocalTime.now()

                if (now >= policy.start && now < policy.end) {
                    emitEvent(eventManager, now)
                }

                delay(policy.intervalMinutes * 60_000)
            }
        }

        // Stop ticking when finishFlow emits
        lifecycleTerminator.tryTerminate(
            job = job,
            finishFlow = context.finishFlow
        )
    }

    private fun emitEvent(eventManager: IEventManager, now: LocalTime) {
        val date = LocalDate.now()
        eventManager.onEvent(
            ObserverEvent.TimeTick(
                date = date,
                time = now,
                daysOfWeek = DayOfWeek.entries.toSet()
            )
        )
    }
}
