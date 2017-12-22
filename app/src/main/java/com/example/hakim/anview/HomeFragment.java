package com.example.hakim.anview;
import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by Hakim on 2017/12/22.
 */

public class HomeFragment extends BaseFragement {
    @Override
    protected void initView() {
    }
    @Override
    public int getLayoutId() {
        return R.layout.home_view;
    }
    @Override
    protected void getDataFromServer() {
        Toast.makeText(mContext, "主页", Toast.LENGTH_SHORT).show();
    }
}
