package com.autohub.app;

import android.content.ContentValues;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public final class DocumentReminderWorker extends Worker {
    public DocumentReminderWorker(@NonNull android.content.Context c, @NonNull WorkerParameters p) { super(c, p); }
    @NonNull @Override public Result doWork() {
        AutoDb db = new AutoDb(getApplicationContext());
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
        long now = System.currentTimeMillis();
        long horizon = now + TimeUnit.DAYS.toMillis(30);
        String horizonDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date(horizon));
        Cursor c = db.raw("SELECT id,title,expiryDate,lastNotifiedAt FROM document WHERE expiryDate<>'' AND expiryDate<=?", new String[]{horizonDate});
        try {
            while (c.moveToNext()) {
                long id=c.getLong(0); String title=c.getString(1); String exp=c.getString(2); long last=c.getLong(3);
                boolean expired=exp.compareTo(today)<0;
                if(last>0 && now-last<TimeUnit.HOURS.toMillis(24)) continue;
                NotificationHelper.show(getApplicationContext(),200000+(int)id,title,expired?"Документ истёк":"Срок действия документа скоро заканчивается");
                ContentValues v=new ContentValues();v.put("lastNotifiedAt",now);db.update("document",v,"id=?",new String[]{String.valueOf(id)});
            }
        } finally { c.close(); }
        return Result.success();
    }
    public static void ensure(android.content.Context c){
        PeriodicWorkRequest r=new PeriodicWorkRequest.Builder(DocumentReminderWorker.class,24,TimeUnit.HOURS).build();
        WorkManager.getInstance(c).enqueueUniquePeriodicWork("document_reminders",ExistingPeriodicWorkPolicy.KEEP,r);
    }
}
