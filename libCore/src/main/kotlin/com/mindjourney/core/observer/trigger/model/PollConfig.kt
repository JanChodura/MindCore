package com.mindjourney.core.observer.trigger.model

/**
 * Declarative configuration for time-driven (polling) trigger execution.
 *
 * While reactive triggers evaluate decisions based on event streams,
 * some conditions must still be checked cyclically — for example:
 *  - periodic sync attempts
 *  - delayed readiness checks
 *  - repeating availability probes
 *
 * `PollConfig` describes **how often** such evaluations should occur:
 *
 *  @property cycles
 *      Number of evaluation attempts before the polling flow completes.
 *      For example, a value of 5 means “evaluate at most 5 times”.
 *
 *  @property intervalSec
 *      Delay (in seconds) between evaluations.
 *
 * This configuration is interpreted by the orchestration layer producing
 * tick flows, not by triggers themselves — triggers remain logic units,
 * and polling timing is controlled externally.
 *
 * The [DEFAULT] value is provided as a convenience for callers
 * that do not supply explicit configuration.
 */
data class PollConfig(val cycles: Int, val intervalSec: Int) {
    companion object {
        val DEFAULT = PollConfig(cycles = 5, intervalSec = 60)
    }
}
