package com.mindjourney.core.tracking

import com.mindjourney.core.tracking.events.ActiveScreenEventBus
import com.mindjourney.core.tracking.events.ScreenReselectDetector
import com.mindjourney.core.tracking.model.CoreScreen
import com.mindjourney.core.tracking.state.HistoryScreenHolder
import com.mindjourney.core.tracking.state.NavigationReadinessTracker
import com.mindjourney.core.tracking.state.ScreenStateHolder
import com.mindjourney.core.util.logging.injectedLogger
import com.mindjourney.core.util.logging.off
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Central orchestrator for managing **screen-related state and events** across the app.
 *
 * This class links navigation, data readiness, and trigger reactions.
 * It delegates logic to four lightweight, testable subcomponents:
 * - [state][com.mindjourney.core.tracking.state.ScreenStateHolder] — holds current and previous screens.
 * - [readiness][com.mindjourney.core.tracking.state.NavigationReadinessTracker] — tracks when app layers are ready.
 * - [reselect][com.mindjourney.core.tracking.events.ScreenReselectDetector] — handles reselection of the screen logic.
 * - [bus][com.mindjourney.core.tracking.events.ActiveScreenEventBus] — emits navigation events as flows.
 *
 * For a detailed lifecycle, event flow, and integration guide,
 * see **ActiveScreenTrackerFlow.txt** in the project documentation.
 *
 * Example usage:
 * ```kotlin
 * ctx.screenTracker.state.setActiveScreen(Screen.Convictions)
 * ctx.screenTracker.bus.emitReselected()
 * ctx.screenTracker.readiness.setDataReady()
 * ```
 */
@Singleton
class ScreenTracker @Inject constructor(
    private val startingScreen: CoreScreen = CoreScreen.Unknown,
    private val fromRoute: (String?) -> CoreScreen = { CoreScreen.Unknown }
) {

    private val log = injectedLogger<ScreenTracker>(off)

    // --- Core subcomponents ---
    val state = ScreenStateHolder(startingScreen)
    val history = HistoryScreenHolder()
    val readiness = NavigationReadinessTracker()
    val reselect = ScreenReselectDetector()
    val bus = ActiveScreenEventBus()

    // --- Convenience accessors ---
    val activeScreen get() = state.activeScreen


    /**
     * Sets the active screen and emits events as needed.
     *
     * If the new screen is the same as the current one,
     * it triggers reselection handling instead.
     *
     * @param screen The [CoreScreen] to set as active.
     */
    fun setActiveScreen(screen: CoreScreen) {
        val previous = getPrevious()
        val isReselectable = screen == previous
        if (isReselectable) {
            log.d("Active screen is reselected")
            reselectScreen(screen)
            return
        }
        state.setActiveScreen(screen)
        addToHistory(screen)
        log.d("Active screen changed from '${previous.title}' to '${screen.title}'")
        bus.emitScreenChanged(screen)

    }

    private fun addToHistory(screen: CoreScreen) {
        log.d("Adding screen='${screen.title}' to history", off)
        history.add(screen)
    }

    private fun reselectScreen(screen: CoreScreen) {
        state.nextScreenChange()
        reselect.handleScreenChange()
        log.d("Re-selecting the same screen='${screen.title}'")
    }

    fun requestBack() = bus.emitBackRequested()

    private fun getPrevious(): CoreScreen =
        if (history.screens.isEmpty())
            CoreScreen.Unknown
        else
            history.screens.last()

    companion object {
        fun empty() = ScreenTracker()
    }
}
