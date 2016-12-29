package com.feicui.demo.videonews.ui.news.comments;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.feicui.demo.videonews.R;
import com.feicui.demo.videonews.bombapi.BombClient;
import com.feicui.demo.videonews.bombapi.BombConst;
import com.feicui.demo.videonews.bombapi.entity.AuthorEntity;
import com.feicui.demo.videonews.bombapi.entity.CommentsEntity;
import com.feicui.demo.videonews.bombapi.other.InQuery;
import com.feicui.demo.videonews.bombapi.result.QueryResult;
import com.feicui.demo.videonews.commons.CommonUtils;
import com.feicui.demo.videonews.ui.base.BaseItemView;
import com.feicui.demo.videonews.ui.base.BaseResourceView;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by Administrator on 2016/12/26.
 */

public class CommentsListView extends BaseResourceView<CommentsEntity,CommentsItemView>{

    public CommentsListView(Context context) {
        super(context);
    }

    public CommentsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public String newsId;
    public void setNewsId(String newsId){
        this.newsId=newsId;
    }
    @Override
    protected Call<QueryResult<CommentsEntity>> queryData(int limit, int skip) {
        InQuery where=new InQuery(BombConst.FIELD_NEWS,BombConst.TABLE_NEWS,newsId);
        return BombClient.getInstance().getNewsApi().getComments(limit,skip,where);
    }

    @Override
    protected int getLimit() {
        return 20;
    }

    @Override
    protected CommentsItemView createItemView() {
        return new CommentsItemView(getContext());
    }
}
