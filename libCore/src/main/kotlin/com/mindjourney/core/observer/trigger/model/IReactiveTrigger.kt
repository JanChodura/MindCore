package com.mindjourney.core.observer.trigger.model

/**
 * Specialization of [IAppTrigger] for triggers driven by reactive data streams.
 *
 * A reactive trigger does not emit its result directly into TriggerManager.
 * Instead, every non-empty TriggerResult is forwarded through a callback
 * provided by TriggerInitializer.
 *
 * This means:
 * - the trigger is driven purely by upstream Flow emissions,
 * - each emission invokes tryExecute(),
 * - any meaningful TriggerResult is delivered via `onResult(result)`,
 * - result propagation requires an active TriggerResultConsumer,
 *   otherwise the emitted result will not reach the manager.
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
