package com.example.hakim.anview;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
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
    Button savePhone,setBu,saveSetBu,set_content1,set_content2,set_content3,set_content4,set_content5,set_content6,sendSms,clearSend,addSend;
    Button disPlayPhone;
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
                    //displayPhone();
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
        disPlayPhone=(Button) findViewById(R.id.displayPhone);
        saveAllText();
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
                final EditText randTime=(EditText) window.findViewById(R.id.setRandTime);
                //开始和结束时间
                final EditText StartTime = (EditText) window.findViewById(R.id.setStartTime);
                final EditText endTime = (EditText) window.findViewById(R.id.setEndTime);

                saveSetBu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //添加判断
                        String start_time_s=StartTime.getText().toString();
                        String randTime_s=randTime.getText().toString();
                        String end_time_s = endTime.getText().toString();
                        String inter_time_s = IntervaTime.getText().toString();
                        if (is_string_enpty(start_time_s)||is_string_enpty(randTime_s)||is_string_enpty(end_time_s)||is_string_enpty(inter_time_s)){
                            Toast.makeText(MainActivity.this, "请填写完整内容"
                                    , Toast.LENGTH_SHORT).show();
                        }else{
                            Integer start_time = Integer.parseInt(StartTime.getText().toString());
                            Integer randTime = Integer.parseInt(StartTime.getText().toString());
                            Integer end_time = Integer.parseInt(endTime.getText().toString());
                            Integer inter_time = Integer.parseInt(IntervaTime.getText().toString());
                            if (inter_time<=randTime){
                                Toast.makeText(MainActivity.this, "间隔时间应小于随机时间"
                                        , Toast.LENGTH_SHORT).show();
                            }else {
                                saveset.save(start_time, end_time, inter_time,randTime);
                                displaySet();
                            }
                        }

                    }
                });
            }
        });
        disPlayPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayPhone();
            }
        });
        savePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View listViewq = getLayoutInflater().inflate(R.layout.push_content, null);
                final Dialog alertDialog1= new AlertDialog.Builder(MainActivity.this).setView(listViewq).setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //read.saveNumber(db);
                            }
                        }).show();
                Window window = alertDialog1.getWindow();
                window.setContentView(R.layout.push_content);
                final EditText startName=(EditText) window.findViewById(R.id.setStartCon);
                final EditText endName=(EditText) window.findViewById(R.id.setEndCon);
                Button readCon=(Button) window.findViewById(R.id.readCon);
                readCon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String sart=startName.getText().toString();
                        String end=endName.getText().toString();
                        if (is_string_enpty(sart)||is_string_enpty(end)){
                            Toast.makeText(MainActivity.this, "请填写号码段"
                                    , Toast.LENGTH_SHORT).show();
                        }else {
                            final ArrayList<String> num_list = read.read(sart,end);
                            alertDialog1.dismiss();
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
                    }
                });
            }
        });
    }
    private void displayPhone(){

        final ArrayList<String> dataList = read.getPhoneList(db);
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return dataList.size();
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
                String number = dataList.get(i);
                TextView tv = new TextView(MainActivity.this);
                tv.setTextSize(18);
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

                    }
                }).show();
    }
    public void displaySet() {
        TextView IntervaTime,Rand_time,StartTime,endTime,content1,content2,content3,content4,content5,content6;
        IntervaTime=(TextView) findViewById(R.id.interTime);
        Rand_time=(TextView) findViewById(R.id.setRand);
        StartTime=(TextView) findViewById(R.id.starTime);
        endTime=(TextView) findViewById(R.id.endTime);
        content1=(TextView) findViewById(R.id.text1);
        content2=(TextView) findViewById(R.id.text2);
        content3=(TextView) findViewById(R.id.text3);
        content4=(TextView) findViewById(R.id.text4);
        content5=(TextView) findViewById(R.id.text5);
        content6=(TextView) findViewById(R.id.text6);
        String text1 = saveset.getText(1);
        String text2 = saveset.getText(2);
        String text3=saveset.getText(3),text4=saveset.getText(4),text5=saveset.getText(5),
        text6=saveset.getText(6);
        int i = 0;
        String in_time = "", start_time = "", end_time = "",rand_time="";
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
                start_time = cursor1.getString(cursor1.getColumnIndexOrThrow("start_time"));
                end_time = cursor1.getString(cursor1.getColumnIndexOrThrow("end_time"));
                in_time = cursor1.getString(cursor1.getColumnIndexOrThrow("interva_time"));
                rand_time=cursor1.getString(cursor1.getColumnIndexOrThrow("rand_time"));
            }
        }

        IntervaTime.setText(in_time);
        Rand_time.setText(rand_time);
        //开始和结束时间
        StartTime.setText(start_time);
        endTime.setText(end_time);
        content1.setText(text1);
        content2.setText(text2);
        content3.setText(text3);
        content4.setText(text4);
        content5.setText(text5);
        content6.setText(text6);
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
    public boolean is_string_enpty(String str){
        //为空时返回true
        boolean res=false;
        if (str == null || str.length() <= 0){
            res=true;
        }
        return res;
    }
    public void saveAllText(){
        set_content1=(Button) findViewById(R.id.setText1);
        set_content2=(Button) findViewById(R.id.setText2);
        set_content3=(Button) findViewById(R.id.setText3);
        set_content4=(Button) findViewById(R.id.setText4);
        set_content5=(Button) findViewById(R.id.setText5);
        set_content6=(Button) findViewById(R.id.setText6);
        set_content1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAllTextAler(1);
            }
        });
        set_content2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAllTextAler(2);
            }
        });
        set_content3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAllTextAler(3);
            }
        });
        set_content4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAllTextAler(4);
            }
        });
        set_content5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAllTextAler(5);
            }
        });
        set_content6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAllTextAler(6);
            }
        });
    }
    private void saveAllTextAler(int index){
        View view1 = getLayoutInflater().inflate(R.layout.set_text, null);
        final int id=index;
        final Dialog alertDialog= new AlertDialog.Builder(MainActivity.this).setView(view1).setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //read.saveNumber(db);
                    }
                }).show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.set_text);
        final EditText content1=(EditText) window.findViewById(R.id.setEditTextq);
        Button setText =(Button) window.findViewById(R.id.setTextq);
        setText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = content1.getText().toString();
                saveset.saveText(id, text);
                displaySet();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog.dismiss();
                    }
                },1000);
            }
        });
    }
}
