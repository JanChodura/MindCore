OBSERVER ARCHITECTURE (MindJourney – Updated Reactive/Polling Trigger System)
=============================================================================

1. EventTriggersModule (Dependency Injection configuration)
-----------------------------------------------------------
Defines all triggers used by the application and groups them into TriggerContext sets.

Each TriggerContext contains:
- description: TriggerDescription (name + source)
- trigger: implementation of IAppTrigger
- optional polling configuration (pollCycles, pollIntervalSec)

Hilt bindings examples:
- @Named("ConvictionTriggers") → triggers active on Conviction screen
- @Named("GlobalTriggers")     → triggers active regardless of screen

This module is the registration layer for the entire observer system.


2. AppScreenObserver (Application Orchestrator)
------------------------------------------------
Coordinates high-level trigger execution in reaction to:
- screen changes
- application lifecycle states
- ViewModel-provided triggersFlow

Responsibilities:
- receives StateFlow<List<TriggerContext>> for the current screen or feature
- configures TriggerManager with the active TriggerContexts
- listens to ActiveScreenTracker for screen switches
- restarts trigger evaluation when screen context changes
- collects TriggerResult from TriggerManager
- passes results to TriggerResultConsumer for handling

AppScreenObserver is the central control tower integrating:
- UI screen state
- trigger orchestration
- navigation and app reactions


3. TriggerManager (Coordinator of trigger lifecycle)
-----------------------------------------------------
Core responsibilities:
- stores active TriggerContext list
- delegates trigger start logic to TriggersLauncher
- exposes unified output stream: StateFlow<TriggerResult>
- forwards every non-None TriggerResult to observers

TriggerManager does NOT:
- manage job lifecycles
- perform scheduling logic
- collect flows directly (TriggerInitializer does that)

It acts as the container and lifecycle entrypoint for all triggers.


4. TriggersLauncher (Runner of polling and reactive triggers)
--------------------------------------------------------------
Starts all triggers based on their type.

Flow:
- splits TriggerContexts into polling and reactive groups
- launches polling triggers every time (safe to restart)
- launches reactive triggers every time (reactiveJobs removed for clarity)

It delegates actual trigger execution wiring to TriggerInitializer.


5. TriggerInitializer (Low-level wiring of trigger execution)
--------------------------------------------------------------
Determines how each trigger is executed:

- If trigger is IPollingTrigger:
  createTickStream()
  collectLatest { trigger.tryExecute() }

- If trigger is IReactiveTrigger:
  trigger.startReactiveFlow(scope, description, onResult)

TriggerInitializer is the only place that subscribes to flows.


6. TriggerPoll (Tick stream generator – cleaned version)
---------------------------------------------------------
TriggerPoll is now stateless and contains only one function:

createTickStream(scope, PollConfig, TriggerDescription)

This produces a private MutableStateFlow<Tick> containing:
- TickFactory.init(description)
- sequence of periodic ticks
- final tick with isFinal = true

No global state. No job storage. No start/stop API.

It is a pure utility generating time-based impulses for polling triggers.


7. IAppTrigger IMPLEMENTATIONS
-------------------------------
All triggers implement:

suspend fun tryExecute(): TriggerResult
val isReady: StateFlow<Boolean>
var description: TriggerDescription

Two categories exist:

A) Reactive triggers:
- implement IReactiveTrigger
- extend ReactiveTrigger<T>
- provide sourceFlow (Flow<T>)
- startReactiveFlow is called by TriggerInitializer

B) Polling triggers:
- implement IPollingTrigger (marker only)
- do NOT expose flows
- their evaluation is triggered by ticks from TriggerPoll

Example polling trigger:
class MorningResetTrigger : IPollingTrigger {
override suspend fun tryExecute(): TriggerResult { ... }
}


8. FlowBasedTrigger (Base for reactive triggers – simplified)
--------------------------------------------------------------
Defines only:
abstract val sourceFlow: Flow<T>

Used as a lightweight parent for ReactiveTrigger.
It contains no start logic and no internal scheduling.


9. ReactiveTrigger (Concrete reactive trigger base class)
----------------------------------------------------------
Extends FlowBasedTrigger<T> and implements IReactiveTrigger.

Responsibilities:
- holds sourceFlow
- exposes isReady (true after first emission)
- starts flow collection via startReactiveFlow()
- logs and forwards non-None results via onResult callback

ReactiveTrigger is the canonical implementation for stream-driven triggers.


10. TriggerResult (Unified output contract)
-------------------------------------------
Standard results:
- None
- Completed(res)
- NavigateToScreenFirstPage(screen)
- ShowDialog(message)
- ExecuteAction(actionId)
- Generic(payload)

Generic allows passing custom domain data.


11. App-specific trigger effects (e.g., MindRiseTriggerEffect)
---------------------------------------------------------------
These are wrapped inside:
TriggerResult.Generic(payload)


12. TriggerResultConsumer (Application behavior adapter)
--------------------------------------------------------
Consumes TriggerResult emitted by TriggerManager.

Responsibilities:
- handle navigation via ActiveScreenTracker
- show dialogs
- execute domain-specific actions
- interpret Generic payloads

This is the bridge between framework-level triggers and real UI actions.


13. Full Observer Flow (MindJourney)
------------------------------------
User Action / Time Event
↓
ActiveScreenTracker (detects screen changes)
↓
AppScreenObserver (configures and restarts triggers)
↓
TriggerManager (holds and exposes TriggerContext list)
↓
TriggersLauncher (splits & starts trigger types)
↓
TriggerInitializer
- Polling: TickStream → tryExecute()
- Reactive: sourceFlow → tryExecute()
↓
TriggerResult (Navigate / Dialog / Action / Generic)
↓
TriggerResultConsumer.consume(result)
↓
UI / ViewModel / Navigation


SUMMARY
-------
The Observer Architecture is a clean, modular, reactive framework for:
- screen-aware trigger orchestration
- time-based evaluation
- reactivity to state changes
- unified trigger-result processing

Each layer has one clear responsibility:
- Trigger: what should happen
- TriggerPoll: when it should happen
- TriggerInitializer: how it starts
- TriggerManager: which triggers are active
- AppScreenObserver: why they run (screen context)
- TriggerResultConsumer: how the app responds

This ensures testability, separation of concerns,
and predictable asynchronous behavior across the application.
