# 📱 Free QR Generator - KMP

![Kotlin](https://img.shields.io/badge/Kotlin-2.3.20-grey?style=flat&logo=kotlin&logoColor=white&labelColor=7F52FF)
![KMP](https://img.shields.io/badge/KMP-Supported-grey?style=flat&logo=kotlin&logoColor=white&labelColor=7F52FF)
![Android](https://img.shields.io/badge/Android-SDK%2036-grey?style=flat&logo=android&logoColor=white&labelColor=green)
![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.10.2-grey?style=flat&logo=jetpackcompose&logoColor=white&labelColor=blue)
![Min SDK](https://img.shields.io/badge/Min%20SDK-23-grey?style=flat&labelColor=green)
![License](https://img.shields.io/badge/License-MIT-grey?style=flat&labelColor=yellow)

Aplicación multiplataforma desarrollada con **Kotlin Multiplatform (KMP)** para la generación y personalización de códigos QR. El proyecto utiliza **Compose Multiplatform** para compartir la lógica y la interfaz de usuario entre Android e iOS.

---

## 🏛️ Arquitectura

El proyecto utiliza una arquitectura modular donde la lógica de negocio y la UI se centralizan en el módulo compartido, permitiendo una máxima reutilización de código.

```
androidApp  → :composeApp (módulo compartido)
iosApp      → :composeApp (módulo compartido vía framework)
:composeApp → commonMain (UI, Presentation, Domain, Navigation, DI)
```

| Módulo | Responsabilidad |
|---|---|
| `:ui` | Componentes visuales comunes, temas y pantallas desarrolladas con Compose Multiplatform. |
| `:presentation` | Gestión del estado de la UI mediante ViewModels compartidos y flujos reactivos. |
| `:domain` | Lógica de negocio pura: Casos de uso (UseCases), entidades y definiciones de repositorios. |
| `:navigation` | Definición de rutas y grafos de navegación utilizando Navigation Compose. |
| `:di` | Configuración de la inyección de dependencias con Koin para todas las plataformas. |
| `:data` | (En androidMain/iosMain) Implementaciones específicas de plataforma para persistencia o APIs nativas. |
| `commonMain` | UI compartida (Compose), ViewModels, Casos de Uso, Navegación e Inyección de Dependencias. |
| `androidMain` | Implementaciones específicas de Android (ej. Repositorios de imágenes nativos). |
| `iosMain` | Implementaciones específicas de iOS y puntos de entrada para la app de Apple. |

---

## 🛠️ Tech Stack

### UI & UX
- **Compose Multiplatform** — Interfaz declarativa compartida.
- **Navigation Compose** — Navegación nativa en código compartido.
- **Compottie** — Animaciones Lottie multiplataforma.
- **ColorPicker Compose** — Selector de color para personalización del QR.

### Core Logic
- **Qrose (Custom QR Generator)** — Estilización avanzada de QRs.
- **Koin 4.2.0** — Inyección de dependencias multiplataforma.
- **Coroutines + Flow** — Gestión de asincronía y estados reactivos.

---

## ✨ Características

- **Generación Dinámica**: QRs instantáneos desde texto o URL.
- **Personalización**: Colores, redondeado de esquinas y estilos visuales.
- **Logos**: Inserción de imágenes personalizadas en el centro del QR.
- **Guardado Nativo**: Soporte para guardar en galería en Android e iOS.

---

## 🎬 Splash Screen

La aplicación cuenta con una pantalla de inicio animada utilizando **Compottie**, que gestiona la transición inicial hacia la pantalla principal de generación.

---

## 🧪 Testing

El proyecto incluye pruebas unitarias en el módulo común:
- **Kotlin Test** para validaciones de lógica en `commonTest`.
- Mocks para la verificación de interacción entre componentes.

```bash
./gradlew :shared:commonTest  # Ejecutar tests comunes
```

---

## 📱 Compose Previews

Se utilizan Previews de Compose para el desarrollo rápido de componentes de UI, permitiendo visualizar cambios de diseño sin necesidad de desplegar en un dispositivo real.

---

## 🚀 Cómo empezar

1. **Clonar el repositorio**.
2. **Sincronizar Gradle**: Requiere Android Studio Ladybug+ y JDK 17+.
3. **Ejecutar Android**: Ejecuta el módulo `androidApp`.
4. **Ejecutar iOS**: Abre `iosApp/iosApp.xcodeproj` en Xcode o usa el plugin KMP en Android Studio.

---

## 🔍 Detalles de Implementación

- **Version Catalog**: Gestión de dependencias en `gradle/libs.versions.toml`.
- **Resources**: Gestión compartida de recursos (imágenes, strings) mediante el plugin de Compose Resources.

## 👀 Ejemplos

![iOS - Light Mode](https://github.com/Jsanzo97/FreeQrGenerator/blob/main/screenshots/iOS-lightMode.png)
![iOS - Dark Mode](https://github.com/Jsanzo97/FreeQrGenerator/blob/main/screenshots/iOS-darkMode.png)
![Android - Light Mode](https://github.com/Jsanzo97/FreeQrGenerator/blob/main/screenshots/android-lightMode.png)
![Android - Dark Mode](https://github.com/Jsanzo97/FreeQrGenerator/blob/main/screenshots/android-darkMode.png)

![Qr - Youtube](https://github.com/Jsanzo97/FreeQrGenerator/blob/main/screenshots/qr-youtube.png)
![Qr - Spotify](https://github.com/Jsanzo97/FreeQrGenerator/blob/main/screenshots/qr-spotify.png)
![Qr - Github](https://github.com/Jsanzo97/FreeQrGenerator/blob/main/screenshots/qr-github.png)
![Qr - X (Classic)](https://github.com/Jsanzo97/FreeQrGenerator/blob/main/screenshots/qr-twitter.png)