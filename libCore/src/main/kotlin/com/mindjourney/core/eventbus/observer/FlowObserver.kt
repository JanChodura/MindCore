package com.mindjourney.core.eventbus.observer

import com.mindjourney.core.eventbus.model.event.ObserverEvent
import com.mindjourney.core.eventbus.model.event.context.FlowObserverContext
import com.mindjourney.core.eventbus.service.EventManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Generic observer that watches a Flow<T> and emits ObserverEvents
 * based on the mapping function provided in FlowObserverContext.
 *
 * Supports lifecycle termination through ObserverLifecycleTerminator.
 * Contains no business logic.
 */
@Singleton
class FlowObserver @Inject constructor(
    private val scope: CoroutineScope,
    private val lifecycleTerminator: ObserverLifecycleTerminator?
) {

    fun <T> start(eventManager: EventManager, context: FlowObserverContext<T>) {
        lifecycleTerminator ?: return
        val job: Job = scope.launch {
            context.flow.collectLatest { value ->
                eventManager.onEvent(
                    ObserverEvent.FlowChanged(value)
                )
            }
        }

        lifecycleTerminator.start(job, context.finishFlow)
    }
}
