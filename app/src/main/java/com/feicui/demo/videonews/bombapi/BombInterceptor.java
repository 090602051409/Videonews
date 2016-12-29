package com.feicui.demo.videonews.bombapi;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/21.
 */

public class BombInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        //拿到请求消息
        Request request=chain.request();
        //拿到消息的构造器
        Request.Builder builder=request.newBuilder();
        // 用于让bomb服务器，区分是哪一个应用
        builder.addHeader("X-Bmob-Application-Id",BombConst.APPLICATION_ID);
        //用于授权
        builder.addHeader("X-Bmob-REST-API-Key", BombConst.REST_API_KEY);
        //请求和响应都统一使用json格式
        builder.addHeader("Content-Type","application/json");

        //构建得到添加请求头的请求消息
        request=builder.build();
        Response response=chain.proceed(request);
        return response;
    }
}
