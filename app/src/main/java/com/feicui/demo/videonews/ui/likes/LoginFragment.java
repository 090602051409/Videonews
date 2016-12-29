package com.feicui.demo.videonews.ui.likes;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.feicui.demo.videonews.bombapi.result.UserResult;
import com.feicui.demo.videonews.commons.ToastUtils;
import com.google.gson.Gson;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/12/22.
 */

public class LoginFragment extends DialogFragment {

    @BindView(R.id.etUsername)
    TextInputEditText etUsername;
    @BindView(R.id.etPassword)
    TextInputEditText etPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //取消标题栏
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_login, container, false);
        unbinder=ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btnLogin)
    public void onClick() {
        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();

        //用户名和密码不能为空
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), R.string.username_or_password_can_not_be_null, Toast.LENGTH_SHORT).show();
            return;
        }

        //显示进度条
        btnLogin.setVisibility(View.GONE);

        // 登录网络请求
        UserApi userApi=BombClient.getInstance().getUserApi();
        //拿到call模型
        Call<UserResult> call=userApi.login(username,password);
        //执行网络请求
        call.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {

                btnLogin.setVisibility(View.VISIBLE);
                //登录失败
                if (!response.isSuccessful()) {
                    try {
                        String error = response.errorBody().string();
                        //通过gson将拿到的json数据解析成失败结果类
                        ErrorResult errorResult = new Gson().fromJson(error, ErrorResult.class);
                        //提示用登录失败
                        ToastUtils.showShort(errorResult.getError());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                //登录成功
                UserResult userResult=response.body();
                listener.loginSuccess(username,userResult.getObjectId());
                //提示登录成功
                ToastUtils.showShort(R.string.login_success);
            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                btnLogin.setVisibility(View.VISIBLE);
                //提示错误信息
                ToastUtils.showShort(t.getMessage());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    //当注册成功会触发的方法
    public interface OnLoginSuccessListener {
        //当登录成功来调用
        void loginSuccess(String username, String objectId);
    }

    private LoginFragment.OnLoginSuccessListener listener;

    public void setListener(@NonNull OnLoginSuccessListener listener) {
        this.listener = listener;
    }
}
