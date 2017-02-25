package com.example.vorona.labviewpager;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by vorona on 29.07.16.
 */

public class MyViewPager extends ViewPager {
    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean isSwipable = true;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return isSwipable && super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return isSwipable && super.onTouchEvent(event);
    }

    public void setSwipable(boolean swipable) {
        isSwipable = swipable;
    }

}
