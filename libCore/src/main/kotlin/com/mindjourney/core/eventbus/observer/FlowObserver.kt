package com.mindjourney.core.eventbus.observer

import com.mindjourney.core.eventbus.model.event.context.FlowObserverContext
import com.mindjourney.core.eventbus.observer.terminator.IObserverLifecycleTerminator
import com.mindjourney.core.eventbus.service.IEventManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import jakarta.inject.Inject
import javax.inject.Singleton

/**
 * Observer that listens to a Flow<T> and emits ObserverEvents produced by
 * the mapping function inside FlowObserverContext.
 *
 * Responsibilities:
 *  - Collect the provided Flow<T>
 *  - Map each emission to an ObserverEvent (mapToEvent)
 *  - Forward the event to the EventManager
 *  - Stop observing when finishFlow emits (via IObserverLifecycleTerminator)
 *
 * This class contains *no business logic*. It is purely a mechanical bridge
 * Flow<T> → ObserverEvent → EventManager.
 */
class FlowObserver @Inject constructor(
    private val scope: CoroutineScope, private val lifecycleTerminator: IObserverLifecycleTerminator?
) : IFlowObserver {

    override fun <T> start(eventManager: IEventManager, context: FlowObserverContext<T>) {
        // If no terminator is injected (e.g., in previews or tests), do nothing.
        lifecycleTerminator ?: return

        val job: Job = scope.launch {
            context.flow.collectLatest { value ->
                val event = context.mapToEvent(value)
                if (event != null) {
                    eventManager.onEvent(event)
                }
            }
        }

        // Lifecycle-controlled termination
        lifecycleTerminator.tryTerminate(job, context.finishFlow)
    }
}
