package com.mindjourney.core.viewmodel.helper

import com.mindjourney.core.tracking.model.CoreScreen

/**
 * A lightweight façade for navigation commands used by ViewModels.
 *
 * ViewModel never communicates with navigation coordinator or dispatcher directly.
 * Instead, it uses this abstraction to:
 * - request navigation to a screen,
 * - request back navigation,
 * - check whether a screen is currently active.
 *
 * This keeps the ViewModel independent from the actual navigation framework
 * and ensures consistent navigation behavior across modules.
 *
 * The façade intentionally contains only high-level actions without any logic.
 * All real navigation work is handled inside the navigation layer.
 */
interface INavigationFacade {

    /** Navigates to the given screen. */
    fun navigateTo(screen: CoreScreen)

    /** Performs back navigation (pop). */
    fun navigateBack()

    /**
     * Returns true if the given screen is currently active.
     * Useful for avoiding redundant navigations or preventing double-triggered actions.
     */
    fun isActive(screen: CoreScreen): Boolean
}
