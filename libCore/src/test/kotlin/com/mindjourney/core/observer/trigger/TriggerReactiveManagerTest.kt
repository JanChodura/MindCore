package com.mindjourney.core.observer.trigger

import app.cash.turbine.test
import com.mindjourney.core.observer.trigger.model.TriggerDescription
import com.mindjourney.core.observer.trigger.model.TriggerResult
import com.mindjourney.core.observer.trigger.model.TriggerResultType
import com.mindjourney.core.observer.trigger.util.ScopeForTest
import com.mindjourney.core.observer.trigger.util.SimpleReactiveTestTrigger
import com.mindjourney.core.observer.trigger.util.TestTriggerResultConsumer
import com.mindjourney.core.observer.trigger.util.UtilTriggerManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class TriggerReactiveManagerTest {

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
    fun reactiveTriggerEmitsResultOnFirstFlowEvent() = testScope.runTest {
        // Arrange
        val description = TriggerDescription("Reactive Test Trigger", "EnvTest")
        val expectedResult = TriggerResult(description, TriggerResultType.ExecuteAction("reactiveAction"))

        val sourceFlow = MutableSharedFlow<Unit>(replay = 1)
        val trigger = SimpleReactiveTestTrigger(
            sourceFlow = sourceFlow,
            result = expectedResult,
        )
        val scope = ScopeForTest.get(testScope)
        val manager = UtilTriggerManager.createWithSingleTrigger(scope, trigger, description)
        val consumer = TestTriggerResultConsumer()
        manager.observeTriggerResults(consumer)

        // Assert
        consumer.results.test {
            manager.startAllTriggers()
            sourceFlow.emit(Unit)
            testScope.advanceUntilIdle()
            val result = awaitItem()
            assertEquals(expectedResult, result)
            assertEquals(expectedResult, result)
        }
    }
}
