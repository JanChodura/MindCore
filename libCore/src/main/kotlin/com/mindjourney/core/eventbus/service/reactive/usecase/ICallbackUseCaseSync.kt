package com.mindjourney.core.eventbus.service.reactive.usecase

/**
 * Represents a ViewModel-defined domain UseCase whose execution is exposed
 * as a synchronous callback.
 *
 * This interface is intended for UseCases whose execution:
 * - does NOT require coroutine suspension
 * - does NOT perform long-running or blocking operations
 * - performs immediate state changes or UI-related logic
 *
 * The callback returned by this UseCase is executed directly by the
 * ReactiveHandler without being wrapped in a CoroutineScope.
 *
 * Responsibilities split:
 * - UseCase:
 *   - owns and defines the complete domain logic
 *   - exposes it as an opaque synchronous callback
 *   - knows nothing about coroutines or lifecycle
 *
 * - ReactiveHandler:
 *   - decides *when* the UseCase is executed
 *   - invokes the callback directly on the current thread
 *
 * Execution flow:
 *
 *     ViewModel
 *        ↓
 *   ICallbackUseCaseSync
 *        ↓
 *     Consumer
 *        ↓
 *   ReactiveHandler (callback())
 *        ↓
 *     Domain logic executed immediately
 *
 * This design is suitable for lightweight, deterministic actions such as
 * navigation state updates, flag toggles, or in-memory state transitions.
 */
interface ICallbackUseCaseSync {

    /**
     * Returns the callback representing the full synchronous domain action.
     *
     * The returned function encapsulates the entire business operation.
     * It MUST complete quickly and MUST NOT perform blocking or suspending work.
     *
     * Consumers and Performers must treat the callback as opaque and
     * must not interpret or modify its behavior.
     *
     * @return A callback containing the complete synchronous domain logic.
     */
    fun asCallback(): () -> Unit
}
