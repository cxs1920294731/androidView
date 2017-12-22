package com.example.hakim.anview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Hakim on 2017/12/22.
 */
public class NoSlidingViewPaper extends ViewPager {
    public NoSlidingViewPaper(Context context) {
        super(context);
    }
    public NoSlidingViewPaper(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return false;
    }

    public boolean onTouchEvent(MotionEvent arg0) {
        return false;
    }
}
