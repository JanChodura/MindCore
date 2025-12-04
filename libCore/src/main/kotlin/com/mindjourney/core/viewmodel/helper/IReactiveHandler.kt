package com.mindjourney.core.viewmodel.helper

import com.mindjourney.core.eventbus.model.trigger.IAppTrigger
import kotlin.reflect.KClass

/**
 * Manages reactive observers associated with a ViewModel.
 *
 * This handler forms part of the **reaction layer** of the architecture:
 * it tracks trigger readiness, binds trigger observers to the ViewModel lifecycle,
 * and prepares the ViewModel for a safe initialization moment.
 *
 * Responsibilities:
 * - prepare all reactive observers (`initialize`)
 * - clear all observers when ViewModel is destroyed (`clear`)
 * - refresh trigger bindings after trigger list changes (`updateTriggers`)
 * - allow ViewModel to wait for readiness of a specific trigger (`observeTriggerReadiness`)
 *
 * This component never performs domain logic.
 * It only manages *when* the ViewModel is allowed to react,
 * not *what* the ViewModel should do.
 */
interface IReactiveHandler {

    /**
     * Initializes all necessary reactive observers.
     * Called when the ViewModelContext is attached and the ViewModel is entering
     * its reactive lifecycle.
     */
    fun initialize()

    /**
     * Clears all observers.
     * Called when the ViewModel is being disposed or replaced as a primary observer holder.
     */
    fun clear()

    /**
     * Observes readiness of a specific trigger type.
     *
     * When the first trigger of the given class reports readiness (isReady == true),
     * the provided callback is executed.
     *
     * This allows ViewModel to start logic only when certain
     * reactive preconditions are satisfied.
     *
     * @param triggerClass The trigger type to wait for.
     * @param onTriggerReady Invoked once when the trigger becomes ready.
     */
    fun <T : IAppTrigger> observeTriggerReadiness(
        triggerClass: KClass<T>,
        onTriggerReady: () -> Unit
    )
}
