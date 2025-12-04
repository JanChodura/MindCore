package com.mindjourney.core.eventbus.testutil

import com.mindjourney.core.eventbus.model.trigger.BaseAppTrigger
import com.mindjourney.core.eventbus.model.trigger.TriggerDescription
import com.mindjourney.core.eventbus.model.trigger.result.TriggerResultType

/**
 * Simple trigger used in FlowObserver tests.
 * Always returns the provided [resultType].
 */
class SimpleFlowTestTrigger(
    private val resultType: TriggerResultType,
    descriptionLabel: String = "SimpleFlowTestTrigger"
) : BaseAppTrigger() {

    init {
        description = TriggerDescription(descriptionLabel)
        setReady(true)
    }

    override suspend fun tryExecute(): TriggerResultType = resultType
}
