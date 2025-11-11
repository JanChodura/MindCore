package com.mindjourney.core.observer.trigger.model

interface TriggerResultConsumer {
    suspend fun consume(result: TriggerResult)

    companion object {
        fun empty(): TriggerResultConsumer {
            return object : TriggerResultConsumer {
                override suspend fun consume(result: TriggerResult) {
                    //empty
                }
            }
        }
    }
}
