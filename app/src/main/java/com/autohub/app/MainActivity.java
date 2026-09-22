package com.autohub.app;

import android.app.*;
import android.os.Bundle;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.view.*;
import android.widget.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends Activity {
    AutoHubView view;
    @Override public void onCreate(Bundle b){
        super.onCreate(b);
        view=new AutoHubView(this);
        applyBars();
        setContentView(view);
    }
    void applyBars(){
        boolean light=getSharedPreferences("auto",0).getBoolean("light",false);
        int c=light?Color.rgb(238,242,246):Color.rgb(5,7,11);
        getWindow().setStatusBarColor(c); getWindow().setNavigationBarColor(c);
        getWindow().getDecorView().setSystemUiVisibility(light ? View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR|View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR : 0);
    }

    void editVehicle(){
        LinearLayout box=form();
        EditText name=input("Марка и модель", view.pref("vehicle","Мой автомобиль"));
        EditText engine=input("Двигатель", view.pref("engine",""));
        EditText year=input("Год", view.pref("year",""));
        EditText mileage=input("Текущий пробег, км", view.pref("mileage","0"));
        box.addView(name);box.addView(engine);box.addView(year);box.addView(mileage);
        new AlertDialog.Builder(this).setTitle("Автомобиль").setView(box).setNegativeButton("Отмена",null).setPositiveButton("Сохранить",(d,w)->{
            view.save("vehicle",name.getText().toString());view.save("engine",engine.getText().toString());view.save("year",year.getText().toString());view.save("mileage",mileage.getText().toString());view.invalidate();
        }).show();
    }
    void addMaintenance(){ addRecord("ТО", "Название работы", "Стоимость, ₽", "Пробег, км", "Комментарий"); }
    void addFuel(){ addRecord("Заправка", "Литры", "Цена за литр, ₽", "Пробег, км", "АЗС / комментарий"); }
    void addExpense(){ addRecord("Расход", "Категория", "Сумма, ₽", "Пробег, км", "Комментарий"); }
    void addRecord(String type,String a,String b,String c,String d){
        LinearLayout box=form(); EditText e1=input(a,"");EditText e2=input(b,"");EditText e3=input(c,view.pref("mileage","0"));EditText e4=input(d,"");
        box.addView(e1);box.addView(e2);box.addView(e3);box.addView(e4);
        new AlertDialog.Builder(this).setTitle("Добавить · "+type).setView(box).setNegativeButton("Отмена",null).setPositiveButton("Сохранить",(di,w)->{
            String row=type+"|"+clean(e1.getText().toString())+"|"+clean(e2.getText().toString())+"|"+clean(e3.getText().toString())+"|"+clean(e4.getText().toString())+"|"+date();
            view.append(row);view.invalidate(); Toast.makeText(this,"Запись сохранена",Toast.LENGTH_SHORT).show();
        }).show();
    }
    LinearLayout form(){ LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.VERTICAL);l.setPadding(20,4,20,0);return l; }
    EditText input(String hint,String value){ EditText e=new EditText(this);e.setHint(hint);e.setText(value);e.setSingleLine(true);e.setTextColor(view.fg());e.setHintTextColor(view.sub());e.setPadding(0,10,0,8);return e; }
    String clean(String s){return s.replace("|","/").replace("\n"," ");}
    String date(){return new SimpleDateFormat("dd.MM.yyyy",Locale.getDefault()).format(new Date());}

    class AutoHubView extends View {
        Paint p=new Paint(Paint.ANTI_ALIAS_FLAG); Path path=new Path(); RectF r=new RectF();
        int page=0; boolean light; float downX,downY;
        final int blue=Color.rgb(46,150,255), blue2=Color.rgb(102,195,255), green=Color.rgb(37,205,143), orange=Color.rgb(255,171,58), red=Color.rgb(255,78,94);
        final String[] nav={"Главная","ТО","Расходы","Авто","Ещё"};
        AutoHubView(Context c){super(c);light=getSharedPreferences("auto",0).getBoolean("light",false);setLayerType(View.LAYER_TYPE_SOFTWARE,null);}
        float d(){return getResources().getDisplayMetrics().density;}
        int bg(){return light?Color.rgb(238,242,246):Color.rgb(5,7,11);} int fg(){return light?Color.rgb(23,29,36):Color.rgb(245,248,252);} int sub(){return light?Color.rgb(91,103,116):Color.rgb(151,166,183);} int glass(){return light?Color.argb(208,255,255,255):Color.argb(185,17,24,34);}
        String pref(String k,String def){return getSharedPreferences("auto",0).getString(k,def);} void save(String k,String v){getSharedPreferences("auto",0).edit().putString(k,v).apply();}
        String records(){return pref("records","");} void append(String row){String old=records();save("records",old.isEmpty()?row:old+"\n"+row);}
        int count(String type){String s=records();if(s.isEmpty())return 0;int n=0;for(String row:s.split("\\n")){if(row.startsWith(type+"|"))n++;}return n;}
        void text(Canvas c,String s,float x,float y,float size,int color,boolean bold){p.setShader(null);p.setStyle(Paint.Style.FILL);p.setColor(color);p.setTextSize(size*d());p.setTypeface(Typeface.create("sans",bold?Typeface.BOLD:Typeface.NORMAL));c.drawText(s,x,y,p);}
        void glass(Canvas c,float l,float t,float rr,float bb,float radius){r.set(l,t,rr,bb);p.setStyle(Paint.Style.FILL);p.setColor(glass());p.setShadowLayer(20*d(),0,8*d(),light?Color.argb(35,30,50,70):Color.argb(95,0,95,220));c.drawRoundRect(r,radius*d(),radius*d(),p);p.clearShadowLayer();p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(1*d());p.setColor(light?Color.argb(160,255,255,255):Color.argb(125,75,145,205));c.drawRoundRect(r,radius*d(),radius*d(),p);p.setShader(new LinearGradient(l,t,rr,bb,light?Color.argb(120,255,255,255):Color.argb(75,115,190,255),Color.TRANSPARENT,Shader.TileMode.CLAMP));p.setStyle(Paint.Style.FILL);c.drawRoundRect(r,radius*d(),radius*d(),p);p.setShader(null);}
        void dot(Canvas c,float x,float y,int color){p.setColor(color);p.setStyle(Paint.Style.FILL);p.setShadowLayer(11*d(),0,0,Color.argb(110,Color.red(color),Color.green(color),Color.blue(color)));c.drawCircle(x,y,4*d(),p);p.clearShadowLayer();}
        @Override protected void onDraw(Canvas c){super.onDraw(c);float D=d();c.drawColor(bg());p.setShader(new RadialGradient(getWidth()*.64f,70*D,330*D,light?Color.argb(68,65,145,230):Color.argb(88,0,110,255),Color.TRANSPARENT,Shader.TileMode.CLAMP));c.drawRect(0,0,getWidth(),getHeight()*.62f,p);p.setShader(null);header(c);if(page==0)home(c);else if(page==1)service(c);else if(page==2)expenses(c);else if(page==3)car(c);else more(c);bottom(c);}
        void header(Canvas c){float D=d();text(c,"AUTO HUB",20*D,32*D,12,sub(),true);text(c,pageTitle(),20*D,67*D,27,fg(),true);dot(c,getWidth()-44*D,28*D,green);text(c,"⋮",getWidth()-26*D,34*D,20,sub(),true);}
        String pageTitle(){return page==0?"Мой автомобиль":nav[page];}
        void home(Canvas c){float D=d();String vehicle=pref("vehicle","Мой автомобиль");String engine=pref("engine","");String mileage=pref("mileage","0");
            glass(c,16*D,88*D,getWidth()-16*D,302*D,25);text(c,vehicle,32*D,121*D,23,fg(),true);text(c,engine.isEmpty()?"Добавьте двигатель":engine,32*D,145*D,11,sub(),false);
            p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(3*D);p.setColor(blue2);path.reset();path.moveTo(46*D,222*D);path.cubicTo(76*D,197*D,92*D,185*D,120*D,182*D);path.lineTo(193*D,182*D);path.cubicTo(218*D,188*D,235*D,205*D,255*D,222*D);path.lineTo(46*D,222*D);c.drawPath(path,p);p.setStyle(Paint.Style.FILL);c.drawCircle(84*D,222*D,13*D,p);c.drawCircle(216*D,222*D,13*D,p);p.setStyle(Paint.Style.STROKE);p.setColor(light?Color.rgb(150,165,180):Color.rgb(50,70,92));c.drawLine(122*D,183*D,135*D,222*D,p);c.drawLine(191*D,183*D,181*D,222*D,p);p.setStyle(Paint.Style.FILL);
            text(c,mileage,32*D,267*D,27,fg(),true);text(c,"км  ·  текущий пробег",32*D,286*D,10,sub(),false);text(c,"ТО",getWidth()-128*D,249*D,9,sub(),true);text(c,count("ТО")==0?"Нет записей":count("ТО")+" записей",getWidth()-128*D,273*D,12,count("ТО")==0?sub():green,true);text(c,"Добавьте ТО",getWidth()-128*D,291*D,9,sub(),false);
            glass(c,16*D,318*D,getWidth()/2-6*D,407*D,18);glass(c,getWidth()/2+6*D,318*D,getWidth()-16*D,407*D,18);metric(c,31,343,"ЗАПРАВКИ",count("Заправка")+" записей",blue);metric(c,(int)(getWidth()/D/2+21),343,"РАСХОДЫ",count("Расход")+" записей",orange);
            text(c,"Быстрые действия",20*D,444*D,18,fg(),true);action(c,16,458,"+ ТО",blue,1);action(c,126,458,"+ Топливо",green,2);action(c,250,458,"+ Расход",orange,3);
            glass(c,16*D,531*D,getWidth()-16*D,595*D,18);text(c,"Автомобиль",32*D,556*D,11,sub(),true);text(c,"Нажмите, чтобы изменить профиль",32*D,578*D,11,fg(),false);text(c,"›",getWidth()-38*D,572*D,22,sub(),false);
        }
        void metric(Canvas c,int x,int y,String a,String b,int col){float D=d();text(c,a,x*D,y*D,9,sub(),true);text(c,b,x*D,(y+30)*D,14,fg(),true);dot(c,x*D+8*D,(y+52)*D,col);text(c,"локально",x*D+19*D,(y+55)*D,9,sub(),false);}
        void action(Canvas c,int x,int y,String label,int col,int id){float D=d();glass(c,x*D,y*D,(x+102)*D,(y+57)*D,16);dot(c,(x+19)*D,(y+29)*D,col);text(c,label,(x+32)*D,(y+34)*D,11,fg(),true);}
        void service(Canvas c){float D=d();text(c,"Сервисная книга",20*D,101*D,13,sub(),true);String[] a={"Моторное масло","Фильтры","Тормозная система","Жидкости","Свечи","Подвеска"};for(int i=0;i<a.length;i++){float y=112+i*61;glass(c,16*D,y*D,getWidth()-16*D,(y+51)*D,16);text(c,a[i],32*D,(y+23)*D,13,fg(),true);int n=i==0?count("ТО"):0;text(c,n==0?"Нет записи":n+" записей",32*D,(y+41)*D,10,sub(),false);dot(c,getWidth()-45*D,(y+25)*D,n>0?green:sub());}glass(c,16*D,492*D,getWidth()-16*D,552*D,18);text(c,"+ Добавить обслуживание",34*D,529*D,13,blue,true);text(c,"История: "+count("ТО")+" записей",34*D,574*D,11,sub(),false);}
        void expenses(Canvas c){float D=d();int n=count("Расход")+count("Заправка");glass(c,16*D,91*D,getWidth()-16*D,212*D,22);text(c,"ЛОКАЛЬНАЯ ИСТОРИЯ",32*D,120*D,10,sub(),true);text(c,n+" операций",32*D,158*D,27,fg(),true);text(c,"Данные вводятся вами и хранятся на устройстве",32*D,182*D,10,sub(),false);text(c,"Последние записи",20*D,250*D,18,fg(),true);String[] rows=records().isEmpty()?new String[0]:records().split("\\n");int shown=0;for(int i=rows.length-1;i>=0&&shown<5;i--){String[] q=rows[i].split("\\|",-1);if(q.length<6)continue;float y=267+shown*62;glass(c,16*D,y*D,getWidth()-16*D,(y+51)*D,15);text(c,q[0]+" · "+q[1],32*D,(y+21)*D,12,fg(),true);text(c,q[5]+"  ·  "+q[3]+" км",32*D,(y+40)*D,9,sub(),false);text(c,q[2],getWidth()-92*D,(y+30)*D,11,fg(),true);shown++;}}
        void car(Canvas c){float D=d();glass(c,16*D,92*D,getWidth()-16*D,222*D,22);text(c,pref("vehicle","Мой автомобиль"),32*D,126*D,20,fg(),true);text(c,pref("engine", "Двигатель не указан"),32*D,150*D,11,sub(),false);text(c,pref("year","Год не указан"),32*D,171*D,11,sub(),false);text(c,pref("mileage","0")+" км",32*D,203*D,20,fg(),true);String[] rows={"Профиль автомобиля","История обслуживания","Напоминания","Документы","Шины","Статистика"};for(int i=0;i<rows.length;i++){float y=238+i*56;glass(c,16*D,y*D,getWidth()-16*D,(y+46)*D,15);text(c,rows[i],34*D,(y+29)*D,13,fg(),true);text(c,"›",getWidth()-38*D,(y+30)*D,21,sub(),false);}}
        void more(Canvas c){float D=d();text(c,"Настройки",20*D,101*D,13,sub(),true);String[] rows={"Автомобиль","Тема интерфейса","Уведомления","Единицы измерения","Резервная копия","Экспорт данных","О приложении"};for(int i=0;i<rows.length;i++){float y=112+i*53;glass(c,16*D,y*D,getWidth()-16*D,(y+44)*D,15);text(c,rows[i],34*D,(y+28)*D,13,fg(),true);if(i==1)text(c,light?"Светлая":"Тёмная",getWidth()-91*D,(y+28)*D,10,blue,true);else text(c,"›",getWidth()-38*D,(y+29)*D,21,sub(),false);}glass(c,16*D,503*D,getWidth()-16*D,563*D,18);text(c,light?"Переключить на тёмную тему":"Переключить на светлую тему",34*D,539*D,13,fg(),true);text(c,"Mirror Glass 1.1.0",34*D,589*D,10,sub(),false);}
        void bottom(Canvas c){float D=d();float y=getHeight()/D-72;glass(c,10*D,y*D,getWidth()-10*D,(y+58)*D,22);for(int i=0;i<5;i++){float x=getWidth()*(i+.5f)/5;int col=i==page?blue:sub();if(i==page){p.setColor(light?Color.argb(35,40,145,255):Color.argb(48,40,145,255));p.setStyle(Paint.Style.FILL);c.drawCircle(x,(y+28)*D,21*D,p);}text(c,nav[i],x-(nav[i].length()*3f)*D,(y+35)*D,9,col,i==page);}}
        @Override public boolean onTouchEvent(MotionEvent e){float D=d();if(e.getAction()==MotionEvent.ACTION_DOWN){downX=e.getX();downY=e.getY();return true;}if(e.getAction()!=MotionEvent.ACTION_UP)return true;float x=e.getX(),y=e.getY();if(Math.abs(x-downX)>90*D){if(x<downX)page=Math.min(4,page+1);else page=Math.max(0,page-1);invalidate();return true;}float navY=getHeight()-72*D;if(y>navY){int n=(int)(x/(getWidth()/5f));if(n>=0&&n<5){page=n;invalidate();}return true;}
            if(page==0){if(y>450*D&&y<525*D){if(x<120*D)addMaintenance();else if(x<245*D)addFuel();else addExpense();return true;}if(y>525*D&&y<620*D){editVehicle();return true;}}
            if(page==1&&y>480*D&&y<560*D){addMaintenance();return true;}
            if(page==2&&y>getHeight()-170*D){addExpense();return true;}
            if(page==3&&y>90*D&&y<225*D){editVehicle();return true;}
            if(page==4){if(y>160*D&&y<225*D){editVehicle();return true;}if(y>500*D&&y<570*D){light=!light;getSharedPreferences("auto",0).edit().putBoolean("light",light).apply();applyBars();invalidate();return true;}}
            return true;}
    }
}
