# Hybrid AR App

An Augmented Reality application that allows the user to place cubes on detected planes.

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

#### Presentation Layer

- **Views**: UI components responsible for rendering the AR experience and user interface
- **ViewModels**: Handle UI logic and state management, communicating between Views and Use Cases

### Use cases

- `GetInitialWorldPositionUseCase`: Get the initial world position
- `CreateCubeUseCase`: Creates a new cube with random name, color and fixed sample size
- `AddCubeToViewUseCase`: Combines `GetInitialWorldPositionUseCase` and `CreateCubeUseCase` to
  create
  a new cube and forward origin offset
- Calculate angle of the arrow pointing to the current selected cube

### Modules

- **domain** - Contains the core business logic and entities
- **data** - Contains the data layer, including repositories and models
- **presentation (app)** - Contains the presentation layer, including views and view models

## Dependencies

- ARCore for AR functionality
- [SceneView](https://github.com/SceneView/sceneview-android) as a replacement for
  deprecated [Sceneform](https://developers.google.com/sceneform/develop)
- Koin for dependency injection
- Coroutines for asynchronous operations
- JUnit for testing

## Unit Tests

The project includes unit tests for:

- Use Cases: Testing business logic and data flow
- Repositories: Testing data operations and mapping
- Models: Testing data transformations

Run tests using:

```bash
./gradlew test
```

## Getting Started

### Configure API URL

The default development URL is defined in `ApiService.kt` companion object. To change it for your
environment:

1. Update the `DEV_URL` constant in `ApiService.kt` to match your API endpoint:

```kt
companion object {
    val DEV_URL = "https://your-api-url"
}
```

2. Open project folder in Android Studio (Android Studio Koala | 2024.1.1 or newer) and run the app
   module

## Questions

### Why SceneView instead of Sceneform?

Sceneform (originally developed by Google) is deprecated and no longer maintained. SceneView is the
only successor of Sceneform.
Both libraries are based on ARCore, which is the official Google library for AR development.

## Limitations

### Camera origin offset

The camera origin offset is not supported in SceneView. Claim is that the ARCore controls the camera
position,
from
SceneView's [code comments](https://github.com/SceneView/sceneview-android/blob/2969a2c5ef00e5e5a0bccb29053e33fd93fcc47d/arsceneview/src/main/java/io/github/sceneview/ar/node/ARCameraNode.kt#L4)
on `ARCameraNode`:
> Represents a virtual camera, which determines the perspective through which the scene is viewed.
>
> If the camera is part of an [ARSceneView], then the camera automatically tracks the
> camera pose from ARCore.
> The following methods will throw [ ] when called:
> - [parent] - CameraNode's parent cannot be changed, it is always the scene.
> - [position] - CameraNode's position cannot be changed, it is controlled by the ARCore camera
    > pose.
> - [rotation] - CameraNode's rotation cannot be changed, it is controlled by the ARCore camera
    > pose.
>
> All other functionality in Node is supported. You can access the position and rotation of the
> camera, assign a collision shape to the camera, or add children to the camera. Disabling the
> camera turns off rendering.


