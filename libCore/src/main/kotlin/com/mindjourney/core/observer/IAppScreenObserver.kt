package com.mindjourney.core.observer

import com.mindjourney.core.observer.trigger.util.TriggerContext
import kotlinx.coroutines.flow.StateFlow

/**
 * Defines a contract for components that observe app-level lifecycle
 * and trigger conditions such as new day start, screen change, etc.
 */
interface IAppScreenObserver {

    /**
     * Initializes the observer with provided trigger contexts.
     *
     * @param triggersFlow A [StateFlow] providing active triggers.
     */
    fun init(triggersFlow: StateFlow<List<TriggerContext>>)
}
