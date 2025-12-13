package com.mindjourney.core.eventbus.service.reactive.usecase

import kotlinx.coroutines.flow.Flow

/**
 * Represents a ViewModel-defined domain UseCase whose execution
 * is conditionally triggered by a reactive signal.
 *
 * This interface models a *declarative asynchronous UseCase*:
 *
 * - The UseCase declares:
 *   - WHAT should happen (domain action)
 *   - WHEN it is allowed to happen (reactive Flow condition)
 *
 * - The runtime layer (ReactiveHandler):
 *   - owns the CoroutineScope
 *   - collects the provided Flow
 *   - executes the callback when the Flow emits
 *   - manages lifecycle and cancellation
 *
 * IMPORTANT:
 * - The UseCase does NOT launch coroutines
 * - The UseCase does NOT collect Flows
 * - The UseCase does NOT know or care about CoroutineScope or lifecycle
 *
 * Responsibility split:
 *
 * - UseCase:
 *   - defines domain intent
 *   - exposes a reactive trigger condition as Flow
 *   - exposes the domain action as an opaque callback
 *
 * - ReactiveHandler:
 *   - collects the Flow in its own scope
 *   - decides when to invoke the callback
 *   - ensures proper cancellation and lifecycle handling
 *
 * Execution flow:
 *
 *     TriggerResult
 *        ↓
 *   ICallbackUseCaseAsync
 *        ↓
 *   reactiveFlow (Flow emits)
 *        ↓
 *   ReactiveHandler (scope.launch { collect })
 *        ↓
 *   callback invoked
 *
 * This design enables:
 * - clean separation of domain logic and runtime concerns
 * - per-UseCase reactive timing conditions
 * - deterministic and testable async behavior
 */
interface ICallbackUseCaseAsync {

    /**
     * Reactive condition that determines *when* the UseCase
     * is allowed to execute.
     *
     * Each emission represents a signal that the domain action
     * may be performed.
     *
     * Typical examples:
     * - entries.first { it.isNotEmpty() }
     * - isLoaded.filter { it }
     * - phase.filter { it == READY }
     */
    val reactiveFlow: Flow<Unit>

    /**
     * Returns the domain action to be executed once the reactive
     * condition is satisfied.
     *
     * The returned callback:
     * - MUST be fast and non-blocking
     * - MUST NOT suspend
     * - MUST NOT launch coroutines
     *
     * It represents pure domain logic executed by the runtime.
     */
    fun asCallback(): () -> Unit
}
