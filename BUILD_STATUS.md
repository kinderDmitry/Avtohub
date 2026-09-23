# Build verification status — 2.1.4

The supplied GitHub Actions log did not compile the application. It failed earlier because `android-actions/setup-android@v3` attempted to run the obsolete `sdkmanager tools` package. The workflow has been replaced so it does not invoke that package and instead verifies/installs only `platform-tools`, `platforms;android-35`, and `build-tools;35.0.0`.

A local Android SDK is not installed in this execution environment, so no claim of a locally built APK is made. The new workflow is the authoritative build check.


## Latest CI diagnosis
The previous CI run reached `:app:processDebugResources` and failed because `car_placeholder.xml` used unsupported `android:cx`, `android:cy`, and `android:r` attributes. The vector was corrected to use pathData for the wheel circles.


## 2.1.4 CI preflight
- Исправлен CI-артефакт на `AUTO_HUB-debug-2.1.4`.
- Перед Gradle добавлена XML/VectorDrawable preflight-проверка: парсинг XML и запрет неподдерживаемых `android:cx/cy/r/rx/ry/x1/x2/y1/y2` внутри vector resources.
- `car_placeholder.xml` уже использует `pathData`, без `android:cx/cy/r`.


## 2.1.4 CI fix
Fixed Java compilation errors from CI 2026-09-23: lambda captures in issue/document dialogs and checked IOException around attachment copy in onActivityResult.
