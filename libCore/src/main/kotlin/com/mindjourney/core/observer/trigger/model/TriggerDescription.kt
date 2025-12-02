package com.mindjourney.core.observer.trigger.model

/**
 * Lightweight human-readable metadata associated with a trigger.
 *
 * A description carries contextual information useful for:
 *  - logging and tracing trigger execution
 *  - diagnostics and debugging
 *  - grouping or filtering triggers
 *  - analytics or telemetry attribution
 *
 * @property name
 *     Stable identifier of the trigger, typically known at app startup.
 *     Examples: `"battery"`, `"sync"`, `"sessionTimeout"`.
 *
 * @property source
 *     Runtime origin of the trigger — e.g. screen, feature module,
 *     or interaction channel. This value is mutable so orchestration layers
 *     may update it when context changes.
 *
 * The `empty()` factory is intended for testing or placeholder contexts
 * where meaningful metadata is not available.
 */
data class TriggerDescription(
    val name: String? = "unknown",
    var source: String = ""
) {
    companion object {
        fun empty(): TriggerDescription =
            TriggerDescription(name = "empty", source = "empty")
    }
}
