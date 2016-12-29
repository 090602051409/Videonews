package com.feicui.demo.videonews.ui.news;

import android.content.Context;
import android.util.AttributeSet;

import com.feicui.demo.videonews.bombapi.BombClient;
import com.feicui.demo.videonews.bombapi.NewsApi;
import com.feicui.demo.videonews.bombapi.entity.NewsEntity;
import com.feicui.demo.videonews.bombapi.result.QueryResult;
import com.feicui.demo.videonews.ui.base.BaseResourceView;

import retrofit2.Call;

/**
 * Created by Administrator on 2016/12/26.
 */

public class NewsListView extends BaseResourceView<NewsEntity,NewsItemView> {
    public NewsListView(Context context) {
        super(context);
    }

    public NewsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected Call<QueryResult<NewsEntity>> queryData(int limit, int skip) {
        return newsApi.getVideoNewsList(limit,skip);
    }

    @Override
    protected int getLimit() {
        return 5;
    }

    @Override
    protected NewsItemView createItemView() {
        return new NewsItemView(getContext());
    }
}
