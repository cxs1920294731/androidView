package com.example.hakim.anview;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Chronometer;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/12/5.
 */


public class sendSMS {
    Context context;
    SQLiteDatabase db;
    public class ReceiverListner extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try
            {
                // 获取短信状态
                switch (getResultCode()) {
                    // 短信发送成功
                    case Activity.RESULT_OK:
                        //update save_text_table set content='"+text+"' where id="+id num varchar(225),is_send integer
                        //
                        //String x=intent.getStringExtra("msg");
                        int index=getResId(intent.getStringExtra("msg"));
                        db.execSQL("update send_result_table set res='发出'  where resID="+index+" and num='"+intent.getStringExtra("msg")+"'");
                        //Toast.makeText(context, "短信发出"+intent.getStringExtra("msg"), Toast.LENGTH_LONG).show();
                        break;
                    // 短信发送不成功
                    default:
                        //db.execSQL("insert into send_result_table VALUES (0,"+intent.getStringExtra("msg")+",'发送失败')");
                        Toast.makeText(context, "发送失败，请重新发送！", Toast.LENGTH_LONG).show();
                        break;
                }
            }catch (Exception e) {
                String x="";
                x="ca0";
                //Toast.makeText(MessagActivity.this, "发送出现异常，请重新发送！", Toast.LENGTH_LONG).show();
            }
        }
    }
    public class AcceptListner extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()) {
                // 短信发送成功
                case Activity.RESULT_OK:
                    int index=getResId(intent.getStringExtra("msg"));
                    db.execSQL("update send_result_table set res='接收' where resID="+index+" and num='"+intent.getStringExtra("msg")+"'");
                    //Toast.makeText(context, "对方失败，请重新发送！", Toast.LENGTH_LONG).show();
                    break;
                // 短信发送不成功
                default:
                    //db.execSQL("insert into send_result_table VALUES (0,"+intent.getStringExtra("msg")+",'发送失败')");
                    Toast.makeText(context, "对方失败，请重新发送！", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
    public sendSMS(Context context,SQLiteDatabase db){
        this.context=context;
        this.db=db;
    }
    ReceiverListner receiverlistner = new ReceiverListner();
    IntentFilter intentfilter = new IntentFilter();
    AcceptListner acceptListner=new AcceptListner();
    IntentFilter intentfilterA=new IntentFilter();
    private static String SMS_SEND_ACTIOIN = "SMS_SEND_ACTIOIN";
    private static String SMS_DELIVERED_ACTION = "SMS_DELIVERED_ACTION";
    //发送短信并实现监听
    public void SendMsgIfSuc(String num,String msg,String name,String id){
        SmsManager sms= SmsManager.getDefault();
        try {
            Intent SendIt =new Intent(SMS_SEND_ACTIOIN);
            SendIt.putExtra("msg",num);
            Intent DevliverIt=new Intent(SMS_DELIVERED_ACTION);
            DevliverIt.putExtra("msg",num);
            //Intent DevliverIt = new Intent(SMS_DELIVERED_ACTION);
            PendingIntent SendPendIt = PendingIntent.getBroadcast(context.getApplicationContext(), 0, SendIt, PendingIntent.FLAG_CANCEL_CURRENT);
            PendingIntent DeliverIt = PendingIntent.getBroadcast(context.getApplicationContext(),0,DevliverIt,0);
            // 发送短信
            sms.sendTextMessage(num, null, msg, SendPendIt, DeliverIt);
            //id integer,num varchar(225),res varchar(255),name varchar(255))
            int Numid=Integer.parseInt(id);
            android.text.format.Time t = new android.text.format.Time();
            t.setToNow();
            String senfTime= getTime();
            //id integer,num varchar(225),res varchar(255),name varchar(255),sendTime varchar(255))");
            db.execSQL("insert into send_result_table (id,num,res,name,sendTime) VALUES ("+Numid+","+num+",'发送未成功','"+name+"','"+senfTime+"')");
            db.execSQL("update send_sms_table set is_send=1 where num='"+num+"'");
        }catch (Exception e){
            Toast.makeText(context, e.toString()
                    , Toast.LENGTH_SHORT).show();
        }
        intentfilter.addAction(SMS_SEND_ACTIOIN);
        context.registerReceiver(receiverlistner, intentfilter);
        //是否接受
        intentfilterA.addAction(SMS_DELIVERED_ACTION);
        context.registerReceiver(acceptListner,intentfilterA);
    }
    private String getTime(){
        android.text.format.Time t = new android.text.format.Time();
        t.setToNow();
        String timeX=t.year+"-"+(t.month+1)+"-"+t.monthDay+" "+t.hour+":"+t.minute;
        return timeX;
    }
    private int getResId(String num){
        int res=0;
        // db.execSQL("update send_result_table set res='发出'  where num='"+intent.getStringExtra("msg")+"'");
        Cursor cursor=db.rawQuery("select * from  send_result_table where num='"+num+"'",null);
        if (cursor!=null){
            while (cursor.moveToNext()){
                res=cursor.getInt(cursor.getColumnIndexOrThrow("resID"));
            }
        }
        return res;
    }
}
