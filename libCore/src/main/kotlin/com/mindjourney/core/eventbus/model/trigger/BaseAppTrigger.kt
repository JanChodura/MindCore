package com.mindjourney.core.eventbus.model.trigger

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Base implementation of IAppTrigger that provides:
 * - readiness handling
 * - description metadata
 *
 * Subclasses must implement tryExecute() and return only TriggerResultType.
 */
abstract class BaseAppTrigger : IAppTrigger {


    private val _isReady = MutableStateFlow(false)
    override val isReady: StateFlow<Boolean> = _isReady


    override lateinit var description: TriggerDescription


    /** Allows orchestration layer to enable/disable trigger. */
    fun setReady(value: Boolean) {
        _isReady.value = value
    }
}
