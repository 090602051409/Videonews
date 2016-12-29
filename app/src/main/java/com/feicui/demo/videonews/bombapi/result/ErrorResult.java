package com.feicui.demo.videonews.bombapi.result;

/**
 * Created by Administrator on 2016/12/22.
 */

public class ErrorResult {
    private int code;
    private String error;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
