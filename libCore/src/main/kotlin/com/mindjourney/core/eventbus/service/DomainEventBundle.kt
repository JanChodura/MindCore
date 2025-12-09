package com.mindjourney.core.eventbus.service

import com.mindjourney.core.eventbus.model.CoreQualifier
import com.mindjourney.core.eventbus.model.event.context.ObserverContext
import com.mindjourney.core.eventbus.model.trigger.context.TriggerContext
import dagger.hilt.android.scopes.ViewModelScoped
import jakarta.inject.Inject

@ViewModelScoped
data class DomainEventBundle @Inject constructor(
    @param:CoreQualifier.Domain override val observerContexts: List<@JvmSuppressWildcards ObserverContext>,
    @param:CoreQualifier.Domain override val triggerContexts: List<@JvmSuppressWildcards TriggerContext>,
) : IDomainEventBundle
