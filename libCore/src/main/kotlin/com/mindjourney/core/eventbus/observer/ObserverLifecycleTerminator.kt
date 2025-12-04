package com.mindjourney.core.eventbus.observer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Low-level utility that listens to a finish-flow signal and terminates
 * a running coroutine job when the signal is emitted.
 *
 * Used by observers (FlowObserver, TimeObserver, etc.) to support externally
 * controlled lifecycle stopping, typically via ObserverFinishCenter.
 *
 * This class contains no business logic and no event dispatching.
 * It simply reacts to a finish signal and cancels the associated job.
 */
class ObserverLifecycleTerminator(
    private val scope: CoroutineScope
) {

    /**
     * Starts listening to the given finishFlow. Once the flow emits any value,
     * the provided job is cancelled and the listener stops observing further signals.
     */
    fun start(job: Job, finishFlow: Flow<Unit>) {
        scope.launch {
            finishFlow.collectLatest {
                job.cancel()
                this.cancel() // stop finish listener itself
            }
        }
    }
}
