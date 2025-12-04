import com.mindjourney.core.eventbus.observer.terminator.IObserverLifecycleTerminator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Utility that listens to a finish signal and cancels a running coroutine job.
 *
 * Observers use this to support externally controlled shutdown.
 * No event logic or business behaviour is performed here.
 */
class ObserverLifecycleTerminator(
    private val scope: CoroutineScope
) : IObserverLifecycleTerminator {

    /**
     * Cancels [job] when [finishFlow] emits a value.
     */
    override fun start(job: Job, finishFlow: Flow<Unit>) {
        scope.launch {
            finishFlow.collectLatest {
                job.cancel()
                this.cancel() // stop listener
            }
        }
    }
}
