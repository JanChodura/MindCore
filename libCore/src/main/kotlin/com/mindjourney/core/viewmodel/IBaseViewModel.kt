package com.mindjourney.core.viewmodel

import com.mindjourney.core.tracking.model.CoreScreen
import com.mindjourney.core.viewmodel.helper.INavigationFacade

interface IBaseViewModel {

    val isReselectHappened: Boolean

    /**
     * Indicates whether this ViewModelContext is currently designated as the primary observer holder.
     * ViewModel can call setAsPrimary() to start observing triggers.
     */
    val isPrimary: Boolean

    val navigationFcd: INavigationFacade

    /**
     * Called when the ViewModel is fully ready for interaction.
     * Override this method to perform any setup that requires the ViewModel to be fully initialized.
     * For example it waits for initialization of trigger observers.
     */
    fun onViewModelInitialized()
}
