package com.mindjourney.core.observer.trigger.model

import com.mindjourney.core.tracking.model.CoreScreen

sealed class TriggerResult {

    data object None : TriggerResult()
    data class Completed(val res: Boolean) : TriggerResult()
    data class NavigateToScreenFirstPage(val screen: CoreScreen) :
        TriggerResult()

    data class NavigateToFirstPage(val screen: CoreScreen) :TriggerResult()

    data class ShowDialog(val message: String) : TriggerResult()
    data class ExecuteAction(val actionId: String) : TriggerResult()

    /**
     * Used for custom trigger results in any app that don't fit predefined types.
     *
     * example: in TriggerConsumer again in the app module
     * if (result is TriggerResult.Generic && result.payload is MindRiseTriggerEffect.StartHabitActivityConvictionRead) {
     *     // ...
     * }
     */
    data class Generic(val payload: Any) : TriggerResult()
}
