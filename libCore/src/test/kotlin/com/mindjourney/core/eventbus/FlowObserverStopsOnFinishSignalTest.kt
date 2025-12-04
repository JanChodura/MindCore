package com.mindjourney.core.eventbus

import app.cash.turbine.test
import com.mindjourney.core.eventbus.model.event.ObserverEvent
import com.mindjourney.core.eventbus.model.event.context.FlowObserverContext
import com.mindjourney.core.eventbus.model.trigger.context.TriggerContext
import com.mindjourney.core.eventbus.observer.FlowObserver
import com.mindjourney.core.eventbus.observer.terminator.IObserverLifecycleTerminator
import com.mindjourney.core.eventbus.service.IEventManager
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Verifies that FlowObserver stops collecting after finishFlow emits a signal.
 */
class FlowObserverStopsOnFinishSignalTest {

    @Test
    fun flowObserverStopsAfterFinishSignal() = runTest {
        val scope = this

        // Fake event manager that captures events into a flow
        val events = MutableSharedFlow<ObserverEvent>()

        val eventManager = object : IEventManager {
            override fun register(context: TriggerContext) = Unit
            override fun onEvent(event: ObserverEvent) {
                println("EventManager.onEvent CALLED")
                scope.launch {
                    events.emit(event)
                    println("EventManager.emit DONE")
                }
            }
        }

        // lifecycle termination observer that records whether job was cancelled
        var jobCancelled = false
        val terminator = object : IObserverLifecycleTerminator {
            override fun start(job: Job, finishFlow: Flow<Unit>) {
                this@runTest.launch {
                    finishFlow.first()
                    jobCancelled = true
                    job.cancel()
                }
            }
        }

        val source = MutableSharedFlow<Int>(replay = 1)
        val finishFlow = MutableSharedFlow<Unit>(replay = 0)

        val context = FlowObserverContext(
            flow = source,
            mapToEvent = { value -> ObserverEvent.FlowChanged(value) },
            event = ObserverEvent.FlowChanged(0),
            finishFlow = finishFlow
        )

        val observer = FlowObserver(scope, terminator)

        observer.start(eventManager, context)

        events.test {
            // emit first event: should arrive
            source.emit(1)
            val event = awaitItem() as ObserverEvent.FlowChanged<*>
            assertEquals(1, event.value)

            // now stop the observer
            finishFlow.emit(Unit)

            // collector must have been cancelled
            assertTrue(jobCancelled)

            // emit again: MUST NOT produce any new item
            source.emit(2)
            expectNoEvents() // turbine helper
        }
    }
}
