package com.feicui.demo.videonews;

import android.app.Application;

import com.feicui.demo.videonews.commons.ToastUtils;

/**
 * Created by Administrator on 2016/12/21.
 */

public class VideoNewsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.init(this);
    }
}
