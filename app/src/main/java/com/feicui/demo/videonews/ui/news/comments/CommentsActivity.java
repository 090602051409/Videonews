package com.feicui.demo.videonews.ui.news.comments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.feicui.demo.videonews.R;
import com.feicui.demo.videonews.ui.UserManager;
import com.feicui.demo.videonews.bombapi.BombClient;
import com.feicui.demo.videonews.bombapi.NewsApi;
import com.feicui.demo.videonews.bombapi.entity.NewsEntity;
import com.feicui.demo.videonews.bombapi.result.CollectResult;
import com.feicui.demo.videonews.commons.CommonUtils;
import com.feicui.demo.videonews.commons.ToastUtils;
import com.feicui.demo.videoplayer.part.SimplevideoPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsActivity extends AppCompatActivity implements EditCommentFragment.OnCommentSuccessListener {
    private static final String KEY_NEWS = "KEY_NEWS";
    private NewsEntity newsEntity;
    private EditCommentFragment editCommentFragment;

    @BindView(R.id.tvTitle)
    TitleTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.commentsListView)
    CommentsListView commentsListView;
    @BindView(R.id.simpleVideoPlayer)
    SimplevideoPlayer simpleVideoPlayer;


    //对外公开一个跳转进来的方法
    public static void open(Context context, NewsEntity newsEntity) {
        Intent intent = new Intent(context, CommentsActivity.class);
        intent.putExtra(KEY_NEWS, newsEntity);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        //拿到新闻数据
        newsEntity = (NewsEntity) getIntent().getSerializableExtra(KEY_NEWS);
        //设置toolbar
        setSupportActionBar(toolbar);
        //给左上角加返回图标（返回按钮）
        tvTitle.setText(newsEntity.getNewsTitle());
        //初始化SimpleVideoPlayer,设置数据源
        String videoPath = CommonUtils.encodeUrl(newsEntity.getPreviewUrl());
        simpleVideoPlayer.setVideoPath(videoPath);
        //初始化commentsListView，设置newsid
        commentsListView.setNewsId(newsEntity.getObjectId());
        commentsListView.autoRefresh();
    }
    // #######################   视频相关  start  #####################

    @Override
    protected void onResume() {
        super.onResume();
        simpleVideoPlayer.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        simpleVideoPlayer.onPause();
    }
    // #######################   视频相关  start  #####################

    // #######################   toolbar    相关  #####################

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.activity_comments,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       //左上角返回按钮
        if (item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        //判断是否离线
        if (UserManager.getInstance().isOffline()){
            ToastUtils.showShort(R.string.please_login_first);
            return true;
        }
        //收藏
        if (item.getItemId()==R.id.menu_item_like){
            String newsId=newsEntity.getObjectId();
            String userId=UserManager.getInstance().getUsername();
            NewsApi newsApi_cloud= BombClient.getInstance().getNewsApi_cloud();
            Call<CollectResult> call=newsApi_cloud.collectNews(newsId,userId);
            call.enqueue(callback);
        }
        //评论
        if (item.getItemId()==R.id.menu_item_comment){
            if (editCommentFragment==null){
                String newsId=newsEntity.getObjectId();
                editCommentFragment=EditCommentFragment.getInstance(newsId);
                editCommentFragment.setListener(this);
            }
            editCommentFragment.show(getSupportFragmentManager(),"Edit Comment");
        }
        return super.onOptionsItemSelected(item);
    }
    // #######################   toolbar    相关  #####################

    private Callback<CollectResult> callback=new Callback<CollectResult>() {
        @Override
        public void onResponse(Call<CollectResult> call, Response<CollectResult> response) {
            CollectResult result=response.body();
            if (result.isSuccess()){
                ToastUtils.showShort(R.string.like_success);
            }else {
                ToastUtils.showShort(R.string.like_failure+result.getError());
            }
        }

        @Override
        public void onFailure(Call<CollectResult> call, Throwable t) {
            ToastUtils.showShort(t.getMessage());
        }
    };

    @Override
    public void onCommentSuccess() {
        editCommentFragment.dismiss();
        //刷新视图，获取最新评论
        commentsListView.autoRefresh();
    }
}
