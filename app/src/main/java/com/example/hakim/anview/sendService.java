package com.example.hakim.anview;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;
public class sendService extends Service {
    private final static String TAG="PlayerMUsic";
    private MediaPlayer mediaPlayer;
    private int count=0;
    private Long SetTime;
    private TimerT timerT;
    private MyData dehelper;
    private PowerManager.WakeLock wakeLock = null;
    mytask timeTask;
    private Integer rand;
    SQLiteDatabase db;
    Timer timer;
    public sendService() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
        //Notification.Builder builder = new Notification.Builder(this);
        Notification.Builder mNotifyBuilder = new Notification.Builder(sendService.this)
                .setSmallIcon(R.drawable.ic_dashboard_black_24dp)
                .setTicker("正在发短信")
                .setWhen(System.currentTimeMillis())
                .setContentTitle("进行进行")
                .setContentText("正在发")
                .setContentIntent(pendingIntent);
        Notification notification = mNotifyBuilder.build();
        /*使用startForeground,如果id为0，那么notification将不会显示*/
        startForeground(100, notification);
        //播放音乐
        mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.silent);
        mediaPlayer.setLooping(true);
        timer = new Timer();
        timeTask=new mytask();
        dehelper = new MyData(this, "mySend.db3", null, 1);
        db = dehelper.getReadableDatabase();
        timerT =new TimerT(this,db);
        int randTime=timerT.getRandTime();
        count=randTime/1000;
        long in=new Long((long)randTime);
        SetTime = System.currentTimeMillis()+in;
        timer.schedule(timeTask, 0, 1000);
        acquire();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                startPlayMusic();
            }
        }).start();
        return  START_STICKY;
    }
    @Override
    public void onDestroy() {
        stopForeground(true);
//        timer.cancel();
//        timer=null;
//        timeTask=null;
        super.onDestroy();
        super.stopSelf();
        release();
    }
    private void acquire()
    {
        if (null == wakeLock)
        {
            PowerManager pm = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE, "sendService");
            if (null != wakeLock)
            {
                wakeLock.acquire();
            }
        }
    }
    private void release()
    {
        if (null != wakeLock)
        {
            wakeLock.release();
            wakeLock = null;
        }
    }
    class mytask extends TimerTask {
        @Override
        public void run() {
            Intent inte = new Intent();
            inte.putExtra("count", count);
            inte.setAction("com.example.hakim.anview.MyReceiver");
            sendBroadcast(inte);
            Long x=System.currentTimeMillis();
            if(timerT.isTime()){
                if (System.currentTimeMillis() > SetTime) {
                    Long y=System.currentTimeMillis();
                    int randTime=timerT.getRandTime();
                    count=randTime/1000;
                    long in=new Long((long)randTime);
                    SetTime = System.currentTimeMillis()+in;
                    timerT.startTime();
                    id_return();
                    //SetTime = System.currentTimeMillis() + timerT.getRandTime();
                }
                count--;
            }else {
                count--;
                if (count<-100){
                    count=-1;
                }
            }
        }
    }
    public void id_return(){
        Cursor cursor = db.rawQuery("select * from send_sms_table  where is_send=0 limit 0,1", null);
        if (cursor!=null){
            if (cursor.getCount()<=0){
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onDestroy();
                    }
                }, 3000);
            }
        }else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onDestroy();
                }
            }, 3000);
        }
    }
    private void startPlayMusic(){
        if(mediaPlayer != null){
            mediaPlayer.start();
        }
    }
    private void stopPlayMusic(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
        }
    }
}
