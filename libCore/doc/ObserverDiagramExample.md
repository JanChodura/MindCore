# ObserverArchitecture_Diagrams.md

ASCII DIAGRAM – UPDATED OBSERVER ARCHITECTURE
=============================================

                     +----------------------------+
                     |      ActiveScreenTracker   |
                     |   (detects screen changes) |
                     +--------------+-------------+
                                    |
                                    v
                         +----------+---------------+
                         |    AppScreenObserver     |
                         |  (selects TriggerContext |
                         |   for active screen)     |
                         +-----------+--------------+
                                     |
                                     v
                        +------------+-------------+
                        |         TriggerManager   |
                        |  (holds active contexts, |
                        |   exposes result flow)   |
                        +------------+-------------+
                                     |
                                     v
                        +------------+--------------+
                        |        TriggersLauncher   |
                        | - splits polling/reactive |
                        | - starts both types       |
                        +------------+--------------+
                                     |
               +---------------------+-----------------------+
               |                                             |
               v                                             v
    +-----------------------------+         +----------------------------------+
    |     Polling Triggers        |         |        Reactive Triggers         |
    | (implement IPollingTrigger) |         |     (extend ReactiveTrigger<T>)  |
    +--------------+--------------+         +------------------+---------------+
               |                                               |
          Tick | cycle, intervalSec,                           | sourceFlow event:
       emitted |  daily time window                            | - change of screen
     by  create|TickStream                                     | - user interaction
               v                                               v - repository update
    +----------+-------------------+         +-----------------+----------------+
    | TriggerPoll.createTickStream |         |     startReactiveFlow()          |
    +----------+-------------------+         | - sets isReady=true on 1st event |
               |                             +-----------------+----------------+
                   |                                           | isReady=true 
               v                                               v
      +--------+--------+                          +-----------+------------+
      | tryExecute()    |                          |     tryExecute()       |
      | (business logic)|                          |  (business logic)      |
      +--------+--------+                          +-----------+------------+
               |                                               |
               | TriggerResult                                 | TriggerResult
               +------------ ------v---------------------------+
                        +----------+-----------+
                        |   TriggerResult      |
                        +----------+-----------+
                                   |
                                   v
        +--------------------------+----------------------+
        |                 TriggerResultConsumer           |
        | - navigation                                    |
        | - dialogs                                       |
        | - generic effects (MindRiseTriggerEffect)       |
        +-------------------------+-----------------------+
                                  |
                                  v
                        +---------+--------+
                        |   UI / ViewModel |
                        | - update state   |
                        | - navigate       |
                        +------------------+


EXAMPLE – REACTIVE TRIGGER
==========================

class ScreenChangedTrigger(
screenFlow: Flow<CoreScreen>,
) : ReactiveTrigger<CoreScreen>(
sourceFlow = screenFlow,
description = TriggerDescription("screenChanged")
) {

    override suspend fun tryExecute(): TriggerResult {
        // Whenever screen changes, mark trigger as ready.
        // Sometimes that's all that's needed → no result.
        return TriggerResult.None
    }
}


EXAMPLE – POLLING TRIGGER
==========================

class MorningWindowTrigger : IPollingTrigger {

    override val isComplete = MutableStateFlow(true)
    override var description = TriggerDescription("morningWindow")

    override suspend fun tryExecute(): TriggerResult {
        val now = LocalTime.now()
        val isWithinMorning = now.isAfter(LocalTime.of(5,0)) &&
                              now.isBefore(LocalTime.of(8,0))

        return if (isWithinMorning) {
            TriggerResult.ExecuteAction("MorningCheck")
        } else TriggerResult.None
    }
}


NOTES
=====
- Polling triggers are driven by Tick(cycle, intervalSec).
- Reactive triggers are driven by sourceFlow events.
- ReactiveTrigger.ready == true means: "trigger has already received first event".
- Many reactive triggers use isReady only (ready signal) without emitting any TriggerResult.
- Both trigger types share tryExecute() contract.
