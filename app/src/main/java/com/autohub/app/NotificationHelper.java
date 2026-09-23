package com.autohub.app;
import android.app.*;import android.content.*;import android.os.*;
public final class NotificationHelper{
 static final String CH="autohub_reminders";
 static void ensure(Context c){if(Build.VERSION.SDK_INT>=26){NotificationChannel ch=new NotificationChannel(CH,"AUTO HUB — Напоминания",NotificationManager.IMPORTANCE_DEFAULT);ch.setDescription("Напоминания об автомобиле");((NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(ch);}}
 public static void show(Context c,int id,String title,String text){ensure(c);Intent i=new Intent(c,MainActivity.class);PendingIntent pi=PendingIntent.getActivity(c,id,i,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);Notification.Builder b=Build.VERSION.SDK_INT>=26?new Notification.Builder(c,CH):new Notification.Builder(c);b.setSmallIcon(com.autohub.app.R.drawable.ic_launcher).setContentTitle(title).setContentText(text).setAutoCancel(true).setContentIntent(pi);if(Build.VERSION.SDK_INT<33||c.checkSelfPermission("android.permission.POST_NOTIFICATIONS")==0)((NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE)).notify(id,b.build());}
}
