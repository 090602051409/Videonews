package com.feicui.demo.videonews.ui.local;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.feicui.demo.videonews.R;
import com.feicui.demo.videoplayer.full.VideoviewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.vov.vitamio.provider.MediaStore;

/**
 * Created by Administrator on 2016/12/28.
 */
public class LocalVideoItem extends FrameLayout {


    public LocalVideoItem(Context context) {
        this(context, null);
    }

    public LocalVideoItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LocalVideoItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    @BindView(R.id.ivPreview)
    ImageView ivPreview;
    @BindView(R.id.tvVideoName)
    TextView tvVideoName;
    private String filePath;//文件路径

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_local_video, this, true);
        ButterKnife.bind(this);
    }
    public String getFilePath(){
        return filePath;
    }
    public void setIvPreView(Bitmap bitmap){
        ivPreview.setImageBitmap(bitmap);
    }
    //设置预览图像，可在后台线程运行
    public void setIvPreView(String filePath,final Bitmap bitmap){
        post(new Runnable() {
            @Override
            public void run() {
           ivPreview.setImageBitmap(bitmap);
            }
        });
    }
    //将数据绑定，将cursor内容设置到对应控件上
    public void bind(Cursor cursor){
        //取出视频文件
        String videoName=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DIRECTORY_NAME));
        tvVideoName.setText(videoName);
        //文件路径
        filePath=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
    }
    //获取视频预览图，是一个很费时的操作
    //-----到后台线程执行

    //同时会获取多张预览图，可能会有多个线程
    //-----线程池处理

    //获取过的图像，做缓存
    //-----LruCache（最近最少使用原则）

//        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(filePath,MediaStore.Video.Thumbnails.MINI_KIND);

    //设置预览图
//        ivPreview.setImageBitmap(bitmap);

//全屏播放
    @OnClick
    public void click(){
        VideoviewActivity.open(getContext(),filePath);
    }
}
