package com.mindjourney.core.observer.trigger

import com.mindjourney.core.observer.trigger.model.PollingTrigger
import com.mindjourney.core.observer.trigger.util.TriggerContext
import com.mindjourney.core.util.logging.ILogger
import com.mindjourney.core.util.logging.injectedLogger
import com.mindjourney.core.util.logging.off
import kotlinx.coroutines.Job

/**
 * Coordinates the initialization and launch of both polling and reactive triggers.
 *
 * This class is responsible for:
 * - Cancelling any previously running trigger jobs.
 * - Splitting the trigger list into polling and reactive types.
 * - Launching each trigger type with proper scoping and logging.
 *
 * Reactive triggers are started only once to avoid duplicate listeners,
 * while polling triggers are restarted every time a screen or context changes.
 */
class TriggersLauncher(
    private val logger: ILogger,
    private val cancelExistingJobs: () -> Unit,
    private val startTrigger: (TriggerContext) -> Unit,
    private val getAllTriggers: () -> List<TriggerContext>,
    private val getReactiveJobs: () -> List<Job>
) {

    private val log = injectedLogger<TriggersLauncher>(logger, off)

    /**
     * Public entry point for launching all triggers.
     * It cancels old jobs, splits the list, logs counts,
     * and launches triggers according to their type.
     */
    fun launchAll() {
        cancelRunningJobs()
        val (polling, reactive) = splitTriggersByType()
        logTriggerCounts(polling.size, reactive.size)
        launchPollingTriggers(polling)
        launchReactiveTriggersOnce(reactive)
    }

    /** Cancels all currently active trigger jobs before restarting them. */
    private fun cancelRunningJobs() {
        log.d("Cancelling existing trigger jobs before relaunch")
        cancelExistingJobs()
    }

    /**
     * Splits all triggers into two groups: polling and reactive.
     * @return Pair of lists (polling, reactive)
     */
    private fun splitTriggersByType(): Pair<List<TriggerContext>, List<TriggerContext>> {
        val triggers = getAllTriggers()
        val (polling, reactive) = triggers.partition { it.trigger is PollingTrigger }
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
    private fun launchPollingTriggers(polling: List<TriggerContext>) {
        log.d("Launching polling triggers...")
        polling.forEach { startTrigger(it) }
    }

    /**
     * Launches reactive triggers only if none are already active.
     * Prevents duplicate Flow collectors or multiple reactive subscriptions.
     */
    private fun launchReactiveTriggersOnce(reactive: List<TriggerContext>) {
        val activeJobs = getReactiveJobs()
        if (activeJobs.isEmpty()) {
            log.d("Launching reactive triggers")
            reactive.forEach { startTrigger(it) }
        } else {
            log.d("Skipping reactive triggers – already active (${activeJobs.size} jobs running)")
        }
    }
}
