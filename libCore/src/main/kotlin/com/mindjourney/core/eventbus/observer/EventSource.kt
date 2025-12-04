package com.mindjourney.core.eventbus.observer

import com.mindjourney.core.eventbus.model.event.ObserverEvent
import com.mindjourney.core.eventbus.service.EventManager

/**
 * Represents a source of ObserverEvents within the application.
 *
 * An EventSource is responsible for *observing* some part of the system
 * (navigation, time signals, lifecycle, etc.) and forwarding standardized
 * [ObserverEvent] instances into the event pipeline.
 *
 * Key properties:
 *  - It performs no business logic.
 *  - It owns no triggers and never evaluates them.
 *  - It does not filter, transform, or debounce events — it simply emits them.
 *  - It forwards each produced ObserverEvent to the central [EventManager].
 *
 * Implementations:
 *  - AppTrackerObserver → emits ScreenChanged when active screen transitions occur.
 *  - TimeObserver → emits NewMinute, NewDay, etc. based on clock events.
 *
 * The method [start] must begin the observation process, typically by
 * subscribing to system signals and emitting events as they occur.
 */
interface EventSource {

    /**
     * Starts observing the underlying system component and emitting ObserverEvents.
     *
     * Implementation examples:
     *  - subscribe to navigation changes and emit ScreenChanged
     *  - subscribe to time tick and emit NewMinute/NewDay
     *
     * This method must not block; it should register observers/listeners
     * and return immediately.
     *
     * @param eventManager the central dispatcher that receives events
     *                     and coordinates trigger execution.
     */
    fun start(eventManager: EventManager)
}
