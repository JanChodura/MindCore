package com.mindjourney.core.viewmodel.helper

import com.mindjourney.core.navigation.NavigationDispatcher
import com.mindjourney.core.observer.IAppScreenObserver
import com.mindjourney.core.observer.trigger.util.TriggerContext
import com.mindjourney.core.tracking.ScreenTracker
import javax.inject.Singleton

/**
 * Provides shared dependencies commonly required by multiple ViewModels.
 * This context avoids repetitive constructor parameters and centralizes
 * access to core services used throughout the app's presentation layer.
 */
@Singleton
class ViewModelContext(
    val navigation: NavigationDispatcher,
    /**
     * Whether this ViewModel instance is currently the primary observer holder.
     *
     * Only the primary ViewModel:
     * - starts observing triggers
     * - reacts to global lifecycle conditions
     * - owns reaction pipelines
     *
     * This avoids duplicate collectors when multiple ViewModels exist
     * inside the same navigation host.
     */
    var isPrimary: Boolean,
    var source: String = "unknown",
    val screenTracker: ScreenTracker,
    val observer: IAppScreenObserver,
    val triggersContext: List<TriggerContext>
)
