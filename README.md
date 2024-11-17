# Hybrid AR App

An Augmented Reality application implementing MVVM (Model-View-ViewModel) architecture pattern with
clean architecture principles.

## Architecture Overview

### Use cases

- Isolate business logic in self contained units called use cases.

### MVVM

- The ViewModel is responsible for handling UI logic and state management, communicating between
  Views and Use Cases.

### UDF

- Unidirectional Data Flow (UDF) is used to ensure a clear and predictable data flow within the app.
- Easy to switch from regular Android views to Jetpack compose

#### Domain Layer

- **Use Cases**: Contain business logic and orchestrate data flow between ViewModels and
  Repositories
- **Entities**: Core business models
- **Repository Interfaces**: Define contracts for data operations

#### Data Layer

- **Repositories**: Implement repository interfaces and manage data operations
- **Models**: Data transfer objects and mapping logic
-

#### Presentation Layer

- **Views**: UI components responsible for rendering the AR experience and user interface
- **ViewModels**: Handle UI logic and state management, communicating between Views and Use Cases

### Key Use cases

- GetInitialWorldPosition: Get the initial world position
- CreateAndAddCube: Creates and adds a new cube at the specified tap location
- Calculate angle of the arrow pointing to the current selected cube

### Modules

- **domain** - Contains the core business logic and entities
- **data** - Contains the data layer, including repositories and models
- **presentation (app)** - Contains the presentation layer, including views and view models

## Dependencies

- ARCore for AR functionality
- SceneView as a replacement for deprecated Sceneform
- Koin for dependency injection
- Coroutines for asynchronous operations
- JUnit for testing

## Unit Tests

The project includes comprehensive unit tests for:

- Use Cases: Testing business logic and data flow
- Repositories: Testing data operations and mapping
- Models: Testing data transformations

Run tests using:

```bash
./gradlew test
```

## Getting Started

[]