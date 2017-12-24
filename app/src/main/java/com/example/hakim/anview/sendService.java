package com.example.hakim.anview;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2017/12/25.
 */

public class sendService extends IntentService {
    private int count;
    public sendService() {
        super("sendService");
    }
    private MyBinder binder=new MyBinder();
    public class MyBinder extends Binder{
        public int getCount(){
            count=1;
            return count;
        }
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int count=1;
        Intent inte=new Intent();
        intent.putExtra("count", count);
        intent.setAction("com.example.hakim.anview.sendService");
        sendBroadcast(inte);
    }
    @Override
    public IBinder onBind(Intent inter){
        return binder;
    }
}
