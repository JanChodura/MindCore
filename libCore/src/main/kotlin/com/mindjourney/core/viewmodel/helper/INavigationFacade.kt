package com.mindjourney.core.viewmodel.helper

import com.mindjourney.core.tracking.model.CoreScreen

interface INavigationFacade {
    fun navigateTo(screen: CoreScreen)
    fun navigateBack()
    fun isActive(screen: CoreScreen): Boolean
}
