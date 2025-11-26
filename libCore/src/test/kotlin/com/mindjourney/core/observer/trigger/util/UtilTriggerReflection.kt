package com.mindjourney.core.observer.trigger.util

import com.mindjourney.core.observer.trigger.model.IAppTrigger
import kotlinx.coroutines.test.TestScope

/**
 * ⚠️ Utility class for test-only reflective hacks on [TriggerPollingManager].
 *
 * This helper uses Java reflection to manually inject a list of triggers
 * into the internal `triggers` field of [TriggerPollingManager].
 *
 * It should only be used in test environments where the normal initialization
 * flow is not available. Using reflection to modify internal state is fragile
 * and may break if the underlying implementation changes.
 *
 * @param manager the instance of [TriggerPollingManager] whose triggers should be set.
 * @param testScope the [TestScope] to use when creating the trigger context.
 * @param trigger the trigger to inject.
 */
object UtilTriggerReflection {

    fun injectSingleTrigger(manager: TriggerPollingManager, testScope: TestScope, trigger: IAppTrigger) {
        val triggersField = TriggerPollingManager::class.java.getDeclaredField("triggers")
        triggersField.isAccessible = true
        triggersField.set(
            manager,
            listOf(UtilTriggerContext.createSimpleTriggerContext(scope = testScope, trigger = trigger))
        )
    }
}
