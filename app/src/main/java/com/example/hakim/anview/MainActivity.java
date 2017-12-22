package com.example.hakim.anview;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private long firstTime;
    private TextView mTextMessage;
    private BottomNavigationView navigation;
    private NoSlidingViewPaper mViewPager;
    readContacts read = new readContacts(this);
    MyData dehelper;
    SQLiteDatabase db;
    Dialog dialog;
    saveSet saveset;
    TimerT timeCao;
    //定义数据
    Button savePhone,setBu,saveSetBu,set_content1,set_content2,sendSms,clearSend,addSend;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager.setCurrentItem(0);
                    ininIndex();
                    displaySend();
                    return true;
                case R.id.navigation_dashboard:
                    mViewPager.setCurrentItem(1);
                    ininSetView();
                    displayPhone();
                    return true;
                case R.id.navigation_notifications:
                    mViewPager.setCurrentItem(2);
                    initRes();
                    return true;
            }
            return false;
        }
    };
    //双击才能退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dehelper = new MyData(this, "mySend.db3", null, 1);
        db = dehelper.getReadableDatabase();
        saveset=new saveSet(this,db);
        timeCao=new TimerT(this,db);
        mViewPager = (NoSlidingViewPaper) findViewById(R.id.vp_main_container);
        final ArrayList<Fragment> fgLists = new ArrayList<Fragment>(3);
        fgLists.add(new HomeFragment());
        fgLists.add(new MessageFragment());
        fgLists.add(new PublishFragment());
        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fgLists.get(position);
            }
            @Override
            public int getCount() {
                return fgLists.size();
            }
        };
        mViewPager.setAdapter(mAdapter);
        //mViewPager.setOffscreenPageLimit(0); //预加载剩下两页
        mViewPager.setCurrentItem(0);
        //displaySend();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        /*给底部导航栏菜单项添加点击事件*/
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    private void displaySend(){
        final ArrayList<String> send_list=read.getList(db);
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return send_list.size();
            }
            @Override
            public Object getItem(int i) {
                return i;
            }
            @Override
            public long getItemId(int i) {
                return i;
            }
            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                String number = send_list.get(i);
                TextView tv = new TextView(MainActivity.this);
                tv.setTextSize(20);
                tv.setPadding(3,1,0,1);
                tv.setText(number);
                return tv;
            }
        };
        final ListView listView1 = (ListView) findViewById(R.id.listSend);
        listView1.setAdapter(adapter);
    }
    private void initRes(){
        final ArrayList<String> send_list=read.getRes(db);
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return send_list.size();
            }
            @Override
            public Object getItem(int i) {
                return i;
            }
            @Override
            public long getItemId(int i) {
                return i;
            }
            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                String number = send_list.get(i);
                TextView tv = new TextView(MainActivity.this);
                tv.setTextSize(20);
                tv.setPadding(3,1,0,1);
                tv.setText(number);
                return tv;
            }
        };
        final ListView listView1 = (ListView) findViewById(R.id.sendRes);
        listView1.setAdapter(adapter);
    }
    private ArrayList<String> getData(){
        ArrayList<String> data = read.getPhoneList(db);
        return data;
    }
    private void ininSetView(){
        displaySet();
        dialog=new Dialog(this);
        savePhone= (Button) findViewById(R.id.savePhone);
        setBu=(Button) findViewById(R.id.setBu);
        setBu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View listView1 = getLayoutInflater().inflate(R.layout.set_table, null);
                Dialog alertDialog= new AlertDialog.Builder(MainActivity.this).setView(listView1).setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //read.saveNumber(db);
                            }
                        }).show();
                Window window = alertDialog.getWindow();
                window.setContentView(R.layout.set_table);
                saveSetBu=(Button) window.findViewById(R.id.saveSetBu);
                final EditText IntervaTime = (EditText) window.findViewById(R.id.setInterTime);
                //开始和结束时间
                final EditText StartTime = (EditText) window.findViewById(R.id.setStartTime);
                final EditText endTime = (EditText) window.findViewById(R.id.setEndTime);
                set_content1=(Button) window.findViewById(R.id.setText1);
                set_content2=(Button) window.findViewById(R.id.setText2);
                final EditText content1=(EditText) window.findViewById(R.id.setEditText1);
                final EditText content2=(EditText) window.findViewById(R.id.setEditText2);
                saveSetBu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Integer start_time = Integer.parseInt(StartTime.getText().toString());
                        Integer end_time = Integer.parseInt(endTime.getText().toString());
                        Integer inter_time = Integer.parseInt(IntervaTime.getText().toString());
                        saveset.save(start_time, end_time, inter_time);
                    }
                });
                set_content1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String text = content1.getText().toString();
                        saveset.saveText(1, text);
                    }
                });
                set_content2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String text = content2.getText().toString();
                        saveset.saveText(2, text);
                    }
                });
            }
        });
        savePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<String> num_list = read.read();
                BaseAdapter adapter = new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return num_list.size();
                    }
                    @Override
                    public Object getItem(int i) {
                        return i;
                    }
                    @Override
                    public long getItemId(int i) {
                        return i;
                    }
                    @Override
                    public View getView(int i, View view, ViewGroup viewGroup) {
                        String number = num_list.get(i);
                        TextView tv = new TextView(MainActivity.this);
                        tv.setTextSize(20);
                        tv.setPadding(3,1,0,1);
                        tv.setText(number);
                        return tv;
                    }
                };
                View listView = getLayoutInflater().inflate(R.layout.list, null);
                final ListView listView1 = (ListView) listView.findViewById(R.id.list1);
                listView1.setAdapter(adapter);
                new AlertDialog.Builder(MainActivity.this).setView(listView).setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                read.saveNumber(db);
                            }
                        }).show();
            }
        });
    }
    private void displayPhone(){

        final ArrayList<String> data = read.getPhoneList(db);
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return data.size();
            }
            @Override
            public Object getItem(int i) {
                return i;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }
            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                String number = data.get(i);
                TextView tv = new TextView(MainActivity.this);
                tv.setTextSize(18);
                tv.setPadding(3,1,0,1);
                tv.setText(number);
                return tv;
            }
        };
        final ListView listView1 = (ListView) findViewById(R.id.numList);
        listView1.setAdapter(adapter);
    }
    public void displaySet() {
        TextView IntervaTime,StartTime,endTime,content1,content2;
        IntervaTime=(TextView) findViewById(R.id.interTime);
        StartTime=(TextView) findViewById(R.id.starTime);
        endTime=(TextView) findViewById(R.id.endTime);
        content1=(TextView) findViewById(R.id.text1);
        content2=(TextView) findViewById(R.id.text2);
        String text1 = "";
        String text2 = "";
        int i = 0;
        String in_time = "", start_time = "", end_time = "";
        Cursor cursor = null, cursor1 = null;
        try {
            cursor = db.rawQuery("select * from save_text_table", null);
            cursor1 = db.rawQuery("select * from set_table", null);
        } catch (Exception e) {
            String c = "";
            c = "cao";
        }

        if (cursor1 != null) {
            while (cursor1.moveToNext()) {
                //set start_time="+start_time+",end_time="+end_time+",interva_time="+inter_time+"

                start_time = cursor1.getString(cursor1.getColumnIndexOrThrow("start_time")).toString();
                end_time = cursor1.getString(cursor1.getColumnIndexOrThrow("end_time")).toString();
                in_time = cursor1.getString(cursor1.getColumnIndexOrThrow("interva_time")).toString();
            }
        }
        if (cursor != null) {
            while (cursor.moveToNext()) {
                if (i == 0) {
                    text1 = cursor.getString(cursor.getColumnIndexOrThrow("content")).toString();
                }
                if (i == 1) {
                    text2 = cursor.getString(cursor.getColumnIndexOrThrow("content")).toString();
                }
                i++;
            }
        }
        IntervaTime.setText(in_time);
        //开始和结束时间
        StartTime.setText(start_time);
        endTime.setText(end_time);
        content1.setText(text1);
        content2.setText(text2);

        if (i < 2) {
            Toast.makeText(MainActivity.this, "请编辑短信内容"
                    , Toast.LENGTH_SHORT).show();
        }
    }
    public void ininIndex(){
        sendSms=(Button) findViewById(R.id.sendSms);
        clearSend=(Button) findViewById(R.id.clearSend);
        addSend=(Button) findViewById(R.id.addSend);
        final EditText StartNum=(EditText) findViewById(R.id.StartNum);
        final EditText EndNum=(EditText) findViewById(R.id.EndNum);
        clearSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.execSQL("delete  from send_sms_table");
            }
        });
        sendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeCao.startTime();
            }
        });
        addSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final ArrayList<String> send_list = new ArrayList<String>();
                    if (is_empty("send_sms_table")) {
                        //没有发送完的情况
                        Toast.makeText(MainActivity.this, "还有未发送短信，请清空上次短信再添加"
                                , Toast.LENGTH_SHORT).show();
                    } else {
                        //已经发送完，或者为空
                        String start, end;
                        int i=0;
                        String mm = StartNum.getText().toString();
                        if (StartNum.getText().toString().length() == 0 || EndNum.getText().toString().length() == 0) {
                            Toast.makeText(MainActivity.this, "请确实是否填写号码段", Toast.LENGTH_LONG).show();
                        } else {
                            start = StartNum.getText().toString();
                            end = EndNum.getText().toString();
                            final Cursor cursor = read.sendList(db, start, end);
                            if (cursor != null) {
                                while (cursor.moveToNext()) {
                                    i++;
                                    // id INTEGER PRIMARY KEY AUTOINCREMENT,monbile varchar(225),name varchar(255))
                                    String sms = cursor.getString(cursor.getColumnIndex("monbile"));
                                    int smsID = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                                    String smsName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                                    //num varchar(225),is_send integer,name varchar(225),numID integer)
                                    String sql = "insert into send_sms_table VALUES ('"+sms+"',0,'"+smsName+"',"+smsID+")";
                                    db.execSQL(sql);
                                }
                            }
                        }
                        if (i>0){
                            Toast.makeText(MainActivity.this, "保存成功"
                                    , Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this, "未查到数据"
                                    , Toast.LENGTH_SHORT).show();
                        }

                    }

                } catch (Exception e) {
                    String x;
                    x = "ao";
                }
            }
        });

    }
    public boolean is_empty(String name) {
        //db.execSQL("delete from send_sms_table");
        Cursor cursor = db.rawQuery("select * from " + name + " where is_send = 0", null);
        String num, xl;
        if (cursor != null) {
            int x = cursor.getCount();
            if (cursor.getCount() > 0) {
                return true;
            }
        }
        return false;
    }
}
