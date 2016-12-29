package com.feicui.demo.videonews.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.feicui.demo.videonews.R;
import com.feicui.demo.videonews.ui.likes.LikesFragment;
import com.feicui.demo.videonews.ui.local.LocalFragment;
import com.feicui.demo.videonews.ui.news.NewsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.btnNews)
    Button btnNews;
    @BindView(R.id.btnLocal)
    Button btnLocal;
    @BindView(R.id.btnLikes)
    Button btnLikes;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    //初始化视图
    private void initView() {
        unbinder=ButterKnife.bind(this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(listener);
        btnNews.setSelected(true);
    }
    //viewPager适配器
    private FragmentPagerAdapter adapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            switch (position){
                //跳转到在线新闻的fragment
                case 0:
                    return new NewsFragment();
                //跳转到本地视频的fragment
                case 1:
                    return new LocalFragment();
                //跳转到我的收藏的fragment
                case 2:
                    return new LikesFragment();
                default:
                    throw new RuntimeException("未知错误");
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };
    //viewPager监听->Button的切换
    private ViewPager.OnPageChangeListener listener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
                //Button,UI改变
            btnNews.setSelected(position==0);
            btnLocal.setSelected(position==1);
            btnLikes.setSelected(position==2);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @OnClick({R.id.btnNews, R.id.btnLocal, R.id.btnLikes})
    public void chooseFragment(Button button) {
        switch (button.getId()) {
            //不要平滑效果，第二个参数传false
            case R.id.btnNews:
                viewPager.setCurrentItem(0,false);
                break;
            case R.id.btnLocal:
                viewPager.setCurrentItem(1,false);
                break;
            case R.id.btnLikes:
                viewPager.setCurrentItem(2,false);
                break;
            default:
                throw new RuntimeException("未知错误");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
