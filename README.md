# AUTO HUB — Mirror Glass 1.0.1

Android-приложение для обслуживания и учёта автомобиля.

## Сборка GitHub Actions

Workflow: `.github/workflows/build.yml`

Поддерживается запуск вручную через **Actions → AUTO HUB Android Build → Run workflow**, а также автоматически при push в `main`/`master`.

Сборка использует:
- JDK 17
- Android SDK 35
- Android Build Tools 35.0.0
- Gradle 8.11.1
- Android Gradle Plugin 8.9.2

Результат: `app/build/outputs/apk/debug/app-debug.apk`

## Интерфейс

- Black Mirror Glass — тёмная тема
- Silver/White Mirror Glass — светлая тема
- Главная
- Обслуживание
- Расходы
- Автомобиль
- Ещё

Проект не требует Gradle Wrapper: GitHub Actions устанавливает Gradle 8.11.1 через `gradle/actions/setup-gradle`.
