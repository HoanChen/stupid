package me.khrystal.meituandetailpage.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/8/22
 * update time:
 * email: 723526676@qq.com
 */
public class ViewPager2 extends ViewPager {

    private boolean scrollable = true;

    public ViewPager2(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isScrollable() {
        return scrollable;
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return scrollable && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return scrollable && super.onTouchEvent(ev);
    }
}
