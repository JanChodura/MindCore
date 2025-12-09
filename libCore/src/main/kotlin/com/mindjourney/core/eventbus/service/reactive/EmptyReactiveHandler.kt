package com.mindjourney.core.eventbus.service.reactive

/**
 * No-op implementation of [IReactiveHandler].
 *
 * This handler performs no reactive processing and does not subscribe
 * to any event sources. It exists as a safe default for contexts that
 * participate in the eventbus architecture but do not require handling
 * of TriggerResults.
 *
 * All methods intentionally do nothing.
 */
object EmptyReactiveHandler : IReactiveHandler {

    /**
     * Does nothing. Called by non-reactive ViewModels to satisfy interface requirements.
     */
    override fun initialize() = Unit

    override fun clear() = Unit

}
