package com.mindjourney.core.viewmodel

import com.mindjourney.core.navigation.NavigationCoordinator
import com.mindjourney.core.observer.AppScreenObserver
import com.mindjourney.core.observer.trigger.model.TriggerResultConsumer
import com.mindjourney.core.tracking.ScreenTrackerFactory
import com.mindjourney.core.util.logging.ILogger
import com.mindjourney.core.util.logging.LogDebugSwitch
import com.mindjourney.core.viewmodel.helper.ViewModelContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

fun createFakeViewModelContext(): ViewModelContext {

    val fakeLogger = object : ILogger {
        override fun d(tag: String, message: String, switch: LogDebugSwitch) {}
        override fun w(tag: String, message: String, throwable: Throwable?) {}
        override fun e(tag: String, message: String, throwable: Throwable?) {}
    }

    val fakeScope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
    val fakeTracker = ScreenTrackerFactory.empty()
    val fakeAppScreenObserver = AppScreenObserver(
        scope = fakeScope,
        tracker = fakeTracker,
        resultConsumer = TriggerResultConsumer.empty()
    )

    val fakeNavigation = NavigationCoordinator.empty()

    val fakeCtx = ViewModelContext(
        navigation = fakeNavigation,
        source = "FakeSource",
        screenTracker = fakeTracker,
        observer = fakeAppScreenObserver,
        triggersContext = emptyList()
    )

    return fakeCtx
}
