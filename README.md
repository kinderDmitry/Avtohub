# AUTO HUB 1.3.0

Premium automotive management app for Android. Local-first, no fabricated vehicle/OBD/GPS/market data.

## Implemented
- Vehicle profile: brand, model, year, engine, gearbox, mileage.
- Maintenance/service log with date, mileage, cost, next mileage and service center/comment.
- Fuel log with liters, price/liter and automatic total calculation.
- Expense ledger with categories, amount, mileage and comments.
- Reminders with date, mileage and repeat interval stored with the vehicle data.
- Problem log with status.
- Tire set records.
- Document records with expiry date.
- Dashboard health summary based only on stored records.
- Finance aggregation and cost/km only when mileage points are sufficient.
- JSON backup export/import through Android document picker.
- Dark/light Mirror Glass UI.
- Persistent local storage via SharedPreferences/JSON.
- Multi-record data model prepared with vehicle isolation field.
- GitHub Actions debug APK build.

## Build
GitHub Actions uses Java 17, Android SDK 35, Build Tools 35.0.0 and Gradle 8.11.1.

## Integrity rule
The app does not generate fake diagnostics, GPS, OBD readings, service prices, fuel prices or external ratings. If external integration is added later, it must be connected to a real provider/API and verified.
