package com.mindjourney.core.observer.trigger.util

import com.mindjourney.core.observer.trigger.model.TriggerResult
import com.mindjourney.core.observer.trigger.model.TriggerResultConsumer
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * Test implementation of [TriggerResultConsumer] that records
 * all received results and exposes them as a [SharedFlow] for assertions.
 */
class TestTriggerResultConsumer : TriggerResultConsumer {

    private val _results = MutableSharedFlow<TriggerResult>(extraBufferCapacity = 16)
    val results: SharedFlow<TriggerResult> get() = _results

    override suspend fun consume(result: TriggerResult) {
        _results.emit(result)
    }
}
