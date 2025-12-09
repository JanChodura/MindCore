package com.mindjourney.core.eventbus.service.reactive.usecase

/**
 * Represents a ViewModel-defined domain UseCase whose execution is exposed
 * as a suspending callback function. This interface is used when the domain
 * action requires coroutine-based work (database, IO, delay, flows, etc.).
 *
 * The ViewModel constructs the UseCase logic, the Consumer decides *when*
 * it should run, and the Performer is responsible only for invoking the
 * provided suspending callback.
 *
 * Execution flow:
 *
 *     ViewModel → ICallbackUseCaseSuspend → Consumer → Performer → suspend callback() → ViewModel
 *
 * This design keeps all domain rules inside the ViewModel-owned UseCase logic
 * while the rest of the event pipeline (Observers, Triggers, Consumers,
 * Performers) remains fully decoupled from business logic.
 */
interface ICallbackUseCase {

    /**
     * Returns a suspending callback representing the entire domain action.
     *
     * Performers must treat the callback as opaque: their only responsibility
     * is to invoke it. All business logic lives inside this suspending lambda.
     *
     * @return A suspending function encapsulating the domain operation.
     */
    fun asCallback(): suspend () -> Unit
}
