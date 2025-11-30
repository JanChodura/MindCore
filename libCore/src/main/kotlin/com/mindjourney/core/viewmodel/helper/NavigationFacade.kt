package com.mindjourney.core.viewmodel.helper

import com.mindjourney.core.tracking.model.CoreScreen

class NavigationFacade(
    private val ctx: ViewModelContext
): INavigationFacade {

    override fun navigateTo(screen: CoreScreen) {
        ctx.navigation.goTo(screen)
    }

    override fun navigateBack() {
        ctx.navigation.goBack()
    }

    override fun isActive(screen: CoreScreen): Boolean {
        return ctx.screenTracker.activeScreen.value == screen
    }
}
