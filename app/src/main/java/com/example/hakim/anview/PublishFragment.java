package com.example.hakim.anview;

import android.widget.Toast;
import android.support.v4.app.Fragment;
/**
 * Created by Hakim on 2017/12/22.
 */

public class PublishFragment extends BaseFragement {
    @Override
    protected void initView() {

    }

    @Override
    public int getLayoutId() {
        Toast.makeText(mContext, "HomeFragment页面请求数据了", Toast.LENGTH_SHORT).show();
        return R.layout.res_view;
    }

    @Override
    protected void getDataFromServer() {
    }
}
