package com.example.hakim.anview;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/12/20.
 */

public class readContacts {
    private Context context;
    private Cursor cursor_num;
    public readContacts(Context context){
        this.context=context;
    }
    private String start;
    private String end;
    private ArrayList<ArrayList<String>> dataPhone=new ArrayList<>();
    public ArrayList<String> read(String start,String end){
        this.start=start;
        this.end=end;
        ArrayList<String> res=new ArrayList<String>();
        ArrayList<String> sendList=new ArrayList<String>();
        ArrayList<String> no_sendList=new ArrayList<String>();
        dataPhone.clear();
        cursor_num=context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, android.provider.ContactsContract.Contacts.SORT_KEY_PRIMARY);
        int i=0;
        Boolean sta=false;
        if (cursor_num != null) {
            while (cursor_num.moveToNext()) {
                i++;
                // 得到手机号码
                ArrayList<String> row=new ArrayList<>();
                String phoneNumber = cursor_num.getString(cursor_num
                        .getColumnIndex(ContactsContract
                                .CommonDataKinds.Phone.NUMBER))
                        .replace("-", "")
                        .replace(" ", "")
                        .replace("+", "");
                String PhoneID = cursor_num.getString(cursor_num.getColumnIndex(android.provider.ContactsContract.Contacts._ID));
                String phoneName = cursor_num.getString(cursor_num.getColumnIndex(android.provider.ContactsContract.Contacts.DISPLAY_NAME));
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (phoneName.equals(start)){
                    sta=true;
                }

                if (sta){
                    if (is_cell_num(phoneNumber)){
                        row.add(phoneName);
                        row.add(phoneNumber);
                        dataPhone.add(row);
                        sendList.add(i+"--"+phoneName+"--"+phoneNumber);
                    }else {
                        no_sendList.add(i+"--"+phoneName+"--"+phoneNumber);
                    }
                }
                if (phoneName.equals(end)){
                    sta=false;
                }
            }
            res.add("不符合条件的号码");
            res.addAll(no_sendList);
            res.add("符合条件的号码");
            res.addAll(sendList);
        }
        return res;
    }
    public Boolean saveNumber(SQLiteDatabase db){
        try{
            db.execSQL("delete from save_monbile_table");
            for (int i=0;i< dataPhone.size();i++){
                String phoneName=  dataPhone.get(i).get(0);
                String phoneNumber=dataPhone.get(i).get(1);
                db.execSQL("insert into save_monbile_table (monbile,name) values ('"+phoneNumber+"','"+phoneName+"')");
            }
            Toast.makeText(context, "保存成功"
                    , Toast.LENGTH_SHORT).show();
            return true;
        }catch (Exception e){
            Toast.makeText(context, "保存失败"
                    , Toast.LENGTH_SHORT).show();
            return true;
        }
    }
    public ArrayList<String> getPhoneList(SQLiteDatabase db){
        ArrayList<String> res=new ArrayList<String>();
        Cursor cursor1=db.rawQuery("select * from save_monbile_table",null);
        if (cursor1!=null){
            while (cursor1.moveToNext()){
                String sms = cursor1.getString(cursor1.getColumnIndex("monbile")).toString();
                String smsID = cursor1.getString(cursor1.getColumnIndexOrThrow("id")).toString();
                String smsName = cursor1.getString(cursor1.getColumnIndexOrThrow("name")).toString();
                res.add(smsID+"---"+smsName+"---"+sms);
            }
        }
        return res;
    }
    public Cursor sendList(SQLiteDatabase db,String start,String end){
        Cursor cursor=null;
        int startID=0,endID=0;
        Cursor cursor1=db.rawQuery("select * from save_monbile_table where name='"+start+"'",null);
        startID=getFirst(cursor1);
        Cursor cursor2=db.rawQuery("select * from save_monbile_table where name='"+end+"'",null);
        endID=getFirst(cursor2);
        cursor=db.rawQuery("select * from save_monbile_table where id>="+startID+" and id<="+endID,null);
        return cursor;
    }
    public Boolean is_cell_num(String mobiles){
        Pattern p = Pattern.compile("^(0|86|17951)?(13[0-9]|15[0-9]|17[0-9]|18[0-9]|14[0-9])[0-9]{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    //得到未发送的列表
    public ArrayList<String> getList(SQLiteDatabase db){
        //"select * from " + name + " where is_send = 0"
        //num varchar(225),is_send integer,name varchar(225),numID integer
        ArrayList<String> res = new  ArrayList<String>();
        Cursor cursor= db.rawQuery("select * from send_sms_table where is_send = 0",null );
        if (cursor!=null){
            while (cursor.moveToNext()){
                String sms = cursor.getString(cursor.getColumnIndex("num"));
                String smsID = cursor.getString(cursor.getColumnIndexOrThrow("numID"));
                String smsName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                res.add(smsID+"---"+smsName+"---"+sms);
            }
        }
        return res;
    }
    //得到发送的结果
    public ArrayList<String> getRes(SQLiteDatabase db){
        ArrayList<String> res = new  ArrayList<String>();
        Cursor cursor= db.rawQuery("select * from send_result_table",null );
        if (cursor!=null&&cursor.getCount()>0){
            cursor.moveToLast();
            String sms = cursor.getString(cursor.getColumnIndex("num"));
            String smsID = cursor.getString(cursor.getColumnIndexOrThrow("id"));
            String smsName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String msg=cursor.getString(cursor.getColumnIndexOrThrow("res"));
            String sendTime=cursor.getString(cursor.getColumnIndexOrThrow("sendTime"));
            res.add(smsID+"---"+smsName+"---"+sms+"---"+msg+"--"+sendTime);
            while (cursor.moveToPrevious()){
                //id integer,num varchar(225),res varchar(255),name varchar(255))"
                sms = cursor.getString(cursor.getColumnIndex("num"));
                smsID = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                smsName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                msg=cursor.getString(cursor.getColumnIndexOrThrow("res"));
                sendTime=cursor.getString(cursor.getColumnIndexOrThrow("sendTime"));
                res.add(smsID+"---"+smsName+"---"+sms+"---"+msg+"--"+sendTime);
            }
        }
        return res;
    }
    //得到最后一个数据
    private int getFirst(Cursor cursor){
        int res=-1;
        String st="";
        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                res=cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            }
            //res=Integer.parseInt(st);
        }
        return res;
    }
}

