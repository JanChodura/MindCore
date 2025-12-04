package com.mindjourney.core.eventbus.model.event.context

import com.mindjourney.core.eventbus.model.event.ObserverEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * Base sealed configuration for any observer in the event system.
 *
 * Each observer context describes:
 *  - which ObserverEvent the observer is expected to produce
 *  - a finishFlow that, when emitting a value, instructs the observer to stop observing
 *
 * The finishFlow is always present and never null. By default, it is an emptyFlow(),
 * meaning the observer will never be forcefully terminated unless a FinishSignal is
 * emitted from the domain layer via the ObserverFinishCenter.
 *
 * ObserverContexts contain no logic. They are purely declarative configuration
 * consumed by ObserverInitializers.
 */
sealed class ObserverContext {

    /** The ObserverEvent that this observer will emit. */
    abstract val event: ObserverEvent

    /**
     * A flow whose emission requests this observer to stop observing.
     *
     * By default: emptyFlow() = observer never finishes automatically.
     *
     * Values are emitted by the domain-level ObserverFinishCenter, which manages
     * lifecycle termination of observers based on business-driven conditions.
     */
    open val finishFlow: Flow<Unit> = emptyFlow()
}
