package com.mindjourney.core.viewmodel.helper

import com.mindjourney.core.observer.trigger.model.IAppTrigger
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

    protected val triggersContext = ctx.triggersContext

    private var isInitialized = false
    protected val jobs = mutableListOf<Job>()

    override fun initialize() {
        if (isInitialized) return
        isInitialized = true

        initializeTriggerObserver()
        observeScreen()
        observeTriggerInitialization()
        observeCustomTriggers()
    }

    override fun clear() {
        jobs.forEach { it.cancel() }
        jobs.clear()
    }

    override fun updateTriggers() {
        // pro případ, že se změní triggerContext
        observeCustomTriggers()
    }

    private fun observeScreen() {
        val job = scope.launch {
            val baseScreen = ctx.screenTracker.activeScreen.value

            ctx.screenTracker.state.activeScreen.collect { screen ->
                if (screen == baseScreen) {
                    onScreenActive()
                }
            }
        }
        jobs += job
    }

    private fun observeTriggerInitialization() {
        val readinessTrigger = ctx.triggersContext
            .firstOrNull { it.trigger.isReady != null }
            ?.trigger ?: return

        val job = scope.launch {
            readinessTrigger.isReady
                .filter { it }
                .take(1)
                .collect {
                    onReady()
                }
        }

        jobs += job
    }

    private fun initializeTriggerObserver() {
        val source = this::class.simpleName ?: "UnknownVM"

        val initializer = ViewModelTriggerSystemInitializer(
            ctx = ctx,
        )

        initializer.initObservingTriggersIn(source)
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

        val job = scope.launch {
            trigger.isReady
                .onStart {
                    if (trigger.isReady.value) {
                        onTriggerReady()
                        onReady()
                    }
                }
                .filter { it }
                .take(1)
                .collect {
                    onTriggerReady()
                    onReady()
                }
        }

        jobs += job
    }


}
