package com.mindjourney.core.viewmodel.helper

import com.mindjourney.core.observer.trigger.TriggerPoll
import com.mindjourney.core.util.logging.injectedLogger
import com.mindjourney.core.util.logging.off
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Handles initialization of trigger observation for a given [ViewModelContext].
 * Ensures proper setup of TriggerPolls and observer activation for primary ViewModels.
 */
class ViewModelTriggerSystemInitializer(
    private val ctx: ViewModelContext,
    private val scope: CoroutineScope,
) {

    private val log = injectedLogger<ViewModelTriggerSystemInitializer>(off)

    /**
     * Public entry point for initializing trigger observation for given ViewModel.
     */
    fun initObservingTriggersIn(source: String) {
        setupTriggerPolls(source)
        initObserverForPrimaryVM()
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

    private fun initObserverForPrimaryVM() {

        if (ctx.isPrimary) {
            ctx.observer.init(MutableStateFlow(ctx.triggersContext))

        }
    }
}
