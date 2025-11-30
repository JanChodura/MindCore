package com.mindjourney.core.viewmodel

import com.mindjourney.core.tracking.model.CoreScreen

interface IBaseViewModel {

    val isReselectHappened: Boolean

    /**
     * Indicates whether this ViewModelContext is currently designated as the primary observer holder.
     * ViewModel can call setAsPrimary() to start observing triggers.
     */
    val isPrimary: Boolean

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
     * Called when the ViewModel is fully ready for interaction.
     * Override this method to perform any setup that requires the ViewModel to be fully initialized.
     * For example it waits for initialization of trigger observers.
     */
    fun onViewModelInitialized()
}
