package com.feicui.demo.videonews.ui.news.comments;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/12/26.
 */

public class TitleTextView extends TextView{
    /**
     * 带跑马灯效果的TextView
     * 需要重写isFocused，让走马灯效果一直存在
     */

    public TitleTextView(Context context) {
        super(context);
    }

    public TitleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TitleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
