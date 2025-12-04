package com.mindjourney.core.eventbus.testutil

import com.mindjourney.core.eventbus.consumer.TriggerResultConsumer
import com.mindjourney.core.eventbus.model.trigger.TriggerResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * Collects TriggerResult emissions for assertion in unit tests.
 */
class TestTriggerResultConsumer : TriggerResultConsumer {

    private val _results = MutableSharedFlow<TriggerResult>(extraBufferCapacity = 16)
    val results: SharedFlow<TriggerResult> get() = _results

    override fun consume(result: TriggerResult) {
        _results.tryEmit(result)
    }
}
