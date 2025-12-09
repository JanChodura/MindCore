package com.mindjourney.core.eventbus.observer.initializer

import com.mindjourney.core.eventbus.observer.FlowObserver
import com.mindjourney.core.eventbus.observer.TimeObserver
import com.mindjourney.core.eventbus.service.IEventManager
import jakarta.inject.Inject

class DomainObserverInitializer @Inject constructor(
    eventManager: IEventManager,
    flowObserver: FlowObserver,
    timeObserver: TimeObserver,
) : ObserverInitializer(eventManager, flowObserver, timeObserver), IDomainObserverInitializer
