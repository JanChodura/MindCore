package com.mindjourney.core.eventbus.service.consumer

import com.mindjourney.core.eventbus.model.trigger.TriggerResult

/**
 * Asynchronous (suspending) TriggerResult consumer.
 *
 * This consumer is used when the associated Performer executes
 * suspending domain operations. It is typically invoked from
 * `viewModelScope.launch { ... }`.
 *
 * The consumer inspects the TriggerResult, decides whether the
 * corresponding Performer should run, and forwards the optional
 * completion callback. Once the Performer and UseCase complete,
 * the callback is invoked.
 *
 * Consumers never contain business logic — they only route domain
 * intent to the appropriate Performer.
 */
interface TriggerResultConsumer {

    /**
     * Processes the TriggerResult asynchronously.
     *
     * @param result   The value emitted by a Trigger.
     * @param callback Optional completion handler invoked after the domain
     *                 action finishes execution.
     */
    suspend fun consume(result: TriggerResult, callback: () -> Unit = {})
}
