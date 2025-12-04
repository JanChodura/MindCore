package com.mindjourney.core.eventbus.testutil

import com.mindjourney.core.eventbus.observer.terminator.IObserverLifecycleTerminator
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

class NoopTerminator : IObserverLifecycleTerminator {
    override fun start(job: Job, finishFlow: Flow<Unit>) {
        // Do nothing — never cancels job
    }
}
