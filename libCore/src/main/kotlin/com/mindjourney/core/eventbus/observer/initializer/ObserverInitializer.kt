package com.mindjourney.core.eventbus.observer

import com.mindjourney.core.eventbus.model.event.context.FlowObserverContext
import com.mindjourney.core.eventbus.model.event.context.ObserverContext
import com.mindjourney.core.eventbus.model.event.context.TimeObserverContext
import com.mindjourney.core.eventbus.observer.initializer.IObserverInitializer
import com.mindjourney.core.eventbus.service.EventManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Initializes all globally scoped observers for the entire application.
 * Supports FlowObservers and TimeObservers using sealed ObserverContext.
 */
@Singleton
class ObserverInitializer @Inject constructor(
    private val eventManager: EventManager,
    private val observers: List<ObserverContext>,
    private val flowObserver: FlowObserver,
    private val timeObserver: TimeObserver
) : IObserverInitializer {

    override fun initialize() {
        observers.forEach { context ->
            when (context) {
                is FlowObserverContext<*> ->
                    flowObserver.start(eventManager, context)

                is TimeObserverContext ->
                    timeObserver.start(eventManager, context)
            }
        }
    }
}
