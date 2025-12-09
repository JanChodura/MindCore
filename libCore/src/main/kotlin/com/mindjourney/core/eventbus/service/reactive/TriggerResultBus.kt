// TODO: Vlož do core/eventbus/service/resultbus/TriggerResultBus.kt

package com.mindjourney.core.eventbus.service.reactive

import com.mindjourney.core.eventbus.model.trigger.TriggerResult
import com.mindjourney.core.logger.helper.injectedLogger
import com.mindjourney.core.logger.service.off
import com.mindjourney.core.logger.service.on
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A global mailbox for TriggerResult objects.
 *
 * EventManager publishes every TriggerResult here.
 * ReactiveHandlers in various ViewModels subscribe to it.
 *
 * This object contains NO business logic.
 * It simply relays TriggerResults across architectural boundaries.
 */
@Singleton
class TriggerResultBus @Inject constructor() {

    private val log = injectedLogger<TriggerResultBus>(off)
    private val _results = MutableSharedFlow<TriggerResult>(extraBufferCapacity = 64)
    val results: SharedFlow<TriggerResult> = _results

    /**
     * Called by EventManager to deliver a TriggerResult to the global mailbox.
     */
    suspend fun publish(result: TriggerResult) {
        log.d("Publishing TriggerResult: $result")
        _results.emit(result)
    }
}
