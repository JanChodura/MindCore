package com.mindjourney.core.viewmodel.helper

import com.mindjourney.core.eventbus.model.trigger.IAppTrigger
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
    private val vmCtx: ViewModelContext,
    protected val scope: CoroutineScope,
    private val onScreenActive: () -> Unit,
    private val onReady: () -> Unit,
) : IReactiveHandler {

    private val log = injectedLogger<ReactiveHandler>(on)
    protected val triggersContext = vmCtx.triggersContext

    private var isInitialized = false
    protected val jobs = mutableListOf<Job>()

    override fun initialize() {
        log.d("Initializing Reactive handler. isInitialized=$isInitialized")
        if (isInitialized) return

        setupTriggerSources()
        initializeTriggerSystemInitializer()
        observeTriggerInitialization()
        isInitialized = true
    }

    override fun clear() {
        jobs.forEach { it.cancel() }
        jobs.clear()
    }

    private fun setupTriggerSources() {
        vmCtx.triggersContext.forEach { triggerCtx ->
            triggerCtx.trigger.updateSource(vmCtx.source)
        }
    }

    fun IAppTrigger.updateSource(source: String) {
        description = description.copy(source = source)
    }

    private fun observeTriggerInitialization() {
        val readinessTrigger = vmCtx.triggersContext
            .firstOrNull { !it.trigger.isReady.value }
            ?.trigger ?: return

        val job = scope.launch {
            readinessTrigger.isReady
                .filter { it }
                .take(1)
                .collect {
                    log.d("Trigger system is ready in source=${vmCtx.source}")
                    onReady()
                }
        }

        jobs += job
    }

    private fun initializeTriggerSystemInitializer() {

        val initializer = DomainEventInitializer(
            ctx = vmCtx,
        )
        log.d("Initializing trigger system initializer for source=${vmCtx.source}")
        initializer.initObserving()
    }

    override fun <T : IAppTrigger> observeTriggerReadiness(
        triggerClass: KClass<T>,
        onTriggerReady: () -> Unit
    ) {
        val trigger = triggersContext
            .firstOrNull { triggerClass.isInstance(it.trigger) }
            ?.trigger as? T ?: return

        log.d("Observing readiness for trigger: ${trigger.description}")
        val job = scope.launch {
            trigger.isReady
                .onStart {
                    if (trigger.isReady.value) {
                        log.d("Trigger ${triggerClass.simpleName} is already ready on start.")
                        onTriggerReady()
                        onReady()
                    }
                }
                .filter { it }
                .take(1)
                .collect {
                    log.d("Trigger ${triggerClass.simpleName} became ready.")
                    onTriggerReady()
                    onReady()
                }
        }

        jobs += job
    }


}
