package com.feicui.demo.videonews.ui.likes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.feicui.demo.videonews.R;
import com.feicui.demo.videonews.bombapi.BombClient;
import com.feicui.demo.videonews.bombapi.UserApi;
import com.feicui.demo.videonews.bombapi.result.ErrorResult;
import com.feicui.demo.videonews.bombapi.entity.UserEntity;
import com.feicui.demo.videonews.bombapi.result.UserResult;
import com.feicui.demo.videonews.commons.ToastUtils;
import com.google.gson.Gson;


import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Administrator on 2016/12/21.
 */

public class RegisterFragment extends DialogFragment {
    @BindView(R.id.etUsername)
    TextInputEditText mEtUsername;
    @BindView(R.id.etPassword)
    TextInputEditText mEtPassword;
    @BindView(R.id.btnRegister)
    Button mBtnRegister;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //取消标题栏
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_register, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btnRegister)
    public void onClick() {
        final String username=mEtUsername.getText().toString();
        final String password=mEtPassword.getText().toString();

        //用户名和密码不能为空
        if (TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
            Toast.makeText(getContext(), R.string.username_or_password_can_not_be_null, Toast.LENGTH_SHORT).show();
            return;
        }

        //显示进度条
        mBtnRegister.setVisibility(View.GONE);

        // TODO: 2016/12/21 网络模块，请求注册
        //注册Api
        UserApi userApi=BombClient.getInstance().getUserApi();
        //构建用户实体类
        UserEntity userEntity=new UserEntity(username,password);
        //拿到call模型
       Call<UserResult> call=userApi.register(userEntity);
        //执行网络请求
        call.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {

                mBtnRegister.setVisibility(View.VISIBLE);
                //注册失败
                if (!response.isSuccessful()) {
                    //拿到失败的json
                    try {
                        String error = response.errorBody().string();
                        //通过gson将拿到的json数据解析成失败结果类
                        ErrorResult errorResult = new Gson().fromJson(error, ErrorResult.class);
                        //提示用户注册失败
                        ToastUtils.showShort(errorResult.getError());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                UserResult userResult=response.body();
                listener.registerSuccess(username,userResult.getObjectId());
                //提示注册成功
                ToastUtils.showShort(R.string.register_success);
            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                mBtnRegister.setVisibility(View.VISIBLE);
                //提示错误信息
                ToastUtils.showShort(t.getMessage());
            }
        });

    }
    //当注册成功会触发的方法
    public interface OnRegisterSuccessListener{
        //当注册成功来调用
        void registerSuccess(String username,String objectId);
    }
    private OnRegisterSuccessListener listener;

    public void setListener(OnRegisterSuccessListener listener) {
        this.listener = listener;
    }
}
