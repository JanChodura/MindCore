package com.mindjourney.core.viewmodel.helper

import com.mindjourney.core.eventbus.model.event.context.ObserverContext
import com.mindjourney.core.eventbus.model.trigger.context.TriggerContext
import com.mindjourney.core.tracking.ScreenTracker

/**
 * A lightweight domain-scoped container holding navigation, screen tracking and
 * event-architecture bindings for any feature-level component (e.g., ViewModel).
 *
 * This base context contains only stable references that do not depend on Android
 * lifecycle. Platform-specific extensions (such as a Hilt @ViewModelScoped
 * ViewModelContext) may inherit from this class and attach ViewModel-specific data
 * like ViewModelType or lifecycle flows.
 *
 * Dynamic event bindings (observer contexts, trigger contexts) are populated
 * later by the ReactiveHandler. DomainContext never starts observers, runs triggers,
 * or performs side effects. It is a passive, immutable-by-design configuration holder,
 * except for event-binding collections which serve as a registry for ReactiveHandler.
 */
open class DomainContext(
    val navigation: INavigationFacade,
    val screenTracker: ScreenTracker,
) {
    // ---------------------------------------------------------
    // EVENT ARCHITECTURE BINDINGS (populated by ReactiveHandler)
    // ---------------------------------------------------------

    /** ViewModel-scoped observer contexts */
    internal val _observerContexts = mutableListOf<ObserverContext>()
    val observerContexts: List<ObserverContext> get() = _observerContexts

    fun addObserver(context: ObserverContext) {
        _observerContexts += context
    }

    fun replaceObservers(contexts: List<ObserverContext>) {
        _observerContexts.clear()
        _observerContexts += contexts
    }

    /** ViewModel-scoped trigger contexts */
    internal val _triggerContexts = mutableListOf<TriggerContext>()
    val triggerContexts: List<TriggerContext> get() = _triggerContexts

    fun addTrigger(context: TriggerContext) {
        _triggerContexts += context
    }

    fun replaceTriggers(contexts: List<TriggerContext>) {
        _triggerContexts.clear()
        _triggerContexts += contexts
    }

    // ---------------------------------------------------------
    // LIFECYCLE + REACTIVE STATE (injected by ReactiveHandler)
    // ---------------------------------------------------------

    /**
     * Optional lifecycle state used across multiple ViewModels.
     * Again: populated externally, not managed by this class.
     */
    var lifecycleState: Any? = null
}
