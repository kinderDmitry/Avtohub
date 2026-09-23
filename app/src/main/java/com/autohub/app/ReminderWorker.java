package com.autohub.app;

import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

public final class ReminderWorker extends Worker {
    public ReminderWorker(@NonNull Context c, @NonNull WorkerParameters p) { super(c, p); }

    @NonNull @Override public Result doWork() {
        long id = getInputData().getLong("id", -1);
        if (id < 0) return Result.failure();
        AutoDb db = new AutoDb(getApplicationContext());
        Cursor c = db.raw("SELECT title,done,repeatDays FROM reminder WHERE id=?", new String[]{String.valueOf(id)});
        String title = null; int repeat = 0; boolean done = true;
        try {
            if (c.moveToFirst()) { title = c.getString(0); done = c.getInt(1) != 0; repeat = c.getInt(2); }
        } finally { c.close(); }
        if (title == null || done) return Result.success();

        NotificationHelper.show(getApplicationContext(), (int) id, title, "AUTO HUB: запланированное напоминание");
        db.update("reminder", cv("lastNotifiedAt", System.currentTimeMillis()), "id=?", new String[]{String.valueOf(id)});

        if (repeat > 0) {
            schedule(getApplicationContext(), id, title, TimeUnit.DAYS.toMillis(repeat), repeat);
        }
        return Result.success();
    }

    private static android.content.ContentValues cv(String key, long value) {
        android.content.ContentValues v = new android.content.ContentValues(); v.put(key, value); return v;
    }

    public static void schedule(Context c, long id, String title, long delayMs, int repeatDays) {
        OneTimeWorkRequest req = new OneTimeWorkRequest.Builder(ReminderWorker.class)
                .setInputData(new Data.Builder().putLong("id", id).putString("title", title).build())
                .setInitialDelay(Math.max(0, delayMs), TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(c).enqueueUniqueWork("reminder_" + id, ExistingWorkPolicy.REPLACE, req);
    }

    public static void cancel(Context c, long id) {
        WorkManager.getInstance(c).cancelUniqueWork("reminder_" + id);
    }
}
