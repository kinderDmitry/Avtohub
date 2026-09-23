package com.autohub.app;
import android.app.*;import android.content.*;import android.database.Cursor;import androidx.annotation.NonNull;import androidx.work.*;import java.util.concurrent.TimeUnit;
public class ReminderWorker extends Worker{
 public ReminderWorker(@NonNull Context c,@NonNull WorkerParameters p){super(c,p);}
 @NonNull public Result doWork(){long id=getInputData().getLong("id",-1);String title=getInputData().getString("title");if(id<0||title==null)return Result.failure();AutoDb db=new AutoDb(getApplicationContext());android.database.Cursor c=db.raw("SELECT done FROM reminder WHERE id=?",new String[]{String.valueOf(id)});try{if(!c.moveToFirst()||c.getInt(0)!=0)return Result.success();}finally{c.close();}NotificationHelper.show(getApplicationContext(),(int)id,title,"AUTO HUB: запланированное напоминание");return Result.success();}
 public static void schedule(Context c,long id,String title,long delayMs,int repeatDays){OneTimeWorkRequest.Builder b=new OneTimeWorkRequest.Builder(ReminderWorker.class);b.setInputData(new Data.Builder().putLong("id",id).putString("title",title).build()).setInitialDelay(Math.max(0,delayMs),TimeUnit.MILLISECONDS);WorkManager.getInstance(c).enqueueUniqueWork("reminder_"+id,ExistingWorkPolicy.REPLACE,b.build());}
 public static void cancel(Context c,long id){WorkManager.getInstance(c).cancelUniqueWork("reminder_"+id);}
}
