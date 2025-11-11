package com.mindjourney.core.observer

import com.mindjourney.core.logger.LoggerProvider
import com.mindjourney.core.observer.trigger.TriggerManager
import com.mindjourney.core.observer.trigger.model.TriggerResultConsumer
import com.mindjourney.core.observer.trigger.util.TriggerContext
import com.mindjourney.core.tracking.ActiveScreenTrackerFactory
import com.mindjourney.core.tracking.ScreenTracker
import com.mindjourney.core.util.logging.ILogger

import com.mindjourney.core.util.logging.injectedLogger
import com.mindjourney.core.util.logging.off
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Observes global app lifecycle conditions such as new day start.
 * If app resumes in early morning (after 2:00 AM), automatically
 * switches to Convictions screen first page once per day.
 */
@Singleton
class AppObserver @Inject constructor(
    private val scope: CoroutineScope,
    private val tracker: ScreenTracker,
    private val logger: ILogger,
    private val resultConsumer: TriggerResultConsumer,
) {

    private val log = injectedLogger<AppObserver>(logger, off)

    companion object {
        /** Creates an empty/no-op instance of AppObserver for cases where DI is not available like Preview, Tests. */
        fun empty(): AppObserver = AppObserver(
            CoroutineScope(EmptyCoroutineContext),
            ActiveScreenTrackerFactory.empty(),
            LoggerProvider.get(),
            TriggerResultConsumer.empty()
        )
    }

    private var isTrackerObserved = false

    private val triggerManager = TriggerManager(scope, logger)

    private var triggersFlow: StateFlow<List<TriggerContext>>? = null

    fun init(triggersFlow: StateFlow<List<TriggerContext>>) {
        this.triggersFlow = triggersFlow
        triggerManager.initTriggers(triggersFlow)
        launchTriggerManager()
        triggerManager.observeTriggerResults(resultConsumer)
    }

    /** Listens to ScreenTracker and starts evaluation when user changes screen. */
    private fun launchTriggerManager() {
        if (isTrackerObserved) return
        isTrackerObserved = true

        scope.launch {
            tracker.activeScreen
                .distinctUntilChangedBy { it }
                .collectLatest { screen ->
                    log.d("Screen changed → $screen, evaluating triggers")
                    triggerManager.startAllTriggers()
                }
        }
    }

}
