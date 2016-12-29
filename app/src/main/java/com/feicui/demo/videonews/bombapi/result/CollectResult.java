package com.feicui.demo.videonews.bombapi.result;

import com.feicui.demo.videonews.bombapi.entity.NewsEntity;

/**
 * Created by Administrator on 2016/12/27.
 */

public class CollectResult {
    private boolean success;
    private String error;
    private NewsEntity data;

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

    public NewsEntity getData() {
        return data;
    }
}
