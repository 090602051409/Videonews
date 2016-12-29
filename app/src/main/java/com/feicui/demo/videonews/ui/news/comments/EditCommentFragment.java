package com.feicui.demo.videonews.ui.news.comments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.feicui.demo.videonews.R;
import com.feicui.demo.videonews.bombapi.BombClient;
import com.feicui.demo.videonews.bombapi.NewsApi;
import com.feicui.demo.videonews.bombapi.entity.PublishEntity;
import com.feicui.demo.videonews.bombapi.result.CommentsResult;
import com.feicui.demo.videonews.commons.ToastUtils;
import com.feicui.demo.videonews.ui.UserManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/12/27.
 */

public class EditCommentFragment extends android.support.v4.app.DialogFragment {
    private static final String KEY_NEWS_ID = "KEY_NEWS_ID";
    @BindView(R.id.etComment)
    EditText etComment;
    @BindView(R.id.btnOK)
    Button btnOK;
    private Unbinder unbinder;


    public static EditCommentFragment getInstance(String newsId) {
        EditCommentFragment fragment = new EditCommentFragment();
        Bundle args = new Bundle();
        args.putString(KEY_NEWS_ID, newsId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_edit_comment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btnOK)
    public void postComment() {
        String comment=etComment.getText().toString();
        //评论内容不能为空
        if (TextUtils.isEmpty(comment)){
            ToastUtils.showShort(R.string.please_edit_comment);
            return;
        }
        btnOK.setVisibility(View.INVISIBLE);
        //评论处理
        String userId= UserManager.getInstance().getObjectId();
        String newsId=getArguments().getString(KEY_NEWS_ID);

        NewsApi newsApi= BombClient.getInstance().getNewsApi();
        PublishEntity publishEntity=new PublishEntity(comment,userId,newsId);
        Call<CommentsResult> commentCall=newsApi.postComments(publishEntity);
        commentCall.enqueue(commentCallback);
    }
    private Callback<CommentsResult> commentCallback=new Callback<CommentsResult>() {
        @Override
        public void onResponse(Call<CommentsResult> call, Response<CommentsResult> response) {
            btnOK.setVisibility(View.VISIBLE);
            if (response.isSuccessful()){
                listener.onCommentSuccess();
                Log.e("CCC",response+"");
                return;
            }
            ToastUtils.showShort("评论异常");
        }

        @Override
        public void onFailure(Call<CommentsResult> call, Throwable t) {
            btnOK.setVisibility(View.VISIBLE);
            ToastUtils.showShort(t.getMessage());
        }
    };

    private OnCommentSuccessListener listener;
    public void setListener(@NonNull OnCommentSuccessListener listener){
        this.listener=listener;
    }
    public interface OnCommentSuccessListener{
        void onCommentSuccess();
    }
}
