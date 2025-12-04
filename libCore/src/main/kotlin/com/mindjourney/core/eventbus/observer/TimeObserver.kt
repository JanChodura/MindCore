package com.mindjourney.core.eventbus.observer

import com.mindjourney.core.eventbus.model.event.ObserverEvent
import com.mindjourney.core.eventbus.model.event.context.TimeObserverContext
import com.mindjourney.core.eventbus.service.EventManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Observer that generates time-based ObserverEvents based on a TimeObserverPolicy.
 *
 * This observer:
 *  - emits an event at regular intervals within the configured time window
 *  - stops automatically when its finishFlow emits a signal (via FinishSignalListener)
 *
 * It contains no business logic; responsibility ends with emitting events.
 */
@Singleton
class TimeObserver @Inject constructor(
    private val scope: CoroutineScope,
    private val observerLifecycleTerminator: ObserverLifecycleTerminator
) {

    fun start(eventManager: EventManager, context: TimeObserverContext) {
        val policy = context.policy

        // Main ticking job
        val tickJob: Job = scope.launch {
            while (true) {
                val now = LocalTime.now()

                if (now >= policy.start && now < policy.end) {
                    onEvent(eventManager, now)
                }

                delay(policy.intervalMinutes * 60_000)
            }
        }

        // Listen for finish signals
        observerLifecycleTerminator.start(
            job = tickJob,
            finishFlow = context.finishFlow
        )
    }

    private fun onEvent(eventManager: EventManager, now: LocalTime) {
        val date = LocalDate.now()
        eventManager.onEvent(
            ObserverEvent.TimeTick(
                date = date,
                time = now,
                dayOfWeek = date.dayOfWeek
            )
        )
    }
}
