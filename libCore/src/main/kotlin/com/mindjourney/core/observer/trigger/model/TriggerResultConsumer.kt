package com.mindjourney.core.observer.trigger.model

/**
 * Consumes trigger results produced by trigger evaluation.
 *
 * Typically implemented by orchestration or UI layers that
 * react to emitted [TriggerResult] effects.
 */
interface TriggerResultConsumer {

    /** Handles a result emitted by a trigger. */
    suspend fun consume(result: TriggerResult)

    companion object {
        /** No-op consumer used when no handling is needed. */
        fun empty(): TriggerResultConsumer =
            object : TriggerResultConsumer {
                override suspend fun consume(result: TriggerResult) = Unit
            }
    }
}
