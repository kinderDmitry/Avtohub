package com.autohub.app;
import android.content.*;import android.database.Cursor;import androidx.annotation.NonNull;import androidx.work.*;import java.util.concurrent.TimeUnit;
public class MileageReminderWorker extends Worker{
 public MileageReminderWorker(@NonNull Context c,@NonNull WorkerParameters p){super(c,p);}
 @NonNull public Result doWork(){AutoDb db=new AutoDb(getApplicationContext());Cursor c=db.raw("SELECT r.id,r.title FROM reminder r JOIN vehicle v ON v.id=r.vehicleId WHERE r.done=0 AND r.mileage>0 AND v.mileage>=r.mileage",null);try{while(c.moveToNext())NotificationHelper.show(getApplicationContext(),100000+(int)c.getLong(0),c.getString(1),"Пробег достиг заданного значения");}finally{c.close();}return Result.success();}
 public static void ensure(Context c){PeriodicWorkRequest r=new PeriodicWorkRequest.Builder(MileageReminderWorker.class,24,TimeUnit.HOURS).build();WorkManager.getInstance(c).enqueueUniquePeriodicWork("mileage_reminders",ExistingPeriodicWorkPolicy.KEEP,r);}
}
