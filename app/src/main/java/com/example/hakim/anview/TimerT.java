package com.example.hakim.anview;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/12/21.
 */

public class TimerT {
    private Time t = new Time();
    private Timer timer = new Timer();
    private Handler myhandler;
    private Context context;
    public SQLiteDatabase db;
    final private int timeunit= 1000;
    private sendSMS send;
    private saveSet redSet;
    public TimerT(final Context context,SQLiteDatabase db){
        this.context=context;
        this.db=db;
        this.send=new sendSMS(context,db);
        this.redSet=new saveSet(context,db);
    }

    public boolean isTime(){
        int[] a = new int[2];
        a = redSet.get_start_end_time();
        t.setToNow();
        Integer x = t.hour;
        if (a[0] > a[1]) {
            //包24小时
            if ((a[0] <= x && a[1] >= 24) || (x <= a[1] && x >= 0)) {
                //timer.cancel();
                return true;
            }
        } else {
            //不包24
            if (a[0] <= x && a[1] >= x) {
                return true;
            }
        }
        return false;
    }
    //开始计时
    public void startTime(){
        if (isTime()){
            String  text;
            ArrayList<String> sending=redSet.sendPone();
            text = redSet.selectText();
            if (text==""||text==null){
                return;
            }
            if (sending.get(2) == "" || sending.get(2) == null){
                return;
            }
            Toast.makeText(context, text
                    , Toast.LENGTH_SHORT).show();
            //String num,String msg,String name,String id
            send.SendMsgIfSuc(sending.get(2), text,sending.get(1),sending.get(0));
        }
    }
    //得到设置的时间搓
    public  int getRandTime(){
        int res;
        Random rand = new Random();
        int time = redSet.get_inter_time();
        int i=redSet.get_rand_time();
        int max = time + i;
        int min = time - i;
        res = (rand.nextInt(max) % (i + 1) + min)*60*1000;
        return res;
    }
}
