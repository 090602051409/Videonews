package com.feicui.demo.videonews.ui.news;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.feicui.demo.videonews.R;
import com.feicui.demo.videonews.bombapi.entity.NewsEntity;
import com.feicui.demo.videonews.commons.CommonUtils;
import com.feicui.demo.videonews.ui.base.BaseItemView;
import com.feicui.demo.videonews.ui.news.comments.CommentsActivity;
import com.feicui.demo.videoplayer.list.MediaPlayerManager;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/26.
 */

public class NewsItemView extends BaseItemView<NewsEntity> implements TextureView.SurfaceTextureListener, MediaPlayerManager.OnPlaybackListener {
    @BindView(R.id.textureView)
    TextureView textureView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.ivPreview)
    ImageView ivPreview;
    @BindView(R.id.tvNewsTitle)
    TextView tvNewsTitle;
    @BindView(R.id.ivPlay)
    ImageView ivPlay;
    @BindView(R.id.tvCreatedAt)
    Button tvCreatedAt;

    private NewsEntity newsEntity;
    private MediaPlayerManager mediaPlayerManager;
    private Surface surface;

    public NewsItemView(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_news, this,true);
        ButterKnife.bind(this);
        //添加列表视频播放控制相关监听
        mediaPlayerManager=MediaPlayerManager.getsInstance(getContext());

        mediaPlayerManager.addPlayerBackListener(this);

        //textureView -> surface相关监听
        textureView.setSurfaceTextureListener(this);
    }

    //绑定数据
    @Override
    protected void bindModel(NewsEntity newsEntity) {
        this.newsEntity=newsEntity;
        //初始化视图状态
        tvNewsTitle.setVisibility(View.VISIBLE);
        ivPreview.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        ivPlay.setVisibility(View.VISIBLE);
        //设置标题，创建时间，预览图
        tvNewsTitle.setText(newsEntity.getNewsTitle());
        tvCreatedAt.setText(CommonUtils.format(newsEntity.getCreatedAt()));
        //设置预览图(Picasso)，服务器返回带中文的图片地址需要转换
        String url=CommonUtils.encodeUrl(newsEntity.getPreviewUrl());
        //使用Picasso来加载预览图像
        Picasso.with(getContext()).load(url).into(ivPreview);
    }

    //点击事件，跳转到评论页面
    @OnClick(R.id.tvCreatedAt)
    public void navigateToComments(){
        CommentsActivity.open(getContext(),newsEntity);
    }
    //点击预览图，开始播放
    @OnClick(R.id.ivPreview)
    public void startPlayer(){
        if (surface==null)return;
        String path=newsEntity.getVideoUrl();
        String videoId=newsEntity.getObjectId();
        mediaPlayerManager.startPlayer(surface,path,videoId);
    }
    //点击视频，停止播放
    @OnClick(R.id.textureView)
    public void stopPlayer(){
        mediaPlayerManager.stopPlayer();
    }
    //判断是否操作当前的视频
    private boolean isCurrentVideo(String videoId){
        if (videoId==null||newsEntity==null)return false;
        return videoId.equals(newsEntity.getObjectId());
    }
    //添加视频列表播放控制相关监听
    @Override
    public void onStartBuffering(String videoId) {
        //将当前视频的prb显示出来
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void onStopBuffering(String videoId) {
        //将当前的prb隐藏
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStartPlay(String videoId) {
        if (isCurrentVideo(videoId)){
            tvNewsTitle.setVisibility(View.INVISIBLE);
            ivPreview.setVisibility(View.INVISIBLE);
            ivPlay.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onStopPlay(String videoId) {
        if (isCurrentVideo(videoId)) {
            tvNewsTitle.setVisibility(View.VISIBLE);
            ivPreview.setVisibility(View.VISIBLE);
            ivPlay.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onSizeMeasured(String videoId, int width, int height) {

    }

    //textureView -> surface相关监听
    //拿到Surface
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        this.surface=new Surface(surface);
    }

    //当SurfaceTexture的缓冲区大小改变。
    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    //当surface销毁时，停止播放
    //当指定的SurfaceTexture即将被摧毁。
    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        this.surface.release();
        this.surface=null;
        //停止自己
        if (newsEntity.getObjectId().equals(mediaPlayerManager.getVideoId())){
            mediaPlayerManager.stopPlayer();
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

}
