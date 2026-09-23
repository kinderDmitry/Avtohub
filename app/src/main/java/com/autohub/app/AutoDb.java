package com.autohub.app;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import java.util.*;

public class AutoDb extends SQLiteOpenHelper {
    static final String DB="autohub.db"; static final int V=1;
    public AutoDb(Context c){super(c,DB,null,V);}
    @Override public void onCreate(SQLiteDatabase d){
        d.execSQL("CREATE TABLE vehicle(id INTEGER PRIMARY KEY AUTOINCREMENT,brand TEXT NOT NULL,model TEXT NOT NULL,year INTEGER,engine TEXT,gear TEXT,mileage REAL NOT NULL,photo TEXT,vin TEXT,plate TEXT,fuelType TEXT,volume TEXT,power TEXT,drive TEXT,color TEXT,note TEXT,createdAt INTEGER)");
        d.execSQL("CREATE TABLE service(id INTEGER PRIMARY KEY AUTOINCREMENT,vehicleId INTEGER,date TEXT,mileage REAL,category TEXT,title TEXT,description TEXT,cost REAL,center TEXT,parts TEXT,nextDate TEXT,nextMileage REAL,photos TEXT,note TEXT)");
        d.execSQL("CREATE TABLE fuel(id INTEGER PRIMARY KEY AUTOINCREMENT,vehicleId INTEGER,date TEXT,mileage REAL,liters REAL,price REAL,total REAL,type TEXT,station TEXT,fullTank INTEGER,note TEXT)");
        d.execSQL("CREATE TABLE expense(id INTEGER PRIMARY KEY AUTOINCREMENT,vehicleId INTEGER,date TEXT,mileage REAL,category TEXT,title TEXT,amount REAL,note TEXT)");
        d.execSQL("CREATE TABLE reminder(id INTEGER PRIMARY KEY AUTOINCREMENT,vehicleId INTEGER,title TEXT,date TEXT,mileage REAL,repeatDays INTEGER,done INTEGER DEFAULT 0,createdAt INTEGER)");
        d.execSQL("CREATE TABLE issue(id INTEGER PRIMARY KEY AUTOINCREMENT,vehicleId INTEGER,title TEXT,description TEXT,date TEXT,mileage REAL,symptoms TEXT,photo TEXT,video TEXT,cost REAL,resolution TEXT,status TEXT)");
        d.execSQL("CREATE TABLE tire(id INTEGER PRIMARY KEY AUTOINCREMENT,vehicleId INTEGER,season TEXT,brand TEXT,model TEXT,size TEXT,purchaseDate TEXT,purchaseMileage REAL,installDate TEXT,removeDate TEXT,tread REAL,pressure TEXT,storage TEXT,cost REAL,installed INTEGER DEFAULT 0)");
        d.execSQL("CREATE TABLE document(id INTEGER PRIMARY KEY AUTOINCREMENT,vehicleId INTEGER,title TEXT,type TEXT,issueDate TEXT,expiryDate TEXT,uri TEXT,note TEXT)");
        d.execSQL("CREATE TABLE part(id INTEGER PRIMARY KEY AUTOINCREMENT,vehicleId INTEGER,name TEXT,brand TEXT,partNumber TEXT,quantity REAL,price REAL,purchaseDate TEXT,supplier TEXT,note TEXT,serviceId INTEGER)");
        d.execSQL("CREATE TABLE service_center(id INTEGER PRIMARY KEY AUTOINCREMENT,vehicleId INTEGER,name TEXT,address TEXT,phone TEXT,website TEXT,note TEXT)");
        d.execSQL("CREATE TABLE activity(id INTEGER PRIMARY KEY AUTOINCREMENT,vehicleId INTEGER,kind TEXT,title TEXT,detail TEXT,date TEXT,time TEXT,createdAt INTEGER)");
    }
    @Override public void onUpgrade(SQLiteDatabase d,int o,int n){ }
    long insert(String table, ContentValues v){return getWritableDatabase().insertOrThrow(table,null,v);}
    int update(String table,ContentValues v,String where,String[] args){return getWritableDatabase().update(table,v,where,args);}
    int delete(String table,String where,String[] args){return getWritableDatabase().delete(table,where,args);}
    Cursor all(String table,long vehicle){return getReadableDatabase().query(table,null,"vehicleId=?",new String[]{String.valueOf(vehicle)},null,null,"id DESC");}
    Cursor raw(String sql,String[] args){return getReadableDatabase().rawQuery(sql,args);}
    long addVehicle(ContentValues v){return insert("vehicle",v);}
    void log(long vehicle,String kind,String title,String detail){ContentValues v=new ContentValues();v.put("vehicleId",vehicle);v.put("kind",kind);v.put("title",title);v.put("detail",detail);String date=new java.text.SimpleDateFormat("yyyy-MM-dd",Locale.US).format(new Date());String time=new java.text.SimpleDateFormat("HH:mm",Locale.US).format(new Date());v.put("date",date);v.put("time",time);v.put("createdAt",System.currentTimeMillis());insert("activity",v);}
}
