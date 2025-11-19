package com.mindjourney.core.tracking.state

import com.mindjourney.core.tracking.model.CoreScreen
import com.mindjourney.core.tracking.model.ScreenChangeCounter
import com.mindjourney.core.util.logging.injectedLogger
import com.mindjourney.core.util.logging.off
import com.mindjourney.core.util.logging.on
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Holds and manages the current and start screen states of the app.
 *
 * ---
 * ### Purpose
 * `ScreenStateHolder` acts as the single source of truth
 * for which [CoreScreen] is currently active in the application.
 *
 * It provides reactive [kotlinx.coroutines.flow.StateFlow] properties that notify
 * other components (e.g., triggers, observers, ViewModels)
 * whenever the user navigates between screens.
 *
 * ---
 * ### Exposed state
 * - [activeScreenChangeCounter] — the current active screen along with how many times it has been viewed before screen is changed.
 * - [activeScreenChangeCounter] — a map tracking how many times each screen has been activated during not changing screen itself.
 *
 * ---
 * ### Example usage
 * ```
 * val holder = ScreenStateHolder(CoreScreen.Unknown)
 * holder.setActiveScreen(Screen.Manifest)
 *
 * holder.activeScreen.collect { screen ->
 *     println("Current: ${screen.title}")
 * }
 * ```
 */
class ScreenStateHolder(startingScreen: CoreScreen) {
    private val log = injectedLogger<ScreenStateHolder>(off)

    private val _activeScreen: MutableStateFlow<CoreScreen> = MutableStateFlow(CoreScreen.Unknown)
    val activeScreen: StateFlow<CoreScreen> get() = _activeScreen

    private val _activeScreenChangeCounter = MutableStateFlow(ScreenChangeCounter(startingScreen, 0))
    val activeScreenChangeCounter = _activeScreenChangeCounter

    /**
     * Updates the active screen and remembers the previous one.
     */
    fun setActiveScreen(screen: CoreScreen) {
        log.d("Updating active screen from ${_activeScreen.value.title} to ${screen.title}")
        _activeScreen.value = screen
        _activeScreenChangeCounter.value = ScreenChangeCounter(screen, 0)
    }

    /**
     * Resets all stored states to initial values.
     * Intended for tests or full app reinitialization.
     */
    fun reset(startingScreen: CoreScreen = CoreScreen.Unknown) {
        _activeScreenChangeCounter.value = ScreenChangeCounter(startingScreen, 0)
        log.d("ScreenStateHolder reset to startingScreen=${startingScreen.title}")
    }

    /**
     * Increments the change count for the current active screen.
     * Called whenever the active screen is reselected.
     */
    fun nextScreenChange() {
        _activeScreenChangeCounter.value.changeCount += 1
    }

}
