package com.mindjourney.core.eventbus.service.consumer

import com.mindjourney.core.eventbus.model.trigger.TriggerResult

/**
 * Synchronous TriggerResult consumer.
 *
 * This consumer is used when the associated Performer executes a
 * non-suspending domain operation. It simply inspects the incoming
 * TriggerResult and, if relevant, delegates execution to a synchronous
 * Performer. No coroutine context is required.
 *
 * A completion callback may be provided by the ViewModel and is
 * forwarded down the chain (Consumer → Performer → UseCase → callback()).
 *
 * Consumers do not execute business logic themselves — they only decide
 * *whether* a Performer should run.
 */
interface TriggerResultConsumerSync {

    /**
     * Processes the TriggerResult synchronously.
     *
     * @param result   The value emitted by a Trigger.
     * @param callback Optional completion handler invoked after the domain
     *                 action finishes execution.
     */
    fun consume(result: TriggerResult, callback: () -> Unit = {})
}
