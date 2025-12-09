package com.mindjourney.core.eventbus.service.reactive.consumer

import com.mindjourney.core.eventbus.model.trigger.TriggerResult

/**
 * Receives TriggerResult objects dispatched by the EventManager and
 * decides whether the corresponding domain action should be executed.
 *
 * A TriggerResultConsumer performs *no* business logic. It only inspects
 * the incoming TriggerResult and, if relevant, delegates execution to a
 * Performer together with a callback that represents the actual domain
 * UseCase logic.
 *
 * The callback (useCaseCallback) is defined by the ViewModel and represents
 * a complete domain action to be executed once the consumer determines the
 * TriggerResult is relevant. This keeps domain logic out of the consumer
 * and performer layers, allowing the ViewModel to orchestrate behaviour.
 *
 * Execution model:
 *
 *     ViewModel → Consumer → Performer → useCaseCallback() → ViewModel
 *
 * Consumers never interpret the meaning of the callback. They only
 * forward it to the performer.
 */
interface TriggerResultConsumer {

    /**
     * Processes the TriggerResult emitted by a Trigger. When the result is
     * relevant, the consumer forwards the provided UseCase callback to the
     * underlying performer.
     *
     * @param result            The TriggerResult produced by a Trigger.
     * @param useCaseCallback   Domain action defined by the ViewModel,
     *                          executed after the performer runs. If the
     *                          consumer determines the result is irrelevant,
     *                          this callback is ignored.
     */
    fun consume(result: TriggerResult, useCaseCallback: () -> Unit = {})
}
