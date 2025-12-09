package com.mindjourney.core.tracking.events

import com.mindjourney.core.tracking.model.CoreScreen
import com.mindjourney.core.logger.helper.injectedLogger
import com.mindjourney.core.logger.service.off
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Central event bus for broadcasting high-level screen navigation events.
 *
 * Acts as a reactive bridge between navigation components,
 * ViewModels, and triggers that respond to screen-related events.
 *
 * The bus exposes three event flows:
 *  - [screenChanges] → emitted when the active screen changes.
 *  - [backRequests] → emitted when user requests "back" navigation.
 *
 * The bus is designed to be publicly accessible through [com.mindjourney.core.tracking.ScreenTracker.bus],
 * enabling lightweight calls like:
 * ```
 * ctx.screenTracker.bus.emitScreenChanged(screen)
 * ctx.screenTracker.bus.emitReselected()
 * ```
 */
class ActiveScreenEventBus {

    private val log = injectedLogger<ActiveScreenEventBus>(off)

    private val _screenChanges = MutableSharedFlow<CoreScreen>(extraBufferCapacity = 1)
    val screenChanges: SharedFlow<CoreScreen> get() = _screenChanges.asSharedFlow()

    private val _backRequests = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val backRequests: SharedFlow<Unit> get() = _backRequests.asSharedFlow()


    // --- Emitters (called externally via tracker.bus) ---
    fun emitScreenChanged(screen: CoreScreen) {
        log.d("Screen change:${screen.title}")
        _screenChanges.tryEmit(screen)
    }

    fun emitBackRequested() {
        log.d("Back requested")
        _backRequests.tryEmit(Unit)
    }

}
