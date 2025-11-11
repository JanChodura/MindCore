package com.mindjourney.core.tracking.events

import com.mindjourney.core.tracking.model.ScreenChangeCounter
import com.mindjourney.core.util.logging.ILogger
import com.mindjourney.core.util.logging.injectedLogger
import com.mindjourney.core.util.logging.on

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
 * ### Logic
 * - When [changeCount] > 0, reselection is considered to have happened.
 * - Otherwise, it's the first activation of the screen.
 *
 */
class ScreenReselectDetector(
    private val logger: ILogger
) {

    private val log = injectedLogger<ScreenReselectDetector>(logger, on)

    private var _isReselectHappened = false
    val isReselectHappened: Boolean get() = _isReselectHappened

    /**
     * Evaluates whether reselection occurred for the current screen based on [counter].
     */
    fun handleScreenChange(counter: ScreenChangeCounter): Boolean {
        _isReselectHappened = counter.changeCount > 0
        if (_isReselectHappened) {
            log.d("Reselection detected for screen=${counter.screen.title}, count=${counter.changeCount}")
        }
        return _isReselectHappened
    }
}
