package com.feicui.demo.videoplayer.full;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.feicui.demo.videoplayer.R;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class VideoviewActivity extends AppCompatActivity {

    private static final String KEY_VIDEO_PATH = "video_path";

    //####################启动当前的activity###########################
    public static void open(Context context, String videoPath) {
        Intent intent = new Intent(context, VideoviewActivity.class);
        intent.putExtra(KEY_VIDEO_PATH, videoPath);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    //####################启动当前的activity###########################

    private VideoView videoView;
    private ImageView ivLoading;//缓冲信息（图像）
    private TextView tvBufferInfo;//缓冲信息（kb/s）
    private MediaPlayer mediaPlayer;
    private int bufferPercent;//缓冲百分比
    private int downloadSpeed;//下载速度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );
        //设置背景色
        getWindow().setBackgroundDrawableResource(android.R.color.black);
        setContentView(R.layout.activity_videoview);
        initBufferView();
        initVideoView();
    }

    //初始化VideoView
    private void initVideoView() {
        Vitamio.isInitialized(this);
        videoView = (VideoView) findViewById(R.id.videoView);
        // TODO: 2016/12/19 先用自带的MediaController，稍后自定义
        videoView.setMediaController(new CustomMediaController(this));
        videoView.setKeepScreenOn(true);
        videoView.requestFocus();//拿到焦点

        //缓冲第一步     准备
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //拿到mediaPlayer
                mediaPlayer=mp;
                //设置缓冲大小
                mediaPlayer.setBufferSize(512*1024);
            }
        });
        //缓冲第二步     缓冲状态：信息
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                //缓冲开始
                switch (what){
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        showBufferView();
                        if (videoView.isPlaying()){
                            videoView.pause();
                        }
                        return true;
                    //缓冲结束
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        hideBufferView();
                        videoView.start();
                        return true;
                    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                        downloadSpeed=extra;
                        upDateBufferView();
                        return true;
                }
                return false;
            }
        });
        //第三步   更新缓冲百分比
        videoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                bufferPercent=percent;
                upDateBufferView();
            }
        });

    }

    //初始化缓冲相关视图
    private void initBufferView() {
        tvBufferInfo = (TextView) findViewById(R.id.tvBufferInfo);
        ivLoading = (ImageView) findViewById(R.id.ivLoading);
        tvBufferInfo.setVisibility(View.INVISIBLE);
        ivLoading.setVisibility(View.INVISIBLE);
    }

    //设置数据源
    @Override
    protected void onResume() {
        super.onResume();
        videoView.setVideoPath(getIntent().getStringExtra(KEY_VIDEO_PATH));
    }

    //停止播放
    @Override
    protected void onPause() {
        super.onPause();
        videoView.stopPlayback();
    }

    //显示缓冲视图
    private void showBufferView(){
        tvBufferInfo.setVisibility(View.VISIBLE);
        ivLoading.setVisibility(View.INVISIBLE);
        downloadSpeed=0;
        bufferPercent=0;
    }
    //隐藏缓冲视图
    private void hideBufferView(){
        tvBufferInfo.setVisibility(View.INVISIBLE);
        ivLoading.setVisibility(View.INVISIBLE);
    }
    //更新缓冲视图UI
    private void upDateBufferView(){
        String info = bufferPercent + "%  " + downloadSpeed + "kb/s";
        tvBufferInfo.setText(info);
    }

}
