package com.mindjourney.core.eventbus.observer.terminator

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

/**
 * Contract for components capable of terminating observer lifecycle jobs.
 *
 * Implementations listen to an external finish signal (typically a Flow<Unit>)
 * and cancel the associated running coroutine job when that signal is emitted.
 *
 * This interface contains **no business logic**, **no event dispatching**, and
 * no assumptions about coroutine scope. It defines only the mechanism required
 * by observers (FlowObserver, TimeObserver, ...) to support externally-driven
 * lifecycle shutdown.
 */
interface IObserverLifecycleTerminator {

    /**
     * Flow that emits when an observer lifecycle should terminate.
     * Useful for event chains that need to know when an observer has finished.
     */
    val terminatedFlow: Flow<Unit>

    /**
     * Starts monitoring [finishFlow], and cancels [job] once the flow emits.
     * Implementations decide how the monitoring coroutine is launched.
     */
    fun tryTerminate(job: Job, finishFlow: Flow<Unit>)
}
