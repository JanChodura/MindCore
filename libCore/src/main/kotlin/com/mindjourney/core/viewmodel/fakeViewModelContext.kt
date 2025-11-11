package com.mindjourney.core.viewmodel

import com.mindjourney.core.navigation.NavigationCoordinator
import com.mindjourney.core.observer.AppObserver
import com.mindjourney.core.observer.trigger.model.TriggerResultConsumer
import com.mindjourney.core.tracking.ActiveScreenTrackerFactory
import com.mindjourney.core.util.logging.ILogger
import com.mindjourney.core.util.logging.LogDebugSwitch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

fun fakeViewModelContext(): ViewModelContext {

    val fakeLogger = object : ILogger {
        override fun d(tag: String, message: String, switch: LogDebugSwitch) {}
        override fun w(tag: String, message: String, throwable: Throwable?) {}
        override fun e(tag: String, message: String, throwable: Throwable?) {}
    }

    val fakeScope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
    val fakeTracker = ActiveScreenTrackerFactory.empty()
    val fakeAppObserver = AppObserver(
        scope = fakeScope,
        tracker = fakeTracker,
        logger = fakeLogger,
        resultConsumer = TriggerResultConsumer.empty()
    )

    val fakeNavigation = NavigationCoordinator.empty()

    val fakeCtx = ViewModelContext(
        logger = fakeLogger,
        navigation = fakeNavigation,
        screenTracker = fakeTracker,
        observer = fakeAppObserver,
        triggersContext = emptyList()
    )

    return fakeCtx
}
