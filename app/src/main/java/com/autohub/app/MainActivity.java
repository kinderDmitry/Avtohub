package com.autohub.app;

import android.app.*;
import android.os.Bundle;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;
import java.text.*;
import java.util.*;

public class MainActivity extends Activity {
    AutoHubView view;
    @Override public void onCreate(Bundle b){super.onCreate(b); getWindow().setStatusBarColor(Color.rgb(7,9,13)); getWindow().setNavigationBarColor(Color.rgb(7,9,13)); view=new AutoHubView(this); setContentView(view);}
    void addRecord(String type){
        final EditText input=new EditText(this); input.setHint(type.equals("Топливо")?"Например: 2984 ₽ / 42.7 л":"Например: 7800 ₽");
        new AlertDialog.Builder(this).setTitle("Добавить "+type).setMessage("Данные сохраняются локально на устройстве.").setView(input)
          .setNegativeButton("Отмена",null).setPositiveButton("Сохранить",(d,w)->{view.added++; view.invalidate();}).show();
    }

    class AutoHubView extends View {
        Paint p=new Paint(3); Path path=new Path(); int page=0; boolean light; int added=0; RectF r=new RectF();
        int blue=Color.rgb(20,133,255), green=Color.rgb(30,210,145), orange=Color.rgb(255,164,45), red=Color.rgb(255,75,88);
        String[] nav={"Главная","Обслуживание","Расходы","Авто","Ещё"};
        AutoHubView(Context c){super(c); light=getSharedPreferences("auto",0).getBoolean("light",false); p.setTypeface(Typeface.create("sans",Typeface.NORMAL)); setLayerType(View.LAYER_TYPE_SOFTWARE,null);}
        float d(){return getResources().getDisplayMetrics().density;}
        void text(Canvas c,String s,float x,float y,float size,int color,boolean bold){p.setStyle(Paint.Style.FILL);p.setColor(color);p.setTextSize(size*d());p.setTypeface(Typeface.create("sans",bold?Typeface.BOLD:Typeface.NORMAL));c.drawText(s,x,y,p);}
        void card(Canvas c,float l,float t,float rr,float bb){
            int fill=light?Color.argb(215,250,252,255):Color.argb(190,16,23,32); int stroke=light?Color.rgb(200,210,220):Color.rgb(27,78,125);
            p.setStyle(Paint.Style.FILL);p.setColor(fill);p.setShadowLayer(14*d(),0,6*d(),light?Color.argb(35,20,40,60):Color.argb(90,0,110,255));r.set(l,t,rr,bb);c.drawRoundRect(r,18*d(),18*d(),p);p.clearShadowLayer();
            p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(1*d());p.setColor(stroke);c.drawRoundRect(r,18*d(),18*d(),p);
            p.setShader(new LinearGradient(l,t,rr,bb, light?Color.argb(90,255,255,255):Color.argb(65,70,150,255),Color.TRANSPARENT,Shader.TileMode.CLAMP));p.setStyle(Paint.Style.FILL);c.drawRoundRect(r,18*d(),18*d(),p);p.setShader(null);
        }
        int fg(){return light?Color.rgb(25,31,38):Color.rgb(241,247,255);} int sub(){return light?Color.rgb(92,103,114):Color.rgb(156,171,188);} int bg(){return light?Color.rgb(242,245,248):Color.rgb(7,9,13);}
        @Override protected void onDraw(Canvas c){super.onDraw(c); float D=d(); c.drawColor(bg());
            p.setShader(new RadialGradient(getWidth()*0.55f,100*D,260*D,light?Color.argb(70,40,130,220):Color.argb(80,0,100,255),Color.TRANSPARENT,Shader.TileMode.CLAMP));c.drawRect(0,0,getWidth(),getHeight(),p);p.setShader(null);
            text(c,"AUTO HUB",24*D,38*D,21,fg(),true); text(c,pageTitle(),24*D,72*D,26,fg(),true); text(c,"◉",getWidth()-48*D,39*D,20,blue,true);
            if(page==0) home(c); else if(page==1) service(c); else if(page==2) expenses(c); else if(page==3) car(c); else more(c); nav(c);
        }
        String pageTitle(){return page==0?"Мой автомобиль":nav[page];}
        void home(Canvas c){float D=d();card(c,16*D,92*D,getWidth()-16*D,280*D);text(c,"Volkswagen Polo",34*D,124*D,20,fg(),true);text(c,"1.6 MPI · 110 л.с. · 2018",34*D,148*D,13,sub(),false);text(c,"POLO",getWidth()-92*D,128*D,13,blue,true);
            // stylized car
            p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(4*D);p.setColor(blue);path.reset();path.moveTo(55*D,205*D);path.lineTo(72*D,180*D);path.lineTo(105*D,168*D);path.lineTo(195*D,168*D);path.lineTo(225*D,183*D);path.lineTo(250*D,205*D);path.lineTo(258*D,225*D);path.lineTo(48*D,225*D);path.close();c.drawPath(path,p);p.setStyle(Paint.Style.FILL);c.drawCircle(85*D,225*D,15*D,p);c.drawCircle(220*D,225*D,15*D,p);
            text(c,"Пробег",34*D,252*D,12,sub(),false);text(c,"82 450 км",34*D,271*D,19,fg(),true);text(c,"До ТО",180*D,252*D,12,sub(),false);text(c,"3 500 км",180*D,271*D,19,fg(),true);
            small(c,16,296,188,370,"Обслуживание","Ближайшее ТО","3 500 км",blue);small(c,196,296,368,370,"Топливо","Средний расход","7.2 л/100 км",green);small(c,16,382,188,456,"Расходы","За месяц","24 680 ₽",orange);small(c,196,382,368,456,"Документы","Сроки в норме","3 активных",blue);
            text(c,"Ближайшие напоминания",18*D,493*D,18,fg(),true); card(c,16*D,510*D,getWidth()-16*D,570*D); text(c,"Замена масла и фильтров",32*D,540*D,15,fg(),true); text(c,"3 500 км · 28 дней",32*D,561*D,12,sub(),false); text(c,"›",getWidth()-40*D,548*D,26,blue,true);
        }
        void small(Canvas c,int l,int t,int rr,int b,String a,String b1,String b2,int col){float D=d();card(c,l*D,t*D,rr*D,b*D);text(c,a,l*D+14*D,t*D+25*D,13,sub(),false);text(c,b1,l*D+14*D,t*D+47*D,11,sub(),false);text(c,b2,l*D+14*D,t*D+67*D,15,fg(),true);}
        void service(Canvas c){float D=d();String[] a={"Моторное масло","Масляный фильтр","Воздушный фильтр","Салонный фильтр","Тормозная жидкость","Охлаждающая жидкость"};String[] b={"В норме","В норме","Через 3 500 км","Через 3 500 км","В норме","В норме"};for(int i=0;i<a.length;i++){float y=98+i*72;card(c,16*D,y*D,getWidth()-16*D,(y+62)*D);text(c,a[i],32*D,(y+25)*D,15,fg(),true);text(c,"Следующее обслуживание по дате/пробегу",32*D,(y+45)*D,10,sub(),false);text(c,b[i],getWidth()-118*D,(y+31)*D,10,i==2||i==3?orange:green,true);}fab(c,"+");}
        void expenses(Canvas c){float D=d();card(c,16*D,98*D,getWidth()-16*D,215*D);text(c,"Расходы за месяц",32*D,128*D,13,sub(),false);text(c,"24 680 ₽",32*D,160*D,28,fg(),true);text(c,"↓ 18%",getWidth()-100*D,158*D,14,green,true);text(c,"Топливо  45%",32*D,190*D,12,sub(),false);text(c,"ТО и ремонт  20%",170*D,190*D,12,sub(),false);
            text(c,"Последние расходы",18*D,252*D,18,fg(),true); String[] e={"Топливо","Замена масла","Мойка"};String[] v={"2 984 ₽","7 800 ₽","600 ₽"};for(int i=0;i<3;i++){float y=270+i*68;card(c,16*D,y*D,getWidth()-16*D,(y+56)*D);text(c,e[i],32*D,(y+23)*D,14,fg(),true);text(c,"21.09.2026",32*D,(y+43)*D,10,sub(),false);text(c,v[i],getWidth()-92*D,(y+33)*D,13,fg(),true);}fab(c,"+");}
        void car(Canvas c){float D=d();card(c,16*D,98*D,getWidth()-16*D,215*D);text(c,"Volkswagen Polo",32*D,130*D,21,fg(),true);text(c,"1.6 MPI · 110 л.с. · 2018",32*D,153*D,13,sub(),false);text(c,"Госномер",32*D,183*D,11,sub(),false);text(c,"А123ВС 799",32*D,202*D,14,fg(),true);String[] rows={"Профиль автомобиля","История обслуживания","Все расходы","Напоминания","Документы","Шины","Статистика"};for(int i=0;i<rows.length;i++){float y=235+i*49;card(c,16*D,y*D,getWidth()-16*D,(y+42)*D);text(c,rows[i],34*D,(y+27)*D,14,fg(),true);text(c,"›",getWidth()-38*D,(y+27)*D,22,blue,true);}}
        void more(Canvas c){float D=d();String[] rows={"Настройки","Уведомления","Резервное копирование","Экспорт данных","О приложении","Тема интерфейса: "+(light?"светлая":"тёмная")};for(int i=0;i<rows.length;i++){float y=104+i*62;card(c,16*D,y*D,getWidth()-16*D,(y+52)*D);text(c,rows[i],34*D,(y+32)*D,15,fg(),true);}card(c,16*D,492*D,getWidth()-16*D,575*D);text(c,"Переключить тему",34*D,520*D,14,fg(),true);text(c,light?"Светлая → Тёмная":"Тёмная → Светлая",34*D,542*D,12,blue,true);}
        void fab(Canvas c,String s){float D=d();p.setColor(blue);p.setStyle(Paint.Style.FILL);p.setShadowLayer(18*D,0,5*D,Color.argb(130,0,120,255));c.drawCircle(getWidth()-42*D,getHeight()-92*D,28*D,p);p.clearShadowLayer();text(c,s,getWidth()-50*D,getHeight()-84*D,27,Color.WHITE,true);}
        void nav(Canvas c){float D=d();float y=getHeight()/D-74;card(c,10*D,y*D,getWidth()-10*D,(y+60)*D);for(int i=0;i<5;i++){float x=(getWidth()/D)*(i+.5f)/5;int col=i==page?blue:sub();text(c,nav[i],x-(nav[i].length()*3.2f)*D,(y+36)*D,10,col,i==page);}}
        @Override public boolean onTouchEvent(android.view.MotionEvent e){if(e.getAction()!=MotionEvent.ACTION_UP)return true;float D=d(),x=e.getX()/D,y=e.getY()/D;
            float navY=getHeight()/D-74;if(y>navY){int n=(int)(x/(getWidth()/D)*5);if(n>=0&&n<5){page=n;invalidate();}return true;}
            if(page==1&&x>getWidth()/D-90&&y>getHeight()/D-150){addRecord("обслуживание");return true;}
            if(page==2&&x>getWidth()/D-90&&y>getHeight()/D-150){addRecord("расход");return true;}
            if(page==0&&y>500&&y<575){addRecord("обслуживание");return true;}
            if(page==4&&y>485&&y<570){light=!light;getSharedPreferences("auto",0).edit().putBoolean("light",light).apply();getWindow().setStatusBarColor(light?Color.rgb(242,245,248):Color.rgb(7,9,13));getWindow().setNavigationBarColor(light?Color.rgb(242,245,248):Color.rgb(7,9,13));invalidate();}
            return true;}
    }
}
