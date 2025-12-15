package com.mindjourney.core.eventbus.testutil

import com.mindjourney.core.eventbus.model.event.ObserverEvent
import com.mindjourney.core.eventbus.model.trigger.IAppTrigger
import com.mindjourney.core.eventbus.model.trigger.context.TriggerContext

object UtilTriggerContext {
    fun create(trigger: IAppTrigger): TriggerContext {
        return TriggerContext(
            trigger = trigger,
            event = ObserverEvent.FlowChanged("ignored")
        )
    }

}
