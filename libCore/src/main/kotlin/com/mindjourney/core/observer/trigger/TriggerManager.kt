package com.mindjourney.core.observer.trigger

import com.mindjourney.core.observer.trigger.model.TriggerPool
import com.mindjourney.core.observer.trigger.model.TriggerResult
import com.mindjourney.core.observer.trigger.model.TriggerResultConsumer
import com.mindjourney.core.observer.trigger.util.TriggerContext
import com.mindjourney.core.util.logging.injectedLogger
import com.mindjourney.core.util.logging.on
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Coordinates trigger lifecycle at application level.
 *
 * Responsible for:
 *  - storing registered triggers,
 *  - initiating their execution via [TriggersLauncher],
 *  - exposing a unified result stream via [triggerResult].
 *
 * Business logic remains inside individual triggers; this class
 * manages orchestration and result propagation.
 */
class TriggerManager(
    private val scope: CoroutineScope,
) {

    private val log = injectedLogger<TriggerManager>(on)
    private val triggers: MutableList<TriggerContext> = mutableListOf()

    private val _triggerResult = MutableSharedFlow<TriggerResult>(
        replay = 0,
        extraBufferCapacity = 16
    )
    val triggerResult: SharedFlow<TriggerResult> = _triggerResult

    private val triggersLauncher = TriggersLauncher(
        startTrigger = {
            log.d("Starting trigger: ${it.trigger.description.name}")
            triggerSelector.init(it)
        }
    )

    private val triggerSelector = TriggerInitializer(scope) { trigger, resultType ->
        scope.launch {
            _triggerResult.emit(TriggerResult(trigger.description, resultType))
        }
    }

    /** Initializes triggers*/
    fun registerTriggers(triggersFlow: StateFlow<List<TriggerContext>> = MutableStateFlow(emptyList())) {
        log.d("Initializing triggers from flow of size=${triggersFlow.value.size}")
        triggers.clear()
        triggers.addAll(triggersFlow.value)
    }

    /** Starts processing all triggers, deciding between polling and reactive modes. */
    fun evaluateTriggers() {

        val toStart = triggers.filter { ctx ->
            val key = ctx.trigger.description.name
            !TriggerPool.isStarted(key)
        }
        toStart.forEach { ctx ->
            val key = ctx.trigger.description.name
            TriggerPool.markStarted(key)
        }

        log.d("Starting triggers:${toStart.size}. Already started triggers:${triggers.size - toStart.size}")
        triggersLauncher.evaluate(toStart)
    }


    /** Determines trigger type and starts appropriate observation logic. */

    fun observeTriggerResults(resultConsumer: TriggerResultConsumer) {
        scope.launch {
            log.d("Observing trigger results to consume them.")
            triggerResult.collectLatest {
                log.d("Emitting trigger result: $it")
                resultConsumer.consume(it)
            }
        }
    }
}
