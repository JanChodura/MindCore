package com.mindjourney.core.observer.trigger.model

import kotlinx.coroutines.flow.Flow

/**
 * Minimal base class for triggers driven by a reactive data source (`Flow`).
 *
 * This abstraction defines only *what the trigger listens to*, not *how it starts*.
 * Execution is fully coordinated externally by `TriggerInitializer`, which
 * subscribes to `sourceFlow` and invokes `tryExecute()` on each emission.
 *
 * Use this class when implementing triggers whose evaluation should occur
 * in response to upstream events. For polling-based triggers, implement
 * `IPollingTrigger` instead.
 *
 * Responsibilities:
 * - expose a `Flow<T>` that produces evaluation impulses
 * - implement `tryExecute()` from `IAppTrigger`
 *
 * Not responsible for:
 * - collecting or starting the flow
 * - lifecycle management
 * - emitting results
 *
 * `ReactiveTrigger` provides a richer implementation on top of this base.
 *
 * @param T Type of values emitted by the upstream reactive source.
 */
abstract class FlowBasedTrigger<T> : IAppTrigger {
    abstract val sourceFlow: Flow<T>
}
