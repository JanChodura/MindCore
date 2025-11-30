package com.mindjourney.core.viewmodel.helper

import com.mindjourney.core.observer.trigger.model.IAppTrigger
import kotlin.reflect.KClass

interface IReactiveHandler {

    fun initialize()

    fun clear()

    fun updateTriggers()
    fun <T : IAppTrigger> observeTriggerReadiness(triggerClass: KClass<T>, onTriggerReady: () -> Unit)
}
