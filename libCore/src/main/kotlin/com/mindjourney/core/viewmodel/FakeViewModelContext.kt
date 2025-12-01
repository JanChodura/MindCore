package com.mindjourney.core.viewmodel

import com.mindjourney.core.navigation.NavigationCoordinator
import com.mindjourney.core.observer.AppObserver
import com.mindjourney.core.observer.trigger.model.TriggerResultConsumer
import com.mindjourney.core.tracking.ScreenTrackerFactory
import com.mindjourney.core.util.logging.ILogger
import com.mindjourney.core.util.logging.LogDebugSwitch
import com.mindjourney.core.viewmodel.helper.ViewModelContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

fun FakeViewModelContext(): ViewModelContext {

    val fakeLogger = object : ILogger {
        override fun d(tag: String, message: String, switch: LogDebugSwitch) {}
        override fun w(tag: String, message: String, throwable: Throwable?) {}
        override fun e(tag: String, message: String, throwable: Throwable?) {}
    }

    val fakeScope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
    val fakeTracker = ScreenTrackerFactory.empty()
    val fakeAppObserver = AppObserver(
        scope = fakeScope,
        tracker = fakeTracker,
        resultConsumer = TriggerResultConsumer.empty()
    )

    val fakeNavigation = NavigationCoordinator.empty()

    val fakeCtx = ViewModelContext(
        navigation = fakeNavigation,
        isPrimary = false,
        screenTracker = fakeTracker,
        observer = fakeAppObserver,
        triggersContext = emptyList()
    )

    return fakeCtx
}
