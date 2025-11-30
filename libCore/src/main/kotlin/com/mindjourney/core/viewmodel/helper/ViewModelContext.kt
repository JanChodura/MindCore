package com.mindjourney.core.viewmodel.helper

import com.mindjourney.core.navigation.NavigationDispatcher
import com.mindjourney.core.observer.AppObserver
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
    val screenTracker: ScreenTracker,
    val observer: AppObserver,
    val triggersContext: List<TriggerContext>
)
