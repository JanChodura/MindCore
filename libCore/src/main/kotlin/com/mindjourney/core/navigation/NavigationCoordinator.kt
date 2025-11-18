package com.mindjourney.core.navigation

import com.mindjourney.core.logger.LoggerProvider
import com.mindjourney.core.tracking.ActiveScreenTrackerFactory
import com.mindjourney.core.tracking.ScreenTracker
import com.mindjourney.core.tracking.model.CoreScreen
import com.mindjourney.core.util.logging.injectedLogger
import com.mindjourney.core.util.logging.off
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationCoordinator @Inject constructor(
    private val screenTracker: ScreenTracker,
) : NavigationDispatcher {

    private val log = injectedLogger<NavigationCoordinator>(LoggerProvider.get(), off)

    companion object {
        fun empty() = NavigationCoordinator(ActiveScreenTrackerFactory.empty())
    }

    override fun goTo(screen: CoreScreen) {
        log.d("goTo: ${screen.route}")
        screenTracker.setActiveScreen(screen)
    }

    override fun goBackTo(screen: CoreScreen) {
        screenTracker.setActiveScreen(screen)
    }

    override fun goBack() {
        screenTracker.requestBack()
    }
}
