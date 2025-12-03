package com.mindjourney.core.viewmodel.helper

import com.mindjourney.core.observer.trigger.model.IAppTrigger
import com.mindjourney.core.util.logging.injectedLogger
import com.mindjourney.core.util.logging.on
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

open class ReactiveHandler(
    private val ctx: ViewModelContext,
    protected val scope: CoroutineScope,
    private val onScreenActive: () -> Unit,
    private val onReady: () -> Unit,
) : IReactiveHandler {

    private val log = injectedLogger<ReactiveHandler>(on)
    protected val triggersContext = ctx.triggersContext

    private var isInitialized = false
    protected val jobs = mutableListOf<Job>()

    override fun initialize() {
        log.d("Initializing Reactive handler. isInitialized=$isInitialized")
        if (isInitialized) return

        initializeTriggerSystemInitializer()
        observeScreen()
        observeTriggerInitialization()
        observeCustomTriggers()
        isInitialized = true
    }

    override fun clear() {
        jobs.forEach { it.cancel() }
        jobs.clear()
    }

    override fun updateTriggers() {
        observeCustomTriggers()
    }

    private fun observeScreen() {
        val job = scope.launch {
            val baseScreen = ctx.screenTracker.activeScreen.value

            ctx.screenTracker.state.activeScreen.collect { screen ->
                log.d("ReactiveHandler: Observed active screen change to $screen")
                if (screen == baseScreen) {
                    log.d("ReactiveHandler: Active screen matches base screen. Invoking onScreenActive.")
                    onScreenActive()
                }
            }
        }
        jobs += job
    }

    private fun observeTriggerInitialization() {
        val readinessTrigger = ctx.triggersContext
            .firstOrNull { !it.trigger.isReady.value }
            ?.trigger ?: return

        val job = scope.launch {
            readinessTrigger.isReady
                .filter { it }
                .take(1)
                .collect {
                    log.d("ReactiveHandler: Trigger system is ready.")
                    onReady()
                }
        }

        jobs += job
    }

    private fun initializeTriggerSystemInitializer() {

        val initializer = ViewModelTriggerSystemInitializer(
            ctx = ctx,
        )
        log.d("ReactiveHandler: Initializing trigger system initializer for source=${ctx.source}")
        initializer.initObservingTriggersIn()
    }

    private fun observeCustomTriggers() {
        // později doplníme – přesuneme:
        // - ScreenWithPagerReselectTrigger
        // - MorningResetTrigger
        // - ActiveScreenReadinessTrigger
    }

    override fun <T : IAppTrigger> observeTriggerReadiness(
        triggerClass: KClass<T>,
        onTriggerReady: () -> Unit
    ) {
        val trigger = triggersContext
            .firstOrNull { triggerClass.isInstance(it.trigger) }
            ?.trigger as? T ?: return

        log.d("ReactiveHandler: Observing readiness for trigger: ${triggerClass.simpleName}")
        val job = scope.launch {
            trigger.isReady
                .onStart {
                    if (trigger.isReady.value) {
                        log.d("ReactiveHandler: Trigger ${triggerClass.simpleName} is already ready on start.")
                        onTriggerReady()
                        onReady()
                    }
                }
                .filter { it }
                .take(1)
                .collect {
                    log.d("ReactiveHandler: Trigger ${triggerClass.simpleName} became ready.")
                    onTriggerReady()
                    onReady()
                }
        }

        jobs += job
    }


}
