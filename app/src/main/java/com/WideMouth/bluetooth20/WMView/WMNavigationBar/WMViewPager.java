package com.WideMouth.bluetooth20.WMView.WMNavigationBar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.qmuiteam.qmui.widget.QMUIViewPager;

/**
 * Created by Jue on 2018/6/8.
 */

public class WMViewPager extends QMUIViewPager {

    private boolean isCanScroll = true;

    public WMViewPager(Context context) {
        super(context);
    }

    public WMViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onTouchEvent(ev);

    }
}
