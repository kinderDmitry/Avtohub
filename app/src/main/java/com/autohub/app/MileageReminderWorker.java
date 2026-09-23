package com.autohub.app;

import android.content.ContentValues;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

public final class MileageReminderWorker extends Worker {
    public MileageReminderWorker(@NonNull android.content.Context c, @NonNull WorkerParameters p) { super(c, p); }

    @NonNull @Override public Result doWork() {
        AutoDb db = new AutoDb(getApplicationContext());
        Cursor c = db.raw("SELECT r.id,r.title,r.mileage,r.lastMileageNotified FROM reminder r JOIN vehicle v ON v.id=r.vehicleId WHERE r.done=0 AND r.mileage>0 AND v.mileage>=r.mileage", null);
        try {
            while (c.moveToNext()) {
                long id = c.getLong(0); String title = c.getString(1); double target = c.getDouble(2); double last = c.getDouble(3);
                if (last >= target) continue;
                NotificationHelper.show(getApplicationContext(), 100000 + (int) id, title, "Пробег достиг заданного значения");
                ContentValues v = new ContentValues(); v.put("lastMileageNotified", target);
                db.update("reminder", v, "id=?", new String[]{String.valueOf(id)});
            }
        } finally { c.close(); }
        return Result.success();
    }

    public static void ensure(android.content.Context c) {
        PeriodicWorkRequest r = new PeriodicWorkRequest.Builder(MileageReminderWorker.class, 24, TimeUnit.HOURS).build();
        WorkManager.getInstance(c).enqueueUniquePeriodicWork("mileage_reminders", ExistingPeriodicWorkPolicy.KEEP, r);
    }
}
