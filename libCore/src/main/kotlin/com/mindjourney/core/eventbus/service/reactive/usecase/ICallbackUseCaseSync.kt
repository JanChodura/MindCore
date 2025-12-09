package com.mindjourney.core.eventbus.service.reactive.usecase

/**
 * Represents a ViewModel-defined domain UseCase whose execution is exposed
 * purely as a callback function. This interface is used in the event/trigger
 * pipeline where the ViewModel prepares a domain action, the Consumer decides
 * *when* it should run, and the Performer is responsible for executing it.
 *
 * The ICallbackUseCase encapsulates the entire domain logic of an operation
 * and exposes it through [asCallback]. Performers and Consumers never interpret
 * the logic contained inside — they only invoke the callback once triggered.
 *
 * Execution flow:
 *
 *     ViewModel → ICallbackUseCase → Consumer → Performer → callback() → ViewModel
 *
 * This keeps domain logic strongly owned by the ViewModel while the rest of
 * the architecture (Observers, Triggers, Consumers, Performers) remains clean
 * and agnostic to the actual business rules.
 */
interface ICallbackUseCaseSync {

    /**
     * Returns the callback representing the full domain action to be executed.
     *
     * The returned function contains the complete business logic of this
     * UseCase. Once invoked by a Performer, it should execute the entire
     * domain operation. Consumers and Performers must treat the callback as
     * opaque and must not interpret or modify its meaning.
     *
     * @return A function encapsulating the actual domain logic.
     */
    fun asCallback(): () -> Unit
}
