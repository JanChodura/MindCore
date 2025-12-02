package com.mindjourney.core.observer.trigger

import app.cash.turbine.test
import com.mindjourney.core.observer.trigger.model.TriggerResult
import com.mindjourney.core.observer.trigger.util.TestTrigger
import com.mindjourney.core.observer.trigger.util.UtilTriggerContext
import com.mindjourney.core.observer.trigger.util.UtilTriggerPollingManager
import com.mindjourney.core.observer.trigger.util.UtilTriggerReflection
import com.mindjourney.core.util.logging.ILogger
import com.mindjourney.core.util.logging.TestLogger
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
        val expectedResult = TriggerResult.ExecuteAction("testAction")
        val trigger = TestTrigger()
        val manager = UtilTriggerPollingManager.createSimpleManager( testScope, trigger)



        // Act + Assert
        trigger
            .drop(1) // Skip initial None
            .test {
                manager.startPollingForTriggers()
                advanceTimeBy(2000)
                val result = awaitItem()
                assertEquals(expectedResult, result)
            }

    }
}
