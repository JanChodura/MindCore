package com.mindjourney.core.observer.trigger.model

object TriggerPool {
    private val pool = mutableMapOf<String, IAppTrigger>()
    private val started = mutableSetOf<String>()

    fun getOrCreate(trigger: IAppTrigger): IAppTrigger {
        return pool.getOrPut(trigger.description.name ?: "unknown") {
            trigger
        }
    }

    fun isStarted(name: String): Boolean =
        started.contains(name)

    fun markStarted(name: String) {
        started.add(name)
    }
}
