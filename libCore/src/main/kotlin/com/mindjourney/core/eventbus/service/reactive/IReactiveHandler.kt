package com.mindjourney.core.eventbus.service.reactive

/**
 * ReactiveHandler is a ViewModel-scoped orchestrator that receives
 * TriggerResults from the global TriggerResultBus and executes the
 * appropriate ViewModel-defined UseCase callbacks.
 *
 * Responsibilities:
 *  - Subscribe to the global TriggerResultBus when initialize() is called.
 *  - For each incoming TriggerResult, route execution to all relevant
 *    TriggerResultConsumers (global + ViewModel-specific).
 *  - Execute synchronous UseCases immediately on the caller thread.
 *  - Execute asynchronous UseCases inside the ViewModel’s CoroutineScope.
 *  - Track all running jobs and cancel them in clear(), which must be
 *    called from ViewModel.onCleared().
 *
 * Non-responsibilities (critical for architectural clarity):
 *  - No business logic (UseCases contain the domain logic).
 *  - No trigger logic (EventManager + Triggers handle that).
 *  - No observer setup (handled elsewhere).
 *  - No UI logic (ViewModel owns state and UI updates).
 *
 * In short: ReactiveHandler is the runtime executor for TriggerResult
 * delivery and UseCase invocation within a specific ViewModel lifecycle.
 */
interface IReactiveHandler {

    /**
     * Starts collecting results from TriggerResultBus and activates
     * all internal routing mechanisms. Must be called exactly once
     * from the owning ViewModel during initialization.
     */
    fun initialize()

    /**
     * Cancels all running async jobs and releases resources. Must be
     * called from ViewModel.onCleared() to avoid leaks.
     */
    fun clear()
}
