package com.mindjourney.core.viewmodel

import androidx.lifecycle.ViewModel
import com.mindjourney.core.tracking.model.CoreScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel(), IBaseViewModel {

    abstract val ctx: ViewModelContext
    private var viewModelScope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    override val isPrimary = false
    protected var isFirstActivation = true

    override val isReselectHappened: Boolean
        get() = ctx.screenTracker.reselect.isReselectHappened

    private var baseScreen: CoreScreen = CoreScreen.Unknown

    protected val pipelinePhase = MutableStateFlow(PipelinePhaseEnum.NOT_STARTED)

    protected open fun onScreenBecameActive() {}

    /** Initialize the ViewModel. Should be called once after creation specific ViewModel.
     *
     * Note: Avoid putting initialization logic in the init block of the ViewModel,
     * as it LEADS to issues with dependency injection frameworks like Hilt.
     */
    protected fun ensureInit() {
        val isSimpleViewModel = ctx == null
        if (isSimpleViewModel) {
            return
        }

        baseScreen = ctx.screenTracker.activeScreen.value
        initObservers()
        pipelinePhase.value = PipelinePhaseEnum.PREINITIALIZED
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

    private fun initObservers() {
        viewModelScope.launch {
            initScreenObserver()
        }
        viewModelScope.launch {
            initCustomObservers()
        }
    }

    private fun initCustomObservers() {
        val source = this::class.simpleName ?: "UnknownViewModel"
        TriggerObserverInitializer(ctx, viewModelScope, isPrimary).initObservingTriggersIn(source)
    }

    private suspend fun initScreenObserver() {
        ctx.screenTracker.state.activeScreen.collect { screen ->
            if (screen == baseScreen) {
                onScreenBecameActive()
            }
        }

    }

    override fun onViewModelInitialized() {
        //specific ViewModel overrides it
        // default no-op
    }

    override fun onCleared() {
        super.onCleared()
    }
}
