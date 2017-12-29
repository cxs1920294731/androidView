package com.example.hakim.anview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by Administrator on 2017/12/19.
 */
public class MyData extends SQLiteOpenHelper {
    public MyData(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //存间隔时间，随机时间，起始和终止时间
        sqLiteDatabase.execSQL("create table set_table (id integer,start_time integer,end_time integer,interva_time integer,rand_time integer)");
        //存从通讯录导入满足条件的号码
        sqLiteDatabase.execSQL("create table save_monbile_table (id INTEGER PRIMARY KEY AUTOINCREMENT,monbile varchar(225),name varchar(255))");
        //存将要发送号码
        sqLiteDatabase.execSQL("create table send_sms_table (num varchar(225),is_send integer,name varchar(225),numID integer)");
        //存设置的短信内容
        sqLiteDatabase.execSQL("create table save_text_table (id integer,content varchar(225))");
        //存发送的结果
        sqLiteDatabase.execSQL("create table send_result_table (id integer,num varchar(225),res varchar(255),name varchar(255),sendTime varchar(255),resID INTEGER PRIMARY KEY AUTOINCREMENT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
