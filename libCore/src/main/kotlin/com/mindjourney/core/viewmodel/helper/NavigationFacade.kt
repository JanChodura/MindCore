package com.mindjourney.core.viewmodel.helper

import com.mindjourney.core.tracking.model.CoreScreen
import dagger.hilt.android.scopes.ViewModelScoped
import jakarta.inject.Inject

@ViewModelScoped
class NavigationFacade @Inject constructor(
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
