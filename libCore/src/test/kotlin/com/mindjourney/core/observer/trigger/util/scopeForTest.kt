package com.mindjourney.core.observer.trigger.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope

object ScopeForTest {

    fun get(testScope: TestScope): CoroutineScope {
        val dispatcher = StandardTestDispatcher(testScope.testScheduler)
        val scope = CoroutineScope(dispatcher)
        return scope
    }
}
