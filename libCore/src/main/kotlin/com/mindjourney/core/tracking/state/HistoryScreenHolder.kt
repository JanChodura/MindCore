package com.mindjourney.core.tracking.state

import com.mindjourney.core.tracking.model.CoreScreen

/**
 * Holds a simple non-reactive history of visited screens.
 * Used by ScreenTracker to store navigation history.
 *
 * The list is append-only except for removals via [removeLast] or [extractToFirst].
 */
class HistoryScreenHolder {

    /** Ordered list of visited screens. */
    val screens: MutableList<CoreScreen> = mutableListOf()

    /** Adds a new screen to the history. */
    fun add(screen: CoreScreen) {
        screens += screen
    }

}
