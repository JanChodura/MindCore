package com.mindjourney.core.eventbus.testutil

import com.mindjourney.core.eventbus.model.event.ObserverEvent
import com.mindjourney.core.eventbus.model.trigger.IAppTrigger
import com.mindjourney.core.eventbus.model.trigger.context.TriggerContext

class UtilTriggerContext() {

    companion object {
        fun create(trigger: IAppTrigger): TriggerContext {
            return TriggerContext(
                trigger = trigger,
                event = ObserverEvent.FlowChanged("ignored")
            )
        }
    }

}
