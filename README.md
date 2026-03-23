# 📱 QR Generator - KMP

![Kotlin](https://img.shields.io/badge/Kotlin-2.3.20-grey?style=flat&logo=kotlin&logoColor=white&labelColor=7F52FF)
![Android](https://img.shields.io/badge/Android-Supported-grey?style=flat&logo=android&logoColor=white&labelColor=green)
![iOS](https://img.shields.io/badge/iOS-Supported-grey?style=flat&logo=apple&logoColor=white&labelColor=000000)
![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.10.2-grey?style=flat&logo=jetpackcompose&logoColor=white&labelColor=blue)
![Testing](https://img.shields.io/badge/Testing-Kotest%20%2F%20Mokkery-grey?style=flat&logo=junit5&logoColor=white&labelColor=orange)
![Android Testing](https://img.shields.io/badge/Android%20Robolectric%20%2B%20Roborazzi-grey?style=flat&logo=android&logoColor=white&labelColor=green)
![Min SDK](https://img.shields.io/badge/Min%20SDK-23-grey?style=flat&labelColor=green)
![License](https://img.shields.io/badge/License-MIT-grey?style=flat&labelColor=yellow)

Multiplatform application developed with **Kotlin Multiplatform (KMP)** for generating and customizing QR codes. The project uses **Compose Multiplatform** to share logic and user interface between Android and iOS.

---

## 🏛️ Architecture

The project uses a clean architecture approach where business logic and UI are centralized in the `:composeApp` module, organized by layers within `commonMain`.

```
androidApp  → :composeApp (shared module)
iosApp      → :composeApp (shared module via framework)
:composeApp → commonMain (UI, Presentation, Domain, Navigation, DI)
```

| Layer (Packages) | Responsibility |
|---|---|
| `ui` | Common visual components, themes, and screens developed with Compose Multiplatform. |
| `presentation` | UI state management using shared ViewModels and reactive flows. |
| `domain` | Pure business logic: Use Cases, entities, and repository definitions. |
| `navigation` | Route definitions and navigation graphs using Navigation Compose. |
| `di` | Dependency injection configuration with Koin for all platforms. |
| `data` | Platform-specific implementations for persistence or native APIs. |
| `commonMain` | Shared UI (Compose), ViewModels, Use Cases, Navigation, and Dependency Injection. |
| `androidMain` | Android-specific implementations (e.g., native image repositories). |
| `iosMain` | iOS-specific implementations and entry points for the Apple app. |

---

## 🛠️ Tech Stack

### UI & UX
- **Compose Multiplatform** — Shared declarative interface.
- **Navigation Compose** — Native navigation in shared code.
- **Compottie** — Multiplatform Lottie animations.
- **ColorPicker Compose** — Color picker for QR customization.

### Core Logic
- **Qrose (Custom QR Generator)** — Advanced QR styling.
- **Koin 4.2.0** — Multiplatform dependency injection.
- **Coroutines + Flow** — Asynchronous management and reactive states.

---

## ✨ Features

- **Dynamic Generation**: Instant QRs from text or URL.
- **Customization**: Colors, corner rounding, and visual styles.
- **Logos**: Custom image insertion in the center of the QR.
- **Native Saving**: Support for saving to gallery on Android and iOS.

---

## 🎬 Splash Screen

The application features an animated splash screen using **Compottie**, which manages the initial transition to the main generation screen.

---

## 🧪 Testing

The project has a multi-layer testing strategy covering common, Android-specific, and iOS-specific code.

### Common Tests (`commonTest`)
Shared tests that run on all platforms using KMP-compatible libraries:
- **Kotest Assertions** — Fluent and readable assertions.
- **Turbine** — Flow testing with `test { }` and `awaitItem()`.
- **Mokkery** — Mocking library specifically designed for Kotlin Multiplatform.
- **Kotlinx Coroutines Test** — Utilities for testing coroutines with `StandardTestDispatcher`.

Covers: `MainViewModel`, Use Cases.

### Android Unit Tests (`androidUnitTest`)
Unit tests running on the JVM with Android environment simulation:
- **Robolectric** — Simulates the Android runtime without a device, enabling tests of Android-specific classes like `ContentResolver`, permissions, and Bitmap operations.
- **MockK** — Mocking library for JVM-based tests.
- **Roborazzi** — Screenshot testing library. Generates PNG reference images of Compose UI states and compares them on subsequent runs to catch visual regressions.

### Android Instrumentation Tests (`androidInstrumentedTest`)
Integration tests running on a real device or emulator:
- **Compose UI Test** — UI interaction and assertion framework for Compose screens.
- **MockK Android** — MockK variant for instrumented tests.
- **Koin test overrides** — ViewModel dependencies are replaced with fakes to isolate UI behavior.

### iOS Tests (`iosTest`)
Unit tests for iOS-specific implementations running on the iOS simulator:
- **Kotlin/Native test framework** — Standard `kotlin.test` for native targets.
- **Kotest Assertions** — Shared assertion library.

Covers: `IosPermissionRepository`, `IosImageRepository`.

### Screenshot Tests (Roborazzi)

Screenshot tests capture visual snapshots of `QrLayoutContent` in different UI states. On first run they generate the reference images; subsequent runs compare against them.

```bash
# Generate reference images
./gradlew :composeApp:recordRoborazziDebug

# Verify no visual regressions
./gradlew :composeApp:verifyRoborazziDebug
```

Reference images are stored in `composeApp/src/androidUnitTest/snapshots/images/` and should be committed to the repository.

### Running Tests

```bash
# Run all common tests (Android & iOS)
./gradlew :composeApp:allTests

# Run Android unit tests only (includes Robolectric + Roborazzi)
./gradlew :composeApp:testDebugUnitTest

# Run Android instrumentation tests (requires emulator or device)
./gradlew :composeApp:connectedDebugAndroidTest

# Run Android UI tests in androidApp module
./gradlew :androidApp:connectedDebugAndroidTest

# Run iOS tests on simulator
./gradlew :composeApp:iosSimulatorArm64Test

# Generate screenshot references
./gradlew :composeApp:recordRoborazziDebug

# Verify screenshot regressions
./gradlew :composeApp:verifyRoborazziDebug
```

---

## 📱 Compose Previews

Compose Previews are used for rapid UI component development, allowing visualization of design changes without needing to deploy to a real device.

---

## 🚀 Getting Started

1. **Clone the repository**.
2. **Sync Gradle**: Requires Android Studio Ladybug+ and JDK 17+.
3. **Run Android**: Run the `androidApp` module.
4. **Run iOS**: Open `iosApp/iosApp.xcodeproj` in Xcode or use the KMP plugin in Android Studio.

---

## 🔍 Implementation Details

- **Version Catalog**: Dependency management in `gradle/libs.versions.toml`.
- **Resources**: Shared resource management (images, strings) using the Compose Resources plugin.

---

## 👀 Examples

| iOS Light Mode | iOS Dark Mode | Android Light Mode | Android Dark Mode |
| :---: | :---: | :---: | :---: |
| <img src="https://github.com/Jsanzo97/QrGenerator-KMP/blob/main/screenshots/iOS-lightMode.png" width="200"> | <img src="https://github.com/Jsanzo97/QrGenerator-KMP/blob/main/screenshots/iOS-darkMode.png" width="200"> | <img src="https://github.com/Jsanzo97/QrGenerator-KMP/blob/main/screenshots/android-lightMode.png" width="200"> | <img src="https://github.com/Jsanzo97/QrGenerator-KMP/blob/main/screenshots/android-darkMode.png" width="200"> |

| Qr - Youtube | Qr - Spotify | Qr - Github | Qr - X (Classic) |
| :---: | :---: | :---: | :---: |
| <img src="https://github.com/Jsanzo97/QrGenerator-KMP/blob/main/screenshots/qr-youtube.png" width="180"> | <img src="https://github.com/Jsanzo97/QrGenerator-KMP/blob/main/screenshots/qr-spotify.png" width="180"> | <img src="https://github.com/Jsanzo97/QrGenerator-KMP/blob/main/screenshots/qr-github.png" width="180"> | <img src="https://github.com/Jsanzo97/QrGenerator-KMP/blob/main/screenshots/qr-twitter.png" width="180"> |