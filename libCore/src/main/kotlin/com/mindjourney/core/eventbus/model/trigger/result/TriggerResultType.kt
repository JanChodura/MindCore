package com.mindjourney.core.eventbus.model.trigger.result

/**
 * Represents the universal, domain-agnostic results produced by any trigger.
 *
 * The core architecture defines only minimal outcomes that are meaningful across
 * all modules. Feature modules or app layers may extend the result set with their
 * own domain-specific variants.
 */
sealed class TriggerResultType {

    /** Trigger executed but produced no meaningful effect. */
    data object None : TriggerResultType()

    /** Trigger completed successfully. */
    data object Success : TriggerResultType()

    /** Trigger evaluated but failed (validation, missing data, etc.). */
    data class Failed(val reason: String? = null) : TriggerResultType()

    /**
     * Allows application-level modules to define their own custom
     * trigger results without modifying the core architecture.
     *
     * Example:
     *   TriggerResultType.Custom(MyFeatureEffect.NavigateToHabitScreen)
     */
    data class Custom(val payload: Any) : TriggerResultType()
}
