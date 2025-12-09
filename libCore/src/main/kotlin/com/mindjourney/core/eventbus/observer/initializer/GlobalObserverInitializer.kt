package com.mindjourney.core.eventbus.observer.initializer

import com.mindjourney.core.di.CoreQualifier
import com.mindjourney.core.eventbus.model.event.context.ObserverContext
import com.mindjourney.core.eventbus.observer.FlowObserver
import com.mindjourney.core.eventbus.observer.TimeObserver
import com.mindjourney.core.eventbus.service.IEventManager
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class GlobalObserverInitializer @Inject constructor(
    @param:CoreQualifier.Global private val contexts: List<@JvmSuppressWildcards ObserverContext>,
    private val eventManager: IEventManager,
    private val flowObserver: FlowObserver,
    private val timeObserver: TimeObserver,
) : ObserverInitializer(eventManager, flowObserver, timeObserver), IGlobalObserverInitializer {


    /** Activates all global observers. */
    override fun initialize() {
        initialize(contexts)
    }
}
