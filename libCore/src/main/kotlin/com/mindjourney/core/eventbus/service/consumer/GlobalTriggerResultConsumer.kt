package com.mindjourney.core.eventbus.service.consumer

import com.mindjourney.core.eventbus.model.trigger.TriggerResult
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Singleton
class GlobalTriggerResultConsumer @Inject constructor() : TriggerResultConsumer {

    private val _results = MutableSharedFlow<TriggerResult>(extraBufferCapacity = 64)
    val results: SharedFlow<TriggerResult> = _results

    override fun consume(result: TriggerResult) {
        _results.tryEmit(result)
    }
}
