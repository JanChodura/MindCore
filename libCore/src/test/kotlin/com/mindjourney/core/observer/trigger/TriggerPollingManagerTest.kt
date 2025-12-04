package com.mindjourney.core.observer.trigger

import app.cash.turbine.test
import com.mindjourney.core.observer.trigger.model.TriggerDescription
import com.mindjourney.core.observer.trigger.model.TriggerResult
import com.mindjourney.core.observer.trigger.model.TriggerResultType
import com.mindjourney.core.observer.trigger.util.ScopeForTest
import com.mindjourney.core.observer.trigger.util.SimplePollingTestTrigger
import com.mindjourney.core.observer.trigger.util.UtilTriggerManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class TriggerPollingManagerTest {

    private lateinit var testScope: TestScope
    private lateinit var testDispatcher: TestDispatcher

    @Before
    fun setup() {
        testDispatcher = StandardTestDispatcher()
        testScope = TestScope(testDispatcher)
    }

    @After
    fun tearDown() {
        testScope.cancel()
    }

    @Test
    fun triggerResultIsEmittedAfterOneTick() = testScope.runTest {
        // Arrange
        val description = TriggerDescription("testPolling", "testSource")
        val expectedResult = TriggerResult(description, TriggerResultType.ExecuteAction("testAction"))
        val trigger = SimplePollingTestTrigger(expectedResult)
        val scope = ScopeForTest.get(testScope)

        // Act - starts polling flow
        val manager = UtilTriggerManager.createWithSingleTrigger(scope, trigger, description)


        // Assert
        manager.triggerResult
            .drop(1) // Skip initial None
            .test {
                manager.evaluateTriggers()
                advanceTimeBy(2000)
                val result = awaitItem()
                assertEquals(expectedResult, result)
            }

    }
}
