# AUTO HUB Mirror Glass 1.0.2

Android-приложение для обслуживания автомобиля в стиле Mirror Glass.

## Сборка GitHub Actions

1. Загрузите содержимое этого проекта в репозиторий GitHub.
2. Откройте **Actions → AUTO HUB Android Build → Run workflow**.
3. После успешной сборки откройте **Artifacts → AUTOHUB-debug** и скачайте `app-debug.apk`.

Workflow намеренно не использует `android-actions/setup-android@v3`: этот action может пытаться установить устаревший пакет SDK `tools`, из-за чего сборка останавливается до запуска Gradle. Используется предустановленный Android SDK GitHub runner и прямой `sdkmanager`.

## Технологии
- Android Gradle Plugin 8.9.2
- Gradle 8.11.1
- JDK 17
- compileSdk / targetSdk 35
- minSdk 26
- Java UI без внешних runtime-зависимостей
