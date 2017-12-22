package com.example.hakim.anview;

import android.widget.Toast;
import android.support.v4.app.Fragment;
/**
 * Created by Hakim on 2017/12/22.
 */

public class MessageFragment extends BaseFragement {
    @Override
    protected void initView() {

    }

    @Override
    public int getLayoutId() {
        Toast.makeText(mContext, "设置页面加载", Toast.LENGTH_SHORT).show();
        return R.layout.set_view;
    }

    @Override
    protected void getDataFromServer() {
    }
}
