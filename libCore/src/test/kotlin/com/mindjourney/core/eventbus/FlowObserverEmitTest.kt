package com.mindjourney.core.eventbus

import app.cash.turbine.test
import com.mindjourney.core.eventbus.model.event.ObserverEvent
import com.mindjourney.core.eventbus.model.event.context.FlowObserverContext
import com.mindjourney.core.eventbus.model.trigger.SimpleEagerTrigger
import com.mindjourney.core.eventbus.model.trigger.result.TriggerResultType
import com.mindjourney.core.eventbus.observer.FlowObserver
import com.mindjourney.core.eventbus.service.EventManager
import com.mindjourney.core.eventbus.service.consumer.GlobalTriggerResultConsumer
import com.mindjourney.core.eventbus.testutil.NoopTerminator
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
class FlowObserverEmitTest {

    @Test
    fun flowObserverEmitsTriggerResultWhenFlowChanges() = runTest {
        // Arrange
        val dispatcher = StandardTestDispatcher(testScheduler)
        val scope = TestScope(dispatcher)

        val sourceFlow = MutableSharedFlow<String>(replay = 1)
        val consumer = GlobalTriggerResultConsumer()
        val eventManager = EventManager(scope, consumer)

        val trigger = SimpleEagerTrigger()

        // Define event mapping
        val triggerContext = UtilTriggerContext.create(trigger)
        eventManager.register(triggerContext)

        // FlowObserver
        val observer = FlowObserver(scope, NoopTerminator())
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

            assertEquals(TriggerResultType.Success, emission.type)
            assertEquals(trigger.description, emission.description)
            val event = emission.triggeredBy as ObserverEvent.FlowChanged<*>
            assertEquals("HELLO", event.value)
        }
    }
}
