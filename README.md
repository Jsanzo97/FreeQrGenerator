# 📱 Free QR Generator - KMP

![Kotlin](https://img.shields.io/badge/Kotlin-2.3.20-grey?style=flat&logo=kotlin&logoColor=white&labelColor=7F52FF)
![KMP](https://img.shields.io/badge/KMP-Supported-grey?style=flat&logo=kotlin&logoColor=white&labelColor=7F52FF)
![Android](https://img.shields.io/badge/Android-SDK%2036-grey?style=flat&logo=android&logoColor=white&labelColor=green)
![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.10.2-grey?style=flat&logo=jetpackcompose&logoColor=white&labelColor=blue)
![Min SDK](https://img.shields.io/badge/Min%20SDK-23-grey?style=flat&labelColor=green)
![License](https://img.shields.io/badge/License-MIT-grey?style=flat&labelColor=yellow)

Multiplatform application developed with **Kotlin Multiplatform (KMP)** for generating and customizing QR codes. The project uses **Compose Multiplatform** to share logic and user interface between Android and iOS.

---

## 🏛️ Architecture

The project uses a modular architecture where business logic and UI are centralized in the shared module, allowing for maximum code reuse.

```
androidApp  → :composeApp (shared module)
iosApp      → :composeApp (shared module via framework)
:composeApp → commonMain (UI, Presentation, Domain, Navigation, DI)
```

| Module | Responsibility |
|---|---|
| `:ui` | Common visual components, themes, and screens developed with Compose Multiplatform. |
| `:presentation` | UI state management using shared ViewModels and reactive flows. |
| `:domain` | Pure business logic: Use Cases, entities, and repository definitions. |
| `:navigation` | Route definitions and navigation graphs using Navigation Compose. |
| `:di` | Dependency injection configuration with Koin for all platforms. |
| `:data` | (In androidMain/iosMain) Platform-specific implementations for persistence or native APIs. |
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

The project includes unit tests in the common module:
- **Kotlin Test** for logic validation in `commonTest`.
- Mocks for verifying interaction between components.

```bash
./gradlew :shared:commonTest  # Run common tests
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

## 👀 Examples

| iOS Light Mode | iOS Dark Mode | Android Light Mode | Android Dark Mode |
| :---: | :---: | :---: | :---: |
| <img src="https://github.com/Jsanzo97/FreeQrGenerator/blob/main/screenshots/iOS-lightMode.png" width="200"> | <img src="https://github.com/Jsanzo97/FreeQrGenerator/blob/main/screenshots/iOS-darkMode.png" width="200"> | <img src="https://github.com/Jsanzo97/FreeQrGenerator/blob/main/screenshots/android-lightMode.png" width="200"> | <img src="https://github.com/Jsanzo97/FreeQrGenerator/blob/main/screenshots/android-darkMode.png" width="200"> |

| Qr - Youtube | Qr - Spotify | Qr - Github | Qr - X (Classic) |
| :---: | :---: | :---: | :---: |
| <img src="https://github.com/Jsanzo97/FreeQrGenerator/blob/main/screenshots/qr-youtube.png" width="180"> | <img src="https://github.com/Jsanzo97/FreeQrGenerator/blob/main/screenshots/qr-spotify.png" width="180"> | <img src="https://github.com/Jsanzo97/FreeQrGenerator/blob/main/screenshots/qr-github.png" width="180"> | <img src="https://github.com/Jsanzo97/FreeQrGenerator/blob/main/screenshots/qr-twitter.png" width="180"> |
