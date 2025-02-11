package com.example.test.my_tools;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class MyViewPager extends ViewPager {
    private OnViewPagerTouchListener mTouchListener = null;

    public MyViewPager(@NonNull Context context) {
        super(context);
    }

    public MyViewPager(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // 处理触摸事件，判断按下和抬起
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mTouchListener != null) {
                    mTouchListener.onPageTouched(true); // 用户触摸
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mTouchListener != null) {
                    mTouchListener.onPageTouched(false); // 触摸结束
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void setOnViewPagerTouchListener(OnViewPagerTouchListener listener) {
        this.mTouchListener = listener;
    }

    // 接口方法，传递触摸状态
    public interface OnViewPagerTouchListener {
        void onPageTouched(boolean isTouch); // 触摸状态
    }
}
