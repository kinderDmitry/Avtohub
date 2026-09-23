# Build verification

The project was statically checked in this environment:
- XML resources parse successfully.
- Java source files have balanced braces/parentheses/brackets.
- Gradle/Android SDK are not installed in the execution environment, so `:app:assembleDebug` could not be executed locally.

GitHub Actions is configured for JDK 17, Gradle 8.11.1, Android SDK 35 and Build Tools 35.0.0 and runs `gradle --no-daemon --stacktrace :app:assembleDebug`, verifies the APK, and uploads it as an artifact.
