package com.feicui.demo.videonews.bombapi;

import com.feicui.demo.videonews.bombapi.entity.UserEntity;
import com.feicui.demo.videonews.bombapi.result.UserResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/12/22.
 */

public interface UserApi {
    @POST("1/users")
    Call<UserResult> register(@Body UserEntity userEntity);

    @GET("1/login")
    Call<UserResult> login(@Query("username") String username,@Query("password") String password);
    //baseUrl
}
