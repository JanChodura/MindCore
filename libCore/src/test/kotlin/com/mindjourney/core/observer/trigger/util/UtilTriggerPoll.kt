// TODO: Vlož do test/.../util/UtilTriggerPoll.kt
package com.mindjourney.core.observer.trigger.util

import com.mindjourney.core.observer.trigger.TriggerPoll
import com.mindjourney.core.util.logging.TestLogger
import kotlinx.coroutines.CoroutineScope

object UtilTriggerPoll {

    /**
     * Creates a minimal TriggerPoll instance configured for simple testing.
     * Polls once after 1 second in a controlled coroutine test scope.
     *
     * @return TriggerPoll ready for testing
     */
    fun createSimpleTestingTriggerPoll(scope: CoroutineScope): TriggerPoll {
        val logger = TestLogger()

        return TriggerPoll(logger, scope, "TestSimplePoll").apply {
            attachCycles(3)
            attachInterval(1)
            attachSource("test")
            attachScope(scope)
        }
    }
}
