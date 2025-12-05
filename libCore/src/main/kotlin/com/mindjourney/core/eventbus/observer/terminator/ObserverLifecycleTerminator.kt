import com.mindjourney.core.eventbus.observer.terminator.IObserverLifecycleTerminator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Utility that listens to a finish signal and cancels a running coroutine job.
 *
 * Observers use this to support externally controlled shutdown.
 * No event logic or business behaviour is performed here.
 */
class ObserverLifecycleTerminator @Inject constructor(
    private val scope: CoroutineScope
) : IObserverLifecycleTerminator {

    private val _finishFlow = MutableSharedFlow<Unit>()
    override val terminatedFlow: Flow<Unit> get() = _finishFlow

    /**
     * Cancels [job] when [finishFlow] emits a value.
     */
    override fun tryTerminate(job: Job, finishFlow: Flow<Unit>) {
        scope.launch {
            finishFlow.collectLatest {
                job.cancel()
                _finishFlow.emit(Unit)
                this.cancel() // stop listener
            }
        }
    }
}
