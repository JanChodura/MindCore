package com.mindjourney.core.viewmodel.helper

import com.mindjourney.core.observer.trigger.TriggerPoll
import com.mindjourney.core.util.logging.injectedLogger
import com.mindjourney.core.util.logging.off
import com.mindjourney.core.util.logging.on
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Handles initialization of trigger observation for a given [ViewModelContext].
 * Ensures proper setup of TriggerPolls and observer activation for primary ViewModels.
 */
class ViewModelTriggerSystemInitializer(
    private val ctx: ViewModelContext,
    ) {

    private val log = injectedLogger<ViewModelTriggerSystemInitializer>(on)

    /**
     * Public entry point for initializing trigger observation for given ViewModel.
     */
    fun initObservingTriggersIn() {
        setupTriggerSources()
        initObserverForPrimaryVM()
    }

    // --- private helpers ---

    private fun setupTriggerSources() {
        ctx.triggersContext.forEach { triggerContext ->
            log.d("Setting up trigger source for trigger: ${triggerContext.description} with source: ${ctx.source}")
            triggerContext.description.source = ctx.source
        }
    }

    private fun initObserverForPrimaryVM() {

        if (ctx.isPrimary) {
            ctx.observer.init(MutableStateFlow(ctx.triggersContext))

        }
    }
}
