package com.mindjourney.core.eventbus.model.event

import kotlinx.coroutines.flow.Flow

/**
 * Provides finish signals for observers in the event system.
 *
 * The ObserverFinishCenter is a domain-driven component that exposes flows
 * which, when emitting a value, instruct specific observers to terminate.
 *
 * Observers do not decide their own lifecycle, and the EventManager does not
 * control observers directly. Instead, the domain layer emits finish signals
 * through this center, allowing observers to shut down cleanly.
 *
 * Example use cases:
 *  - a TimeObserver finishes once a morning routine trigger fires
 *  - a FlowObserver finishes when a screen becomes irrelevant
 *  - observers tied to temporary flows (e.g. onboarding) auto-expire
 *
 * Implementations may use SharedFlows, MutableStateFlows, or multi-channel
 * structures to expose independent termination flows per observer type.
 */
interface ObserverFinishCenter {

    /**
     * Returns a Flow<Unit> that will emit a value when the observer
     * associated with the given key should stop.
     *
     * @param key identifier for a specific observer or observer group
     * @return a Flow<Unit> emitting at most once to signal termination
     */
    fun finishSignalFor(key: String): Flow<Unit>
}
