package com.feicui.demo.videonews.ui.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feicui.demo.videonews.R;
import com.feicui.demo.videonews.ui.UserManager;
import com.feicui.demo.videoplayer.list.MediaPlayerManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/21.
 */

public class NewsFragment extends Fragment {
    @BindView(R.id.newsListView_id)
    NewsListView newsListViewId;
    private View view;
    private boolean isPlay;//因为viewPager有缓存机制，所以需要控制视频的播放

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_news, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        //首次进来，自动刷新
        newsListViewId.post(new Runnable() {
            @Override
            public void run() {
           newsListViewId.autoRefresh();
            }
        });
    }

    //初始化MediaPlayer
    @Override
    public void onResume() {
        super.onResume();
        MediaPlayerManager.getsInstance(getContext()).onResume();
        UserManager.getInstance().setPlay(true);
    }

    //释放MediaPlayer
    @Override
    public void onPause() {
        super.onPause();
        if (UserManager.getInstance().isPlay()){
        MediaPlayerManager.getsInstance(getContext()).onPause();
        UserManager.getInstance().setPlay(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //移除view
        ((ViewGroup)view.getParent()).removeView(view);
    }

    //移除所有监听（不再需要与用户交互）
    @Override
    public void onDestroy() {
        super.onDestroy();
        MediaPlayerManager.getsInstance(getContext()).removeAllListeners();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isPlay)return;
        //正在播放，并且用户不可见（滑动到第二个viewPager）
        if (UserManager.getInstance().isPlay()){
            MediaPlayerManager.getsInstance(getContext()).onResume();

        }
    }
}
