package com.mindjourney.core.tracking

import com.mindjourney.core.logger.LoggerProvider
import com.mindjourney.core.tracking.events.ActiveScreenEventBus
import com.mindjourney.core.tracking.events.ScreenReselectDetector
import com.mindjourney.core.tracking.model.CoreScreen
import com.mindjourney.core.tracking.state.HistoryScreenHolder
import com.mindjourney.core.tracking.state.NavigationReadinessTracker
import com.mindjourney.core.tracking.state.ScreenStateHolder
import com.mindjourney.core.util.logging.injectedLogger
import com.mindjourney.core.util.logging.on
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

    private val log = injectedLogger<ScreenTracker>(on)

    // --- Core subcomponents ---
    val state = ScreenStateHolder(startingScreen)
    val history = HistoryScreenHolder()
    val readiness = NavigationReadinessTracker()
    val reselect = ScreenReselectDetector()
    val bus = ActiveScreenEventBus()

    // --- Convenience accessors ---
    val activeScreen get() = state.activeScreen

    // --- API ---

    /**
     * Sets the active screen and emits events as needed.
     *
     * If the new screen is the same as the current one,
     * it triggers reselection handling instead.
     *
     * @param screen The [CoreScreen] to set as active.
     * @param first Indicates if the the first screen from history will be taken.
     */
    fun setActiveScreen(screen: CoreScreen, first: Boolean = false) {
        val previous = getPrevious(first)
        val isReselect = previous == screen && history.screens.isNotEmpty()

        if (isReselect && state.activeScreenChangeCounter.value.changeCount == 0) {
            log.d("Small hack because of an trigger invoked reselection or an change invoked change of flow . it was already reselect")
            return
        }

        if (isReselect) {
            log.d("Active screen is reselected")
            reselectScreen(screen)
        } else {
            addToHistory(screen)
            state.setActiveScreen(screen)
            log.d("Active screen changed from '${previous.title}' to '${screen.title}'")
        }
        bus.emitScreenChanged(screen)
    }

    private fun addToHistory(screen: CoreScreen) {
        history.add(screen)
    }

    private fun reselectScreen(screen: CoreScreen) {
        state.nextScreenChange()
        reselect.handleScreenChange(state.activeScreenChangeCounter.value)
        log.d("Re-selecting the same screen='${screen.title}'")
    }

    fun requestBack() = bus.emitBackRequested()

    private fun getPrevious(first: Boolean): CoreScreen =
        if (first)
            history.clean()
        else
            state.activeScreen.value

    companion object {
        fun empty() = ScreenTracker()
    }
}
