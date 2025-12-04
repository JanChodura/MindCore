# Observer → Event → Trigger Architecture (Diagram)

Below is a single unified ASCII diagram showing the complete flow of the system, followed by two
concrete examples.

---

## 1. High‑Level System Diagram

```
          +------------------+
          |   FlowObserver   |
          |  (Flow<T> -> EV) |
          +---------+--------+
                    |
                    | ObserverEvent
                    v
          +------------------+
          |   EventManager   |
          | maps Event ->    |
          | registered        |
          | triggers          |
          +---------+--------+
                    |
                    | tryExecute()
                    v
          +------------------+
          |      Trigger     |
          | (IAppTrigger)    |
          +---------+--------+
                    |
                    | TriggerResult
                    v
          +-------------------------+
          |   TriggerResultConsumer |
          +-------------------------+
```

```
          +------------------+
          |   TimeObserver   |
          | (ticks -> EV)    |
          +---------+--------+
                    |
                    | ObserverEvent.TimeTick
                    v
          +------------------+
          |   EventManager   |
          +------------------+
```

---

## 2. Example: FlowObserver Triggering Navigation Reset

```
Flow<Int>: screenIndexFlow
    emits: 0, 1, 2, ...

FlowObserverContext:
    flow = screenIndexFlow
    mapToEvent = { idx -> FlowChanged(idx) }
    finishFlow = empty

TriggerContext:
    event = FlowChanged
    trigger = ResetPageTrigger
```

Flow:

```
   screenIndexFlow emits 3
          |
          v
   FlowObserver maps value -> ObserverEvent.FlowChanged(3)
          |
          v
   EventManager receives event
          |
          v
   ResetPageTrigger.tryExecute()
          |
          v
   TriggerResultConsumer.consume(result)
```

---

## 3. Example: TimeObserver Triggering Morning Routine

```
TimeObserverPolicy:
    start = 04:00
    end   = 06:00
    intervalMinutes = 5

TimeObserverContext:
    policy = above
    finishFlow = empty

TriggerContext:
    event = TimeTick
    trigger = MorningRoutineTrigger
```

Flow:

```
   TimeObserver emits TimeTick at 04:00
          |
          v
   EventManager routes event to MorningRoutineTrigger
          |
          v
   MorningRoutineTrigger.tryExecute()
          |
          v
   TriggerResultConsumer.consume(result)
```

---

## Summary

* Observers only emit events.
* EventManager only routes events.
* Triggers only execute domain logic.
* TriggerResultConsumer receives outcomes.

This diagram shows the complete path: **Observer → Event → EventManager → Trigger → ResultConsumer
**.
