package com.mindjourney.core.eventbus.service

import com.mindjourney.core.eventbus.model.trigger.TriggerResult
import com.mindjourney.core.eventbus.service.reactive.consumer.IDomainTriggerResultConsumer
import com.mindjourney.core.eventbus.service.reactive.consumer.IGlobalTriggerResultConsumer
import javax.inject.Inject
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Default implementation of [ITriggerResultHub].
 *
 * Holds a set of domain-level TriggerResult consumers and simply forwards every
 * TriggerResult to all of them. It performs no filtering or business logic.
 *
 * Each consumer decides for itself whether the result is relevant.
 */
@ViewModelScoped
class TriggerResultHub @Inject constructor(
    private val globalConsumers: Set<@JvmSuppressWildcards IGlobalTriggerResultConsumer>,
    private val domainConsumers: Set<@JvmSuppressWildcards IDomainTriggerResultConsumer>
) : ITriggerResultHub {

    override fun dispatch(result: TriggerResult, useCaseCallback: () -> Unit) {
        domainConsumers.forEach { it.consume(result, useCaseCallback) }
    }
}
