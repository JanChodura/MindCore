-- README_TECH_corelib

-- ============================================
-- 1. OVERVIEW
-- ============================================
-- The libCore module is the foundational layer of the MindRise ecosystem.
-- It provides the essential architecture and utilities that all higher modules
-- (such as mindriseApp) depend on. libCore defines the reactive backbone for
-- state management, logging, navigation, and event observation.
-- It is not feature-specific but serves as a shared runtime for all app modules.

-- The module focuses on consistency, simplicity, and testability.
-- All communication between ViewModels, managers, and UI layers is built on top
-- of libCore abstractions.


-- ============================================
-- 2. TECHNOLOGICAL BACKGROUND
-- ============================================
-- libCore is built on Kotlin and designed for Android projects using the
-- Jetpack ecosystem. The key technologies and principles include:
--   • Dependency Injection via Hilt (Dagger)
--   • Reactive data flows using Kotlin coroutines and StateFlow/SharedFlow
--   • Clean separation of concerns with modular design
--   • Thread-safe logging and observer mechanisms

-- Hilt provides automatic injection of shared dependencies (Logger, Dispatcher,
-- Tracker, DataStore repository). libCore defines these dependencies centrally
-- and exposes them to higher layers as injectable components.


-- ============================================
-- 3. ARCHITECTURAL APPROACH
-- ============================================
-- libCore implements the reactive foundation of an MVVM-style architecture.
-- It defines the core abstractions that enable unidirectional data flow:
--   UI -> ViewModel -> Manager -> libCore service.
--
-- Each subsystem serves a distinct purpose:
--   • Logger: unified structured logging interface with configuration provider.
--   • Navigation: state-driven navigation system built around ActiveScreenTracker.
--   • Observer: event triggers and background observation layer for app-wide events.
--   • Data: DataStoreRepository for persistent key-value storage.
--
-- The design avoids circular dependencies and promotes pure reactive patterns.
-- All state changes propagate through StateFlow or SharedFlow rather than
-- callbacks or global variables.
--
-- Additionally, libCore defines a shared ViewModel data class called ViewModelContext.
-- This class groups frequently used dependencies such as ILogger, NavigationDispatcher,
-- ActiveScreenTracker, IConvictionManager, and AppObserver. It simplifies dependency
-- injection for ViewModels by providing a single entry point, reducing boilerplate
-- and maintaining consistent access to core services across all ViewModels.


-- ============================================
-- 4. MAIN COMPONENTS
-- ============================================
-- 4.1 Logger subsystem
--   Provides ILogger interface and AppLogger implementation for structured logging.
--   The LoggerProvider ensures controlled access for both DI and test environments.
--
-- 4.2 Navigation subsystem
--   NavigationDispatcher, NavigationCoordinator, and ActiveScreenTracker manage
--   reactive navigation state and transitions between screens.
--
-- 4.3 Observer subsystem
--   AppObserver and IAppTrigger provide a reactive event system to trigger
--   domain-level actions (such as daily resets or background updates).
--
-- 4.4 Data persistence subsystem
--   DataStoreRepository wraps Android DataStore<Preferences> to provide async
--   persistence with coroutine scope management.
--
-- 4.5 ViewModel context abstraction
--   The ViewModelContext serves as a dependency aggregator for ViewModels.
--   Instead of injecting multiple shared services individually, developers inject
--   this single context object. It provides cleaner constructor signatures and
--   centralizes lifecycle-related logic.
--
-- All components are injectable and independent of UI code, allowing libCore
-- to be reused in multiple app layers or modules.


-- ============================================
-- 5. USAGE IN OTHER MODULES
-- ============================================
-- libCore is included as a dependency in higher-level modules through Gradle:
--   implementation(project(":libCore"))
-- This grants access to all DI-provided services such as:
--   • ILogger (logging)
--   • NavigationDispatcher / ActiveScreenTracker (navigation)
--   • AppObserver (event observation)
--   • DataStoreRepository (preferences persistence)
--   • ViewModelContext (shared ViewModel dependency container)
--
-- Higher modules (like mindriseApp) inject these components into their
-- ViewModels, Managers, and other runtime classes.
-- No additional configuration is required beyond the Gradle inclusion and
-- Hilt initialization in the Application class.


-- ============================================
-- END OF README
