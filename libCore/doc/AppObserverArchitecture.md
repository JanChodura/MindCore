# Observer–Event–Trigger Architecture

This document defines the unified architecture for observer-driven event processing and trigger
execution. It describes how observers emit events, how events are dispatched, and how triggers
perform domain actions.

The system is composed of five layers:

1. **Observers** – Sources of events
2. **Observer Contexts** – Declarative configuration of observers
3. **Event Manager** – Central routing of events
4. **Trigger Contexts** – Declarative bindings between events and triggers
5. **Triggers** – Domain logic executed in response to events

Each layer has a single responsibility and is fully decoupled from the others.

---

## 1. Observers

Observers watch external signals and convert them into `ObserverEvent` objects. They never contain
domain logic.

### 1.1 FlowObserver

* Observes any `Flow<T>`.
* Each emission is mapped to an `ObserverEvent` through a provided mapping function.
* Emits the event to the `EventManager`.
* Can be externally terminated via a finish-flow.

### 1.2 TimeObserver

* Emits `ObserverEvent.TimeTick` at intervals defined by `TimeObserverPolicy`.
* Active only within a configured time window.
* Terminable via a finish-flow.

Observers never evaluate whether an event "should" occur — they only emit.

---

## 2. Observer Contexts

Observer contexts describe how observer instances should behave.

### 2.1 FlowObserverContext

Contains:

* A source `flow`.
* A `mapToEvent` function converting values to events.
* A declared event-type for diagnostics.
* An optional finish-flow.

### 2.2 TimeObserverContext

Contains:

* A time policy specifying interval, start, and end.
* A finish-flow.

Contexts are passive configuration objects and contain no logic.

---

## 3. Observer Initialization

Initialization links observer contexts with concrete observer instances.

### 3.1 IObserverInitializer

Defines a single method:

```
initialize(contexts: List<ObserverContext>)
```

It activates all observers declared in the contexts.

### 3.2 ObserverInitializer (abstract)

Provides shared wiring logic:

* For each `FlowObserverContext` → start `FlowObserver`
* For each `TimeObserverContext` → start `TimeObserver`

### 3.3 GlobalObserverInitializer

Registers global observers used across the entire application.

### 3.4 DomainObserverInitializer

Registers observers declared within a specific ViewModel or feature.

---

## 4. Event System

All observers emit `ObserverEvent` objects. These are sent into the `EventManager`, the central
event router.

### 4.1 EventManager

Responsibilities:

* Maintain mapping: `EventType → List<IAppTrigger>`.
* On `onEvent(event)`, locate all triggers bound to the event.
* Execute each trigger.
* Wrap results in a `TriggerResult` and forward them to a `TriggerResultConsumer`.

The EventManager contains no domain logic; it only dispatches.

---

## 5. Trigger Contexts

A `TriggerContext` binds one trigger to one event.

Fields:

* `trigger: IAppTrigger`
* `event: ObserverEvent`

Each trigger must be associated with exactly one event type.

### 5.1 GlobalTriggerInitializer

Registers trigger contexts required at the application level.

### 5.2 DomainTriggerInitializer

Registers trigger contexts defined by a ViewModel or feature.

---

## 6. Triggers

Triggers contain pure domain logic.

### 6.1 IAppTrigger

* Exposes `isReady: StateFlow<Boolean>`.
* Exposes metadata description.
* Implements `suspend fun tryExecute(): TriggerResultType`.

### 6.2 BaseAppTrigger

Shared implementation handling readiness and description.

Triggers cannot:

* Observe flows directly.
* Dispatch events.
* Start coroutines outside `tryExecute()`.

They simply react to one event.

---

## 7. Trigger Execution

When EventManager receives an event:

1. Identify all triggers bound to that event.
2. Skip triggers where `isReady == false`.
3. Execute `tryExecute()`.
4. Wrap result as `TriggerResult`.
5. Forward to `TriggerResultConsumer`.

`TriggerResult` contains:

* result type
* event description
* triggering event
* timestamp
* optional metadata

---

## 8. Lifecycle Termination

`IObserverLifecycleTerminator` allows observers to be stopped when receiving a finish signal.

This provides manual control for:

* ViewModel-scoped observers
* Feature-specific observers
* Test environments

Termination cancels only the observer job, not the surrounding scope.

---

## 9. Summary

This architecture guarantees:

* Strict separation of observer logic and domain logic.
* A single dispatch point (EventManager).
* Declarative configuration via contexts.
* Predictable trigger execution.
* Clean global vs domain-level extensibility.

Observers → Events → EventManager → Triggers

Each step is explicit, testable, and isolated.
