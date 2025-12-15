package com.mindjourney.core.tracking.state

import com.mindjourney.core.logger.helper.injectedLogger
import com.mindjourney.core.logger.service.off
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Tracks readiness of the app’s **navigation** and **data** layers.
 *
 * ---
 * ### Purpose
 * `NavigationReadinessTracker` ensures that dependent components
 * (e.g. triggers, ViewModels, observers) only start once both
 * navigation and data layers are initialized.
 *
 * ---
 * ### State Model
 * - **NavHost ready** → set via [setNavHostReady]
 * - **isReady** → `true` only when both flags are true
 *
 * The tracker exposes a reactive [isReady] flow,
 * allowing other layers to observe when initialization completes.
 *
 * ---
 * ### Example usage
 * ```
 * val tracker = NavigationReadinessTracker(logger)
 * tracker.setNavHostReady()
 * tracker.setDataReady()
 *
 * tracker.isReady.collect { ready ->
 *     if (ready) startTriggers()
 * }
 * ```
 */
class NavigationReadinessTracker {

    private val log = injectedLogger<NavigationReadinessTracker>(off)

    // Internal state flags
    private val _isNavHostReady = MutableStateFlow(false)

    private val _isReady = MutableStateFlow(false)

    /** Combined readiness flag (true only if both NavHost and data are ready). */
    val isReady: StateFlow<Boolean> get() = _isReady

    /** Marks NavHost as ready (ignored if already true). */
    fun setNavHostReady() {
        if (!_isNavHostReady.value) {
            _isNavHostReady.value = true
            log.d("NavHost ready")
            recomputeReady()
        }
    }

    /** Resets all flags (useful for tests or full reinitialization). */
    fun reset() {
        _isNavHostReady.value = false
        _isReady.value = false
        log.d("Tracker reset to initial state")
    }

    /** Recomputes overall readiness when one of the flags changes. */
    private fun recomputeReady() {
        val ready = _isNavHostReady.value
        if (_isReady.value != ready) {
            _isReady.value = ready
            log.d("Tracker isReady=$ready")
        }
    }
}
