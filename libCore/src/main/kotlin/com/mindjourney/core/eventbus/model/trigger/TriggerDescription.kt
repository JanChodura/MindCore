package com.mindjourney.core.eventbus.model.trigger

/**
 * Data class representing the description of a trigger.
 *
 * @property name The name of the trigger - static defined during start app.
 * @property source The source or origin of the trigger - dynamic defined from current source.
 */
data class TriggerDescription(val name: String = "unknown", var source: String = "") {
    companion object {
        fun empty(): TriggerDescription {
            return TriggerDescription(name = "empty", source = "empty")
        }
    }
}
