package com.mindjourney.core.observer.trigger.util

import com.mindjourney.core.observer.trigger.model.IAppTrigger
import com.mindjourney.core.observer.trigger.model.TriggerDescription

/**
 * Bundles all metadata required to initialize and run a trigger.
 *
 * This context is consumed by orchestration components, not triggers themselves.
 * It provides a single place containing:
 *
 *  - the trigger instance to evaluate
 *  - descriptive information for logging and diagnostics
 *  - optional polling configuration, if the trigger is time-driven
 *
 * Reactive triggers ignore polling fields, while polling orchestration
 * reads `pollCycles` and `pollIntervalSec` to configure the tick stream.
 */
data class TriggerContext(
    val description: TriggerDescription,
    val trigger: IAppTrigger,
    val pollCycles: Int? = null,
    val pollIntervalSec: Int? = null
)
