package com.mindjourney.core.viewmodel

import androidx.lifecycle.ViewModel
import com.mindjourney.core.tracking.model.CoreScreen
import com.mindjourney.core.viewmodel.helper.INavigationFacade
import com.mindjourney.core.viewmodel.helper.IReactiveHandler
import com.mindjourney.core.viewmodel.helper.NavigationFacade
import com.mindjourney.core.viewmodel.helper.PipelinePhaseEnum
import com.mindjourney.core.viewmodel.helper.ReactiveHandler
import com.mindjourney.core.viewmodel.helper.ViewModelContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow

abstract class BaseViewModel : ViewModel(), IBaseViewModel {

    abstract val ctx: ViewModelContext
    private var viewModelScope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    override val navigationFcd: INavigationFacade by lazy {
        NavigationFacade(ctx)
    }

    protected lateinit var reactiveManager: IReactiveHandler

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

        reactiveManager = ReactiveHandler(
            ctx,
            viewModelScope,
            isPrimary,
            ::onScreenBecameActive,
            ::onViewModelInitialized
        )
        reactiveManager.initialize()
        pipelinePhase.value = PipelinePhaseEnum.PREINITIALIZED
    }

    override fun onViewModelInitialized() {
        //specific ViewModel overrides it
        // default no-op
    }

    override fun onCleared() {
        reactiveManager.clear()
        super.onCleared()
    }
}
