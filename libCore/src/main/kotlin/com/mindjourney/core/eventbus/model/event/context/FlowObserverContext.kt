package com.mindjourney.core.eventbus.model.event.context

import com.mindjourney.core.eventbus.model.event.ObserverEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * Declarative configuration for observing a Flow<T> and converting its emissions
 * into ObserverEvents.
 *
 * This context is consumed by ObserverInitializers, which attach the FlowObserver
 * to the event pipeline. The observer:
 *
 *  - collects the provided Flow<T>
 *  - transforms each emission using [mapToEvent]
 *  - emits non-null ObserverEvents into the EventManager
 *
 * This context contains *no logic* — only configuration describing how the
 * observation should behave.
 *
 * @param T the type emitted by the observed Flow
 */
data class FlowObserverContext<T>(
    /** The Flow being observed. */
    val flow: Flow<T>,

    /**
     * Mapping function converting each Flow emission into an ObserverEvent.
     * Returning `null` means "no event should be emitted for this emission".
     */
    val mapToEvent: (T) -> ObserverEvent?,

    /** Metadata describing the expected event type produced by this observer. */
    override val event: ObserverEvent,

    override val finishFlow: Flow<Unit> = emptyFlow()
) : ObserverContext()
