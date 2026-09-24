# AUTO HUB 3.0.0 — production baseline

## Реализовано в архиве
- Local-first Android application, namespace `com.autohub.app`.
- Persistent vehicle profile and vehicle-scoped records.
- Dashboard, maintenance, finance, vehicle, reminders, issues, tires, documents, parts, service centers, statistics and activity journal.
- Real CRUD operations against SQLite.
- Fuel consumption and cost-per-km calculations from recorded data only.
- Mileage advancement and reminder scheduling workers.
- Boot/package-replacement reminder rescheduling.
- Notification permission/channel handling for Android 13+.
- JSON backup and full ZIP backup with attachments.
- JSON/ZIP import with validation and Merge/Replace modes.
- Dark/light visual modes and RU/EN interface switch.
- Search/filter flows for maintenance/activity records.
- Vehicle photo and document/file attachment persistence inside app storage.
- Database v4 migration with indexes for common vehicle/date/mileage queries.
- Pure calculation layer (`CarMath`) with JVM unit tests.
- GitHub Actions: SDK verification, resource preflight, unit tests, debug APK build and artifact upload.

## Important quality rule
No OBD, GPS diagnosis, market prices, service-center ratings or other external facts are fabricated. If the app does not have the required user-entered data, it shows an insufficient-data state.

## Build
```bash
./gradlew --no-daemon --stacktrace test
./gradlew --no-daemon --stacktrace :app:assembleDebug
```

## Verification status of this environment
The project files were statically reviewed and the Gradle workflow was updated, but a local Android build could not be executed in this environment because the Gradle distribution could not be downloaded due unavailable DNS/network access. Therefore this archive is not falsely marked as locally build-verified.
