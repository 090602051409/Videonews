package com.feicui.demo.videonews.ui.local;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.provider.MediaStore;

import com.feicui.demo.videonews.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/12/21.
 */

public class LocalFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    @BindView(R.id.gridview)
    GridView gridview;

    private View view;
    private Unbinder unbinder;
    private static final String TAG = "onLoadFinished";
    private LocalVideoAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化当前页面的Loader（加载器，去loader视频数据）
        getLoaderManager().initLoader(0, null, this);
        //初始化适配器
        adapter = new LocalVideoAdapter(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_local_video, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        gridview.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.release();
    }

    //###############################loaderCallBack  start#####################################
    //创建所需loader对象
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                MediaStore.Video.Media._ID,//视频ID
                MediaStore.Video.Media.DATA,//视频文件的路径
                MediaStore.Video.Media.DISPLAY_NAME//视频名称
        };

        return new CursorLoader(
                getContext(),
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,//视频 uri
                projection,
                null, null, null
        );
    }
        //当数据加载完成时会触发：数据已加载完成，放在下面第二个参数data中
        @Override
        public void onLoadFinished (Loader < Cursor > loader, Cursor data){
            adapter.swapCursor(data);
            //测试
//        if (data.moveToFirst()) {
//            do {
//                int index = data.getColumnIndex(Media.DIRECTORY_NAME);
//                String displayname = data.getString(index);
//                Log.d("aaa", "onLoadFinished" + displayname);
//            } while (data.moveToNext());
//        }
        }

        //当数据加载重置
        @Override
        public void onLoaderReset (Loader < Cursor > loader) {
            adapter.swapCursor(null);
        }
        //###############################loaderCallBack  end#####################################
    }

