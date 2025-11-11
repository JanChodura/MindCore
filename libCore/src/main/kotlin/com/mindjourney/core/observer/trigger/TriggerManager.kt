package com.mindjourney.core.observer.trigger

import com.mindjourney.core.observer.trigger.model.TriggerResult
import com.mindjourney.core.observer.trigger.model.TriggerResultConsumer
import com.mindjourney.core.observer.trigger.util.TriggerContext
import com.mindjourney.core.util.logging.ILogger
import com.mindjourney.core.util.logging.injectedLogger
import com.mindjourney.core.util.logging.off
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Central manager responsible for coordinating both polling and reactive triggers.
 * It decides how to start each trigger type, attaches configuration,
 * and manages their coroutine jobs and results.
 */
class TriggerManager(
    private val scope: CoroutineScope,
    private val logger: ILogger,
) {

    private val log = injectedLogger<TriggerManager>(logger, off)
    private val activeJobs = mutableListOf<Job>()
    private val triggers: MutableList<TriggerContext> = mutableListOf()

    private val _triggerResult = MutableStateFlow<TriggerResult>(TriggerResult.None)
    val triggerResult: StateFlow<TriggerResult> = _triggerResult

    private val reactiveJobs = mutableListOf<Job>()
    private val triggersLauncher = TriggersLauncher(
        logger = logger,
        cancelExistingJobs = { cancelExistingJobs() },
        startTrigger = { triggerSelector.init(it) },
        getAllTriggers = { triggers },
        getReactiveJobs = { reactiveJobs }
    )

    private val triggerSelector = TriggerInitializer(scope, logger) { result ->
        if (result !is TriggerResult.None) _triggerResult.value = result
    }

    /** Initializes triggers*/
    fun initTriggers(triggersFlow: StateFlow<List<TriggerContext>>? = null) {
        triggers.clear()
        triggers.addAll(triggersFlow?.value.orEmpty())
    }

    /** Starts processing all triggers, deciding between polling and reactive modes. */
    fun startAllTriggers() {
        triggersLauncher.launchAll()
    }

    /** Cancels all running jobs. */
    fun cancelExistingJobs() {
        log.d( "Cancelling ${activeJobs.size} existing trigger jobs")
        activeJobs.forEach { it.cancel() }
        activeJobs.clear()
    }

    /** Determines trigger type and starts appropriate observation logic. */

    fun observeTriggerResults(resultConsumer: TriggerResultConsumer) {
        scope.launch {
            triggerResult.collectLatest { resultConsumer.consume(it) }
        }
    }
}
