package com.mindjourney.core.viewmodel

import androidx.lifecycle.ViewModel
import com.mindjourney.core.tracking.model.CoreScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel(), IBaseViewModel {

    abstract val ctx: ViewModelContext
    private var viewModelScope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    private var observerJob: Job? = null

    override val isReselectHappened: Boolean
        get() = ctx.screenTracker.reselect.isReselectHappened


    /** Initialize the ViewModel. Should be called once after creation specific ViewModel.
     *
     * Note: Avoid putting initialization logic in the init block of the ViewModel,
     * as it LEADS to issues with dependency injection frameworks like Hilt.
     */
    protected fun ensureInit(scope: CoroutineScope) {
        viewModelScope = scope

        viewModelScope.launch {
            ctx.isPrimaryObserverHolder.collectLatest { isPrimary ->
                if (isPrimary && observerJob == null) {
                    observerJob = launch {
                        val source = this::class.simpleName ?: "UnknownViewModel"
                        TriggerObserverInitializer(ctx, viewModelScope).initObservingTriggersIn(source)
                    }
                }
            }
        }
    }

    override fun navigateTo(screen: CoreScreen) {
        ctx.navigation.goTo(screen)
    }

    override fun navigateBack(screen: CoreScreen) {
        ctx.navigation.goBackTo(screen)
    }

    override fun navigateBack() {
        ctx.navigation.goBack()
    }

    override fun isActiveScreen(screen: CoreScreen) =
        ctx.screenTracker.activeScreen.value == screen

    override fun markPrimary() {
        ctx.isPrimaryObserverHolder.value = true
    }

    override fun onViewModelReady() {
        //specific ViewModel overrides it
        // default no-op
    }

    override fun onCleared() {
        super.onCleared()
        observerJob?.cancel()
        observerJob = null
    }
}
