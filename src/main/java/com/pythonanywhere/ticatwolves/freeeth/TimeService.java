package com.pythonanywhere.ticatwolves.freeeth;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

public class TimeService extends Service {
    //MyTimerAsynTask task;
    //MainContent taski;
    Handler handler = new Handler();
    //MyValues m;
    static int sec=60,min=29;
    static Boolean done=false,show=true;//=false;
    static String show_time,tsec,tmin;
    SharedPreferences sh;

    SharedPreferences.Editor myeditor;

    public TimeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }



    @Override
    public void onCreate() {
        super.onCreate();
        //Toast.makeText(this,"Service is running",Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sh = getSharedPreferences("time",MODE_PRIVATE);
        myeditor = sh.edit();
        sec = sh.getInt("seconds",60);
        min = sh.getInt("minutes",29);

        if(!done){
            handler.post(abc);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    Runnable abc = new Runnable() {
        @Override
        public void run() {
            if(done && show){
                Toast.makeText(getApplicationContext(),"you can claim",Toast.LENGTH_SHORT).show();
                //done=false;
                NotificationManagerCompat mynoti = NotificationManagerCompat.from(getApplicationContext());
                NotificationCompat.Builder myno = new NotificationCompat.Builder(getApplicationContext());
                myno.setContentTitle("FREE ETH");
                myno.setContentText("You can Claim your ETH");
                myno.setSmallIcon(R.drawable.icon);
                Intent l1 = new Intent(getApplicationContext(),MainActivity.class);
                PendingIntent pd = PendingIntent.getActivity(getApplicationContext(),1,l1,0);
                myno.setContentIntent(pd);
                show =false;
                done=true;
                myno.setAutoCancel(true);
                mynoti.notify(1,myno.build());
                Log.i("Bilol Calim","Claim");
                myeditor.putString("showtime","claim");
                myeditor.putBoolean("done",true);
                myeditor.commit();
                sec=60;min=29;
                myeditor.putInt("minutes",29);
                myeditor.putInt("seconds",60);
            }
            else if(!done) {
                sec=sec-1;
                if(sec==0 && min==0){
                    done=true;
                    show=true;
                }
                else if(sec<=0){
                    sec = 60;
                    show_time=min+":00";
                    if(min>0) {
                        min = min - 1;
                    }
                }
                if(sec<10 || min<10){
                    if(sec<10){tsec="0"+sec;}
                    else {tsec=""+sec;}
                    if(min<10){tmin="0"+min;}
                    else{tmin=""+tmin;}
                }
                else {
                    tmin=""+min;
                    tsec=""+sec;
                }
                show_time=tmin+":"+tsec;
                myeditor.putString("showtime",show_time);
                myeditor.putInt("minutes",min);
                myeditor.putInt("seconds",sec);
                myeditor.putInt("perc",(min*60)+sec);
                myeditor.commit();
            }
            handler.postDelayed(this,1000);
        }
    };
}
