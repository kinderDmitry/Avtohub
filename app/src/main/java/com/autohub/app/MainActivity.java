package com.autohub.app;

import android.app.*;
import android.os.Bundle;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;
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
        getWindow().setStatusBarColor(light?Color.rgb(238,242,246):Color.rgb(5,7,11));
        getWindow().setNavigationBarColor(light?Color.rgb(238,242,246):Color.rgb(5,7,11));
    }
    void addRecord(String type){
        LinearLayout box=new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(0,8,0,0);
        EditText value=new EditText(this); value.setHint(type.equals("Топливо")?"Сумма / литры / пробег":"Стоимость / пробег / комментарий"); box.addView(value);
        new AlertDialog.Builder(this).setTitle("Добавить · "+type).setMessage("Запись будет сохранена на устройстве.").setView(box)
            .setNegativeButton("Отмена",null).setPositiveButton("Сохранить",(d,w)->{view.records++;view.invalidate();}).show();
    }

    class AutoHubView extends View {
        Paint p=new Paint(Paint.ANTI_ALIAS_FLAG); Path path=new Path(); RectF r=new RectF();
        int page=0, records=0; boolean light; float downX,downY;
        final int blue=Color.rgb(40,145,255), blue2=Color.rgb(84,180,255), green=Color.rgb(35,205,143), orange=Color.rgb(255,171,58), red=Color.rgb(255,78,94);
        final String[] nav={"Главная","ТО","Расходы","Авто","Ещё"};
        AutoHubView(Context c){super(c); light=getSharedPreferences("auto",0).getBoolean("light",false); setLayerType(View.LAYER_TYPE_SOFTWARE,null);}
        float d(){return getResources().getDisplayMetrics().density;}
        int bg(){return light?Color.rgb(238,242,246):Color.rgb(5,7,11);} int fg(){return light?Color.rgb(23,29,36):Color.rgb(245,248,252);} int sub(){return light?Color.rgb(91,103,116):Color.rgb(151,166,183);} int glass(){return light?Color.argb(205,255,255,255):Color.argb(175,18,25,35);}
        void text(Canvas c,String s,float x,float y,float size,int color,boolean bold){p.setShader(null);p.setStyle(Paint.Style.FILL);p.setColor(color);p.setTextSize(size*d());p.setTypeface(Typeface.create("sans",bold?Typeface.BOLD:Typeface.NORMAL));c.drawText(s,x,y,p);}
        void glass(Canvas c,float l,float t,float rr,float bb,float radius){
            r.set(l,t,rr,bb); p.setStyle(Paint.Style.FILL);p.setColor(glass());p.setShadowLayer(18*d(),0,8*d(),light?Color.argb(32,20,35,50):Color.argb(90,0,90,210));c.drawRoundRect(r,radius*d(),radius*d(),p);p.clearShadowLayer();
            p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(1*d());p.setColor(light?Color.argb(150,255,255,255):Color.argb(115,70,135,190));c.drawRoundRect(r,radius*d(),radius*d(),p);
            LinearGradient g=new LinearGradient(l,t,rr,bb,light?Color.argb(120,255,255,255):Color.argb(75,105,185,255),Color.TRANSPARENT,Shader.TileMode.CLAMP);p.setShader(g);p.setStyle(Paint.Style.FILL);c.drawRoundRect(r,radius*d(),radius*d(),p);p.setShader(null);
        }
        void dot(Canvas c,float x,float y,int color){p.setColor(color);p.setStyle(Paint.Style.FILL);p.setShadowLayer(10*d(),0,0,Color.argb(100, color>>16&255,color>>8&255,color&255));c.drawCircle(x,y,4*d(),p);p.clearShadowLayer();}
        @Override protected void onDraw(Canvas c){super.onDraw(c);float D=d();c.drawColor(bg());
            p.setShader(new RadialGradient(getWidth()*.62f,80*D,300*D,light?Color.argb(70,65,145,230):Color.argb(85,0,110,255),Color.TRANSPARENT,Shader.TileMode.CLAMP));c.drawRect(0,0,getWidth(),getHeight()*.55f,p);p.setShader(null);
            header(c);
            if(page==0)home(c); else if(page==1)service(c); else if(page==2)expenses(c); else if(page==3)car(c); else more(c);
            bottom(c);
        }
        void header(Canvas c){float D=d();text(c,"AUTO HUB",20*D,34*D,18,fg(),true);text(c,pageTitle(),20*D,67*D,27,fg(),true);text(c,"●",getWidth()-49*D,34*D,16,blue,true);text(c,"⋮",getWidth()-25*D,35*D,20,sub(),true);}
        String pageTitle(){return page==0?"Мой автомобиль":nav[page];}
        void home(Canvas c){float D=d();
            glass(c,16*D,88*D,getWidth()-16*D,300*D,24);
            text(c,"VOLKSWAGEN",32*D,119*D,11,sub(),true);text(c,"Polo",32*D,147*D,30,fg(),true);text(c,"1.6 MPI  ·  110 л.с.  ·  2018",32*D,169*D,12,sub(),false);
            // abstract premium car silhouette
            p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(3*D);p.setStrokeCap(Paint.Cap.ROUND);p.setColor(blue2);path.reset();path.moveTo(48*D,231*D);path.cubicTo(76*D,205*D,91*D,191*D,121*D,188*D);path.lineTo(190*D,188*D);path.cubicTo(213*D,191*D,229*D,210*D,251*D,231*D);path.lineTo(48*D,231*D);c.drawPath(path,p);p.setStyle(Paint.Style.FILL);c.drawCircle(86*D,231*D,13*D,p);c.drawCircle(216*D,231*D,13*D,p);p.setStyle(Paint.Style.STROKE);p.setColor(light?Color.rgb(150,165,180):Color.rgb(50,70,92));c.drawLine(123*D,188*D,135*D,231*D,p);c.drawLine(190*D,188*D,180*D,231*D,p);p.setStyle(Paint.Style.FILL);
            text(c,"82 450",32*D,272*D,25,fg(),true);text(c,"км",126*D,272*D,12,sub(),false);text(c,"ПРОБЕГ",32*D,288*D,9,sub(),true);
            text(c,"ТО",190*D,255*D,9,sub(),true);text(c,"3 500 км",190*D,276*D,16,green,true);text(c,"до обслуживания",190*D,291*D,9,sub(),false);
            glass(c,16*D,316*D, getWidth()/2-6*D,404*D,18); glass(c,getWidth()/2+6*D,316*D,getWidth()-16*D,404*D,18);
            metric(c,31,341,"РАСХОД","7.2 л/100 км",green);metric(c,(int)(getWidth()/D/2+21),341,"ЗА МЕСЯЦ","24 680 ₽",orange);
            text(c,"Ближайшее",20*D,439*D,18,fg(),true);text(c,"Все",getWidth()-48*D,439*D,12,blue,true);
            reminder(c,16,454,"Замена масла и фильтра","3 500 км  ·  28 дней",orange);reminder(c,16,522,"Проверка тормозов","6 200 км  ·  62 дня",green);
        }
        void metric(Canvas c,int x,int y,String a,String b,int col){float D=d();text(c,a,x*D,y*D,9,sub(),true);text(c,b,x*D,(y+30)*D,16,fg(),true);dot(c,x*D+8*D,(y+51)*D,col);text(c,"актуально",x*D+19*D,(y+54)*D,9,sub(),false);}
        void reminder(Canvas c,int x,int y,String a,String b,int col){float D=d();glass(c,x*D,y*D,getWidth()-16*D,(y+56)*D,17);dot(c,31*D,(y+27)*D,col);text(c,a,45*D,(y+24)*D,13,fg(),true);text(c,b,45*D,(y+43)*D,10,sub(),false);text(c,"›",getWidth()-36*D,(y+35)*D,22,sub(),false);}
        void service(Canvas c){float D=d();text(c,"Состояние систем",20*D,100*D,13,sub(),true);String[] a={"Моторное масло","Масляный фильтр","Воздушный фильтр","Салонный фильтр","Тормозная жидкость","Охлаждающая жидкость","Свечи зажигания"};String[] b={"3 500 км","3 500 км","6 000 км","6 000 км","В норме","В норме","12 000 км"};for(int i=0;i<a.length;i++){float y=112+i*62;glass(c,16*D,y*D,getWidth()-16*D,(y+52)*D,16);text(c,a[i],32*D,(y+23)*D,13,fg(),true);text(c,b[i],32*D,(y+42)*D,10,sub(),false);dot(c,getWidth()-45*D,(y+25)*D,i==0||i==1?orange:green);}glass(c,16*D,558*D,getWidth()-16*D,616*D,18);text(c,"+ Добавить обслуживание",34*D,593*D,13,blue,true);}
        void expenses(Canvas c){float D=d();glass(c,16*D,92*D,getWidth()-16*D,220*D,22);text(c,"РАСХОДЫ ЗА МЕСЯЦ",32*D,121*D,10,sub(),true);text(c,"24 680 ₽",32*D,157*D,29,fg(),true);text(c,"18%",getWidth()-67*D,157*D,13,green,true);text(c,"меньше прошлого месяца",32*D,178*D,10,sub(),false);bar(c,32,195,170,green,"Топливо");bar(c,176,195,248,blue,"ТО");bar(c,254,195,318,orange,"Прочее");text(c,"Последние операции",20*D,255*D,18,fg(),true);String[] a={"Топливо","Замена масла","Мойка","Запчасти"};String[] b={"2 984 ₽","7 800 ₽","600 ₽","4 200 ₽"};for(int i=0;i<a.length;i++){float y=270+i*61;glass(c,16*D,y*D,getWidth()-16*D,(y+50)*D,15);text(c,a[i],32*D,(y+22)*D,13,fg(),true);text(c,"21.09.2026  ·  82 450 км",32*D,(y+40)*D,9,sub(),false);text(c,b[i],getWidth()-88*D,(y+30)*D,12,fg(),true);}}
        void bar(Canvas c,int x,int y,int rr,int col,String label){float D=d();p.setColor(col);p.setStyle(Paint.Style.FILL);c.drawRoundRect(x*D,y*D,rr*D,(y+5)*D,3*D,3*D,p);text(c,label,x*D,(y+19)*D,8,sub(),false);}
        void car(Canvas c){float D=d();glass(c,16*D,94*D,getWidth()-16*D,210*D,22);text(c,"VOLKSWAGEN POLO",32*D,125*D,18,fg(),true);text(c,"1.6 MPI  ·  110 л.с.  ·  2018",32*D,147*D,11,sub(),false);text(c,"82 450 км",32*D,181*D,20,fg(),true);text(c,"Текущий пробег",142*D,181*D,10,sub(),false);text(c,"● В эксплуатации",getWidth()-120*D,181*D,9,green,true);String[] rows={"Профиль автомобиля","История обслуживания","Напоминания","Документы","Шины","Статистика"};for(int i=0;i<rows.length;i++){float y=226+i*56;glass(c,16*D,y*D,getWidth()-16*D,(y+46)*D,15);text(c,rows[i],34*D,(y+29)*D,13,fg(),true);text(c,"›",getWidth()-38*D,(y+30)*D,21,sub(),false);}}
        void more(Canvas c){float D=d();text(c,"Персонализация",20*D,100*D,13,sub(),true);String[] rows={"Тема интерфейса","Уведомления","Единицы измерения","Резервная копия","Экспорт данных","О приложении"};for(int i=0;i<rows.length;i++){float y=112+i*57;glass(c,16*D,y*D,getWidth()-16*D,(y+47)*D,15);text(c,rows[i],34*D,(y+29)*D,13,fg(),true);if(i==0){text(c,light?"Светлая":"Тёмная",getWidth()-91*D,(y+29)*D,10,blue,true);}else text(c,"›",getWidth()-38*D,(y+30)*D,21,sub(),false);}glass(c,16*D,470*D,getWidth()-16*D,548*D,18);text(c,light?"Переключить на тёмную тему":"Переключить на светлую тему",34*D,502*D,13,fg(),true);text(c,"Mirror Glass",34*D,525*D,10,sub(),false);}
        void bottom(Canvas c){float D=d();float y=getHeight()/D-72;glass(c,10*D,y*D,getWidth()-10*D,(y+58)*D,22);for(int i=0;i<5;i++){float x=getWidth()*(i+.5f)/5;int col=i==page?blue:sub();if(i==page){p.setColor(light?Color.argb(30,40,145,255):Color.argb(40,40,145,255));p.setStyle(Paint.Style.FILL);c.drawCircle(x,(y+28)*D,21*D,p);}text(c,nav[i],x-(nav[i].length()*3.0f)*D,(y+35)*D,9,col,i==page);}}
        @Override public boolean onTouchEvent(MotionEvent e){float D=d();if(e.getAction()==MotionEvent.ACTION_DOWN){downX=e.getX();downY=e.getY();return true;}if(e.getAction()!=MotionEvent.ACTION_UP)return true;float x=e.getX(),y=e.getY();if(Math.abs(x-downX)>90*D){if(x<downX)page=Math.min(4,page+1);else page=Math.max(0,page-1);invalidate();return true;}float navY=getHeight()-72*D;if(y>navY){int n=(int)(x/(getWidth()/5f));if(n>=0&&n<5){page=n;invalidate();}return true;}if(page==4&&y>460*D&&y<555*D){light=!light;getSharedPreferences("auto",0).edit().putBoolean("light",light).apply();applyBars();invalidate();return true;}if((page==1||page==2)&&y>getHeight()-150*D){addRecord(page==1?"Обслуживание":"Расход");return true;}return true;}
    }
}
