package com.mindjourney.core.viewmodel.helper

import com.mindjourney.core.eventbus.model.trigger.context.TriggerContext
import com.mindjourney.core.eventbus.observer.EventSource
import com.mindjourney.core.navigation.NavigationDispatcher
import com.mindjourney.core.tracking.ScreenTracker
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Shared dependency holder for a specific ViewModel instance.
 *
 * This context centralizes cross-cutting services used by presentation logic,
 * so individual ViewModels do not need to request and duplicate the same
 * constructor dependencies.
 *
 * ### Responsibilities
 * - provides access to navigation, tracking and screen observer tools
 * - tracks the origin/source of ViewModel creation (debugging, analytics)
 * - stores and manages **trigger context** used by the reactive initialization layer
 *
 * Unlike a global singleton, this context is scoped **per ViewModel instance**
 * to ensure that triggers, tracking state and observation flows remain
 * independent between screens and avoid unwanted state leaks.
 *
 * @property navigation Dispatcher for screen navigation requests.
 * @property screenTracker Tracks screen visit/activity analytics for this ViewModel.
 * @property observer Lifecycle-driven observer dispatcher for screen events.
 * @property source Optional label describing where the ViewModel originated from
 *                  (useful for analytics/debug overlays).
 */
@ViewModelScoped
class ViewModelContext(
    val navigation: NavigationDispatcher,
    val screenTracker: ScreenTracker,
    val observer: EventSource,
    var source: String = "unknown",
) {

    /**
     * Backing storage of trigger metadata.
     *
     * Mutable privately, but exposed as immutable list to consumers.
     */
    private val _triggersContext: MutableList<TriggerContext> = mutableListOf()

    /**
     * Public read-only view of active trigger contexts.
     *
     * Reactive handlers use this value to attach observers and
     * propagate lifecycle states, but cannot modify the list directly.
     */
    val triggersContext: List<TriggerContext>
        get() = _triggersContext

    /**
     * Adds a new trigger context to this ViewModel scope.
     * Typically invoked during screen creation or when dynamic
     * feature boundaries initialize new reactive components.
     */
    fun addTrigger(trigger: TriggerContext) {
        _triggersContext.add(trigger)
    }

    /**
     * Replaces all trigger contexts with a new list.
     *
     * Useful for hot-reload scenarios, screen re-entry, or
     * when the ViewModel is injected with reactive definitions
     * after construction.
     */
    fun replaceTriggers(newTriggers: List<TriggerContext>) {
        _triggersContext.clear()
        _triggersContext.addAll(newTriggers)
    }
}
