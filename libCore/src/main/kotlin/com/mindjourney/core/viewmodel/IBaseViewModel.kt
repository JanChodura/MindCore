package com.mindjourney.core.viewmodel

import com.mindjourney.core.tracking.model.CoreScreen

interface IBaseViewModel {

    val isReselectHappened: Boolean
    /**
     * Navigates forward to the given screen.
     */
    fun navigateTo(screen: CoreScreen)

    /**
     * Navigates back - to the explicitly given screen.
     */
    fun navigateBack(screen: CoreScreen)
    fun navigateBack()

    fun isActiveScreen(screen: CoreScreen): Boolean

    /**
     * Sets this an observer as primary. Others will be silent then.
     * Otherwise, triggers may be observed multiple times if multiple ViewModels are active.
     */
    fun markPrimary()

    /**
     * Called when the ViewModel is fully ready for interaction.
     * Override this method to perform any setup that requires the ViewModel to be fully initialized.
     * For example it waits for initialization of trigger observers.
     */
    fun onViewModelReady()
}
