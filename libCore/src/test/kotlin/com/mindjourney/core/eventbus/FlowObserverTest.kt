package com.mindjourney.core.eventbus

import app.cash.turbine.test
import com.mindjourney.core.eventbus.model.event.ObserverEvent
import com.mindjourney.core.eventbus.model.event.context.FlowObserverContext
import com.mindjourney.core.eventbus.model.trigger.context.TriggerContext
import com.mindjourney.core.eventbus.model.trigger.result.TriggerResultType
import com.mindjourney.core.eventbus.observer.FlowObserver
import com.mindjourney.core.eventbus.service.EventManager
import com.mindjourney.core.eventbus.testutil.SimpleFlowTestTrigger
import com.mindjourney.core.eventbus.testutil.TestTriggerResultConsumer
import com.mindjourney.core.eventbus.testutil.UtilTriggerContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests full event pipeline:
 * Flow → FlowObserver → EventManager → Trigger → TriggerResultConsumer
 */
class FlowObserverTest {

    @Test
    fun flowObserverEmitsTriggerResultWhenFlowChanges() = runTest {
        // Arrange
        val dispatcher = StandardTestDispatcher(testScheduler)
        val scope = TestScope(dispatcher)

        val sourceFlow = MutableSharedFlow<String>(replay = 1)
        val consumer = TestTriggerResultConsumer()
        val eventManager = EventManager(scope, consumer)

        val expectedType = TriggerResultType.Success
        val trigger = SimpleFlowTestTrigger(expectedType)

        // Define event mapping
        val triggerContext = UtilTriggerContext.create()
        eventManager.register(triggerContext)

        // FlowObserver
        val observer = FlowObserver(scope, null) // null terminator for test
        val flowContext = FlowObserverContext(
            flow = sourceFlow,
            mapToEvent = { value -> ObserverEvent.FlowChanged(value) },
            event = ObserverEvent.FlowChanged("ignored"),
            finishFlow = MutableSharedFlow()
        )
        observer.start(eventManager, flowContext)

        // Act & Assert
        consumer.results.test {
            sourceFlow.emit("HELLO")
            val emission = awaitItem()

            assertEquals(expectedType, emission.type)
            assertEquals(trigger.description, emission.description)
            val event = emission.triggeredBy as ObserverEvent.FlowChanged<*>
            assertEquals("HELLO", event.value)
        }
    }
}
