package com.mindjourney.core.viewmodel

import com.mindjourney.core.viewmodel.helper.INavigationFacade

/**
 * Base interface for all ViewModels in the app.
 *
 * ViewModel in this architecture is not just a passive data holder.
 * It acts as a **reaction layer** between the UI world (screen changes,
 * readiness signals, triggers, navigation events) and the domain orchestrators.
 *
 * Responsibilities of ViewModel:
 * - Listens to screen lifecycle (via ViewModelContext & ScreenTracker)
 * - Controls ownership of observer subscriptions (`isPrimary`)
 * - Initializes trigger observers and other reactive components
 * - Exposes navigation through a façade (`navigationFcd`)
 * - Provides a safe point (`onViewModelInitialized`) where all reactive
 *   dependencies are ready and domain orchestrators can be called
 *
 * ViewModel **does not contain domain logic**.
 * Domain operations are delegated to orchestrators.
 */
interface IBaseViewModel {

    /**
     * Navigation façade providing high-level navigation commands.
     * ViewModel never calls NavigationDispatcher directly.
     */
    val navigationFcd: INavigationFacade

    /**
     * Called when the ViewModel is fully ready for interaction.
     *
     * This moment occurs only after:
     * - the ViewModelContext is attached,
     * - primary status is resolved,
     * - trigger observers are initialized,
     * - screen observers are active.
     *
     * Override this method to start domain orchestration or to run
     * any initialization that requires all reactive layers to be ready.
     */
    fun onViewModelInitialized()
}
