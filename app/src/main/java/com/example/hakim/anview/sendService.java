package com.example.hakim.anview;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/12/25.
 */

public class sendService extends IntentService {
    private int count=0;
    public sendService() {
        super("sendService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Timer timer = new Timer();
        mytask timeTask=new mytask();
        timer.schedule(timeTask, 0, 1000);
    }
    class mytask extends TimerTask {
        @Override
        public void run() {
            count++;
            Intent inte=new Intent();
            inte.putExtra("count", count);
            inte.setAction("com.example.hakim.anview.MyReceiver");
            sendBroadcast(inte);
        }
    }
}
