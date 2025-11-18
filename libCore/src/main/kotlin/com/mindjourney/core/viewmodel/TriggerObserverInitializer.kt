package com.mindjourney.core.viewmodel

import com.mindjourney.core.logger.LoggerProvider
import com.mindjourney.core.observer.trigger.TriggerPoll
import com.mindjourney.core.util.logging.injectedLogger
import com.mindjourney.core.util.logging.off
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Handles initialization of trigger observation for a given [ViewModelContext].
 * Ensures proper setup of TriggerPolls and observer activation for primary ViewModels.
 */
class TriggerObserverInitializer(
    private val ctx: ViewModelContext,
    private val scope: CoroutineScope
) {

    private val log = injectedLogger<TriggerObserverInitializer>(LoggerProvider.get(), off)

    /**
     * Public entry point for initializing trigger observation for given ViewModel.
     */
    fun initObservingTriggersIn(source: String) {
        setupTriggerPolls(source)
        val activePrimaries = detectPrimaryVMHolders()
        initObserverForPrimaryVM(activePrimaries)
    }

    // --- private helpers ---

    private fun setupTriggerPolls(source: String) {

        ctx.triggersContext.forEach { triggerContext ->
            triggerContext.triggerPoll = TriggerPoll(
                scope = scope,
                description = triggerContext.description
            )
            triggerContext.description.source = source
        }
    }

    private fun detectPrimaryVMHolders(): List<BaseViewModel> {
        val allHolders = ctx.triggersContext.mapNotNull { it.trigger as? BaseViewModel? }
        val activePrimaries = allHolders.filter { it.ctx.isPrimaryObserverHolder.value }
        return activePrimaries
    }

    private fun initObserverForPrimaryVM(activePrimaries: List<BaseViewModel>) {
        when {
            activePrimaries.size > 1 -> {
                log.e(
                    "Multiple primary ViewModels detected: ${activePrimaries.map { it::class.simpleName }}",
                    null
                )
            }

            activePrimaries.size == 1 -> {
                log.d("Starting observer only for primary ViewModel: ${activePrimaries.first()::class.simpleName}")
                ctx.observer.init(MutableStateFlow(ctx.triggersContext))
            }

            else -> {
                log.d("No primary ViewModel marked, initializing observer for all contexts")
                ctx.observer.init(MutableStateFlow(ctx.triggersContext))
            }
        }
    }
}
