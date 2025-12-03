package com.mindjourney.core.observer

import com.mindjourney.core.observer.trigger.TriggerManager
import com.mindjourney.core.observer.trigger.model.TriggerResultConsumer
import com.mindjourney.core.observer.trigger.util.TriggerContext
import com.mindjourney.core.tracking.ScreenTrackerFactory
import com.mindjourney.core.tracking.ScreenTracker
import com.mindjourney.core.util.logging.injectedLogger
import com.mindjourney.core.util.logging.off
import com.mindjourney.core.util.logging.on
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
class AppScreenObserver @Inject constructor(
    private val scope: CoroutineScope,
    private val tracker: ScreenTracker,
    private val resultConsumer: TriggerResultConsumer,
):IAppScreenObserver {

    private val log = injectedLogger<AppScreenObserver>(on)

    companion object {
        /** Creates an empty/no-op instance of AppObserver for cases where DI is not available like Preview, Tests. */
        fun empty(): AppScreenObserver = AppScreenObserver(
            CoroutineScope(EmptyCoroutineContext),
            ScreenTrackerFactory.empty(),
            TriggerResultConsumer.empty()
        )
    }

    private var isTrackerObserved = false

    private val triggerManager = TriggerManager(scope)

    private var triggersFlow: StateFlow<List<TriggerContext>>? = null

    override fun init(triggersFlow: StateFlow<List<TriggerContext>>) {
        log.d("Initializing AppScreenObserver with triggersFlow of size=${triggersFlow.value.size}")
        this.triggersFlow = triggersFlow
        triggerManager.initTriggers(triggersFlow)
        observeActiveScreenTransitions()
        triggerManager.observeTriggerResults(resultConsumer)
    }

    /** Listens to ScreenTracker and starts evaluation when user changes screen. */
    private fun observeActiveScreenTransitions() {
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
