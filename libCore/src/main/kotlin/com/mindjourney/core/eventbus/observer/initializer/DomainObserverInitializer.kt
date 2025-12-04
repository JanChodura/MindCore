package com.mindjourney.core.eventbus.observer.initializer

import com.mindjourney.core.eventbus.observer.FlowObserver
import com.mindjourney.core.eventbus.observer.TimeObserver
import com.mindjourney.core.eventbus.service.IEventManager
import javax.inject.Inject

/**
 * Initializes observers that belong to a specific domain component,
 * typically a single ViewModel.
 *
 * Unlike the global initializer, DomainObserverInitializer does not receive
 * contexts from DI. Instead, the caller (commonly ReactiveHandler) supplies
 * the ObserverContexts associated with the current DomainContext.
 *
 * This initializer simply forwards these contexts to the base
 * ObserverInitializer, which activates the correct observer implementation
 * for each context. No lifecycle or readiness logic is performed here.
 *
 * Domain observers are therefore isolated, short-lived, and bound to a
 * specific feature or screen, without affecting global application behavior.
 */
class DomainObserverInitializer @Inject constructor(
    eventManager: IEventManager,
    flowObserver: FlowObserver,
    timeObserver: TimeObserver,
) : ObserverInitializer(eventManager, flowObserver, timeObserver)
