package com.mindjourney.core.tracking.events

import com.mindjourney.core.tracking.model.ScreenChangeCounter
import com.mindjourney.core.logger.helper.injectedLogger
import com.mindjourney.core.logger.service.off
import com.mindjourney.core.logger.service.on
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Determines whether the current screen was reselected based on its [ScreenChangeCounter].
 *
 * ---
 * ### Role in architecture
 * - Used by [com.mindjourney.core.tracking.ScreenTracker] to decide
 *   whether reselection actions (like "scroll to top" or "reset pager") should fire.
 * - The controller itself does not emit events — it just interprets counter data.
 *
 * ---
 */
class ScreenReselectDetector() {

    private val log = injectedLogger<ScreenReselectDetector>(off)

    private var _isReselectHappened = MutableStateFlow<Int>(0)
    val isReselectHappened: StateFlow<Int> = _isReselectHappened

    fun handleScreenChange() {
        val oldValue = _isReselectHappened.value
        _isReselectHappened.value = oldValue + 1
        log.d("Reselection detected for: ${_isReselectHappened.value}")
    }
}
