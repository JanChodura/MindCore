package com.mindjourney.core.navigation

import com.mindjourney.core.tracking.model.CoreScreen

interface NavigationDispatcher {
    fun goTo(screen: CoreScreen)
    fun goBackTo(screen: CoreScreen)

    fun goBack()
}
