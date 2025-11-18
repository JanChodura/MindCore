package com.mindjourney.core.observer.trigger

import com.mindjourney.core.logger.LoggerProvider
import com.mindjourney.core.observer.trigger.model.Tick
import com.mindjourney.core.observer.trigger.model.TriggerDescription
import com.mindjourney.core.observer.trigger.util.TickFactory
import com.mindjourney.core.util.logging.injectedLogger
import com.mindjourney.core.util.logging.off
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Periodically emits ticks for a defined number of cycles and interval.
 * Each emitted tick signals the polling manager to evaluate its trigger.
 *
 * The first tick is emitted immediately when {@link #start()} is called,
 * so trigger evaluation begins right away. Subsequent ticks are emitted
 * after each interval until all cycles are completed.
 *
 * This behavior ensures triggers react promptly after polling starts
 * while still following a timed evaluation pattern.
 */
class TriggerPoll(
    private var scope: CoroutineScope? = null,
    var description: TriggerDescription,
) {

    private val log = injectedLogger<TriggerPoll>(LoggerProvider.get(), off)

    companion object {

        /** Creates an empty/no-op instance of TriggerPoll for cases where DI is not available like Preview, Tests. */
        fun empty(): TriggerPoll =
            TriggerPoll(
                CoroutineScope(EmptyCoroutineContext),
                description = TriggerDescription.empty()
            )

        private const val MILLISECONDS_IN_SECOND = 1000L
    }

    private var intervalMs: Long = 0
    private var pollCycles: Int = 0

    private var actualTick: Tick = TickFactory.empty()

    private var pollingJob: Job? = null

    private val _tickFlow = MutableStateFlow(actualTick)
    val tickFlow: StateFlow<Tick> get() = _tickFlow

    fun attachScope(scope: CoroutineScope) {
        this.scope = scope
    }

    /**
     * Starts polling for a given duration and interval.
     * If polling is already active, it will be restarted.
     */
    fun start() {
        cancelExistingJob()
        pollingJob = scope?.launch {
            log.d("Started polling for  $pollCycles cycles every $intervalMs sec for trigger: $description")
            actualTick = TickFactory.init(description)
            pollLoop()
        }
    }

    /** Cancels any existing polling job. */
    private fun cancelExistingJob() {
        log.d("Cancelling existing polling job for trigger: $description")
        stop()
    }

    /** Stops the current polling job if active after duration. */
    private fun stop() {
        log.d("Stopping existing polling job for trigger: $description")
        pollingJob?.cancel()
        pollingJob = null
    }


    /** Emits the current time safely to the flow. */
    private fun emitTickSafely() {
        actualTick = TickFactory.next(actualTick)
        log.d("Emitting tick: $actualTick")
        _tickFlow.value = actualTick
    }

    private fun emitFinalTick() {
        actualTick = TickFactory.markFinal(actualTick)
        log.d("Emitting final tick: $actualTick")
        _tickFlow.value = actualTick
    }


    /**
     * Main polling loop that emits ticks at defined intervals for the set number of cycles.
     * After completing the cycles, it emits a final tick and stops polling.
     */
    private suspend fun pollLoop() {
        val safeScope = scope ?: error("Scope not attached before polling start")

        while (safeScope.isActive && actualTick.cycle < pollCycles) {
            emitTickSafely()
            delay(intervalMs)
        }

        emitFinalTick()
        stop()
    }

    fun attachDescription(description: TriggerDescription) {
        this.description = description
    }

    fun attachCycles(pollCycles: Int) {
        this.pollCycles = pollCycles
    }

    fun attachInterval(pollIntervalSec: Int) {
        this.intervalMs = pollIntervalSec * MILLISECONDS_IN_SECOND
    }
}
