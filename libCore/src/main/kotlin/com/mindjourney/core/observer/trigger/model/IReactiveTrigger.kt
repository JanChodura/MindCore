package com.mindjourney.core.observer.trigger.model

/**
 * Specialization of [IAppTrigger] for triggers driven by reactive data streams.
 *
 * A reactive trigger does not decide when it evaluates — instead,
 * it listens to an upstream event source (Flow, LiveData, external updates)
 * and executes its logic whenever new data arrives.
 *
 * This makes it suitable for:
 *  - observing domain/state change streams
 *  - reacting to repository or system notifications
 *  - UI / lifecycle / push events
 *
 * Unlike polling triggers, reactive triggers never schedule themselves —
 * they only bind to an external stream and let it drive evaluation.
 */
interface IReactiveTrigger : IAppTrigger {

    /**
     * Starts observing the underlying reactive source.
     *
     * Concrete implementations:
     *  - subscribe to the stream
     *  - wait for incoming events
     *  - invoke [tryExecute] on each emission
     *  - forward non-empty [TriggerResult] values to `onResult`
     *
     * @param scope Coroutine scope driving observation.
     * @param description Human-readable metadata for logging and debugging.
     * @param onResult Callback receiving meaningful trigger output.
     */
    fun startReactiveFlow(
        scope: kotlinx.coroutines.CoroutineScope,
        description: TriggerDescription,
        onResult: (TriggerResult) -> Unit
    )
}
