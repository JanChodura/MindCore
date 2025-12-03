package com.mindjourney.core.observer.trigger.model

object TriggerPool {
    private val pool = mutableMapOf<String, IAppTrigger>()

    fun getOrCreate(trigger: IAppTrigger): IAppTrigger {
        return pool.getOrPut(trigger.description.name ?: "unknown") {
            trigger
        }
    }
}
