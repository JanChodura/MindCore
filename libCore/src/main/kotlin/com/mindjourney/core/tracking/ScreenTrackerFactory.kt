package com.mindjourney.core.tracking

/**
 * Factory for creating instances of [ScreenTracker]. When app is started or in preview/test.
 */
object ScreenTrackerFactory {
    /**
     * Creates an empty/no-op instance for preview or test environments.
     */
    fun empty(): ScreenTracker {
        return ScreenTracker()
    }
}
