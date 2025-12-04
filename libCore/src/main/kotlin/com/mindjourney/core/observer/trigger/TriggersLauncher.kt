package com.mindjourney.core.observer.trigger

import com.mindjourney.core.observer.trigger.model.IPollingTrigger
import com.mindjourney.core.observer.trigger.util.TriggerContext
import com.mindjourney.core.util.logging.injectedLogger
import com.mindjourney.core.util.logging.off

/**
 * Launches triggers using simple orchestration rules:
 *
 *  - polling triggers are always restarted,
 *  - reactive triggers are started only once.
 *
 * It delegates execution to [TriggerInitializer] rather than running logic itself.
 */

class TriggersLauncher(
    private val startTrigger: (TriggerContext) -> Unit,
) {

    private val log = injectedLogger<TriggersLauncher>(off)

    /**
     * Public entry point for launching all triggers.
     * It cancels old jobs, splits the list, logs counts,
     * and launches triggers according to their type.
     */
    fun evaluate(list: List<TriggerContext>) {
        val (polling, reactive) = splitTriggersByType(list)
        logTriggerCounts(polling.size, reactive.size)
        evaluatePollingTriggers(polling)
        evaluateReactiveTriggers(reactive)
    }

    /**
     * Splits all triggers into two groups: polling and reactive.
     * @return Pair of lists (polling, reactive)
     */
    private fun splitTriggersByType(triggers: List<TriggerContext>): Pair<List<TriggerContext>, List<TriggerContext>> {
        val (polling, reactive) = triggers.partition { it.trigger is IPollingTrigger }
        return polling to reactive
    }

    /**
     * Logs how many triggers of each type will be started.
     */
    private fun logTriggerCounts(pollingCount: Int, reactiveCount: Int) {
        log.d("Starting $pollingCount polling and $reactiveCount reactive triggers")
    }

    /**
     * Launches all polling triggers.
     * These triggers are safe to restart and typically run on screen changes.
     */
    private fun evaluatePollingTriggers(polling: List<TriggerContext>) {
        log.d("Launching polling triggers...")
        polling.forEach { startTrigger(it) }
    }

    /**
     * Launches reactive triggers only if none are already active.
     * Prevents duplicate Flow collectors or multiple reactive subscriptions.
     */
    private fun evaluateReactiveTriggers(reactive: List<TriggerContext>) {
        log.d("Launching reactive triggers...")
        reactive.forEach { startTrigger(it) }
    }
}
