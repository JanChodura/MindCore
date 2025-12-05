package com.mindjourney.core.eventbus.model.trigger

import com.mindjourney.core.eventbus.model.trigger.result.TriggerResultType
import kotlinx.coroutines.flow.StateFlow

/**
 * Defines the contract for all triggers in the observer → event → trigger pipeline.
 *
 * A trigger represents the *pure domain reaction* to exactly one ObserverEvent type.
 * The binding between the event type and the trigger is declared externally
 * in {@link TriggerContext}, not inside the trigger itself.
 *
 * ### Architectural rules
 *  - A trigger contains **only** business logic.
 *  - A trigger MUST NOT:
 *      * observe flows
 *      * start coroutines
 *      * schedule work
 *      * depend on Android lifecycle or UI
 *  - The EventManager invokes the trigger, processes its result, and delivers
 *    a full TriggerResult to the result-consumer layer.
 *
 * ### Execution result
 * The trigger returns a {@link TriggerResultType}, the unified result model
 * of the system. To support domain-specific effects, triggers use:
 *
 *     TriggerResultType.Custom(payload)
 *
 * where `payload` is a domain-defined object (sealed class, enum, data class…).
 *
 * This design keeps the core architecture stable while allowing unlimited
 * extension at the feature level.
 *
 * ### Completion signalling (`isCompleted`)
 * Each trigger exposes an immutable `isCompleted: StateFlow<Boolean>`.
 *
 * This flag is controlled by the trigger implementation (typically inside
 * `tryExecute`) and indicates that the trigger has *fully finished* its work.
 *
 * The completion state enables:
 *  - **trigger chaining** (e.g. follow-up observers watching for completion)
 *  - **domain pipelines** composed from multiple triggers
 *  - **reactive orchestration** without coupling triggers together
 *
 * Implementations SHOULD set `isCompleted` to true when the trigger
 * has produced (or deliberately chosen not to produce) its result.
 *
 * ### Description
 * Each trigger provides a human-readable {@link TriggerDescription}, used
 * for diagnostics, logging, and debugging.
 */
interface IAppTrigger {

    /**
     * Executes the trigger's domain logic and produces a {@link TriggerResultType}.
     *
     * The returned value represents only the outcome of the domain evaluation.
     * The EventManager wraps this into a full TriggerResult, adding metadata
     * such as timestamp, triggering event, and description.
     */
    suspend fun tryExecute(): TriggerResultType

    /**
     * Reactive completion signal for this trigger.
     *
     * `true` means the trigger has finished its work. This is typically set
     * inside `tryExecute` by the concrete implementation.
     *
     * External systems (observers, orchestrators, workflows) may watch this
     * flag to perform follow-up actions or chain multiple triggers in sequence.
     */
    val isCompleted: StateFlow<Boolean>

    /**
     * Metadata describing this trigger for logs, reports, or developer tools.
     */
    var description: TriggerDescription
}
