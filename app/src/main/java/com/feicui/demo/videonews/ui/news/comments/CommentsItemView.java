package com.feicui.demo.videonews.ui.news.comments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.feicui.demo.videonews.R;
import com.feicui.demo.videonews.bombapi.entity.AuthorEntity;
import com.feicui.demo.videonews.bombapi.entity.CommentsEntity;
import com.feicui.demo.videonews.commons.CommonUtils;
import com.feicui.demo.videonews.ui.base.BaseItemView;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/26.
 */

public class CommentsItemView extends BaseItemView<CommentsEntity> {

    @BindView(R.id.tvAuthor)
    TextView tvAuthor;
    @BindView(R.id.tvContent)
    TextView tvContent;
    @BindView(R.id.tvCreatedAt)
    TextView tvCreatedAt;

    public CommentsItemView(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_comments, this, true);
        ButterKnife.bind(this);
    }

    @Override
    protected void bindModel(CommentsEntity commentsEntity) {
        Log.i("BBB",commentsEntity.toString());
        //数据绑定
        String content=commentsEntity.getContent();
        Date createdAt=commentsEntity.getCreatedAt();
        AuthorEntity authorEntity=commentsEntity.getAuthor();
        String username=authorEntity.getUsername();
        tvContent.setText(content);
        tvContent.setText(username);
        tvCreatedAt.setText(CommonUtils.format(createdAt));

       // Log.i("AAA",content+username+createdAt+"AAA");
    }
}
