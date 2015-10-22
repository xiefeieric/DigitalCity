package com.fei.digitalcity.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Fei on 11/10/15.
 */
public class HoriViewPager extends ViewPager {

    private int mRawX = -1;

    public HoriViewPager(Context context) {
        super(context);
    }

    public HoriViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mRawX = (int) ev.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mRawX == -1) {
                    mRawX = (int) ev.getRawX();
                }
                int currentX = (int) ev.getRawX();
                int offsetX = currentX -mRawX;
                if (Math.abs(offsetX)>5) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    return super.dispatchTouchEvent(ev);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}
