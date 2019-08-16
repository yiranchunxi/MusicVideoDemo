package com.siasun.musicvideo.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.siasun.musicvideo.R;
import com.siasun.musicvideo.adapter.BaseReAdapter;
import com.siasun.musicvideo.adapter.LiveAdapter;
import com.siasun.musicvideo.base.view.BaseActivity;
import com.siasun.musicvideo.model.bean.LiveBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class LiveActivity  extends BaseActivity {
    public static final String URL = "url";
    public static final String SEAMLESS_PLAY = "seamless_play";
    public static final String TITLE = "title";
    public static final String IS_LIVE = "isLive";
    private static final String LIVE_URL = "http://alcdn.hls.xiaoka.tv/201986/b6f/2a0/e55vAO1no5kB3jOj/index.m3u8";

    @BindView(R.id.rv_live_list)
    RecyclerView rv_live_list;

    private LiveAdapter mAdapter;

    @Override
    public int getContentView() {
        return R.layout.activity_live;
    }

    @Override
    public void initView() {
        rv_live_list.setLayoutManager(new LinearLayoutManager(this));
        mAdapter=new LiveAdapter(this);
        rv_live_list.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {


        mAdapter.setOnItemClickListener(new BaseReAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(LiveActivity.this, PlayerActivity.class);
                intent.putExtra(URL, LIVE_URL);
                intent.putExtra(IS_LIVE, true);
                intent.putExtra(TITLE, "直播");
                startActivity(intent);

            }
        });

    }

    @Override
    public void initData() {
        List<LiveBean> beanList=new ArrayList<>();
        for(int i=0;i<10;i++){
            LiveBean bean=new LiveBean();
            if(i%2==0){
                bean.setCoverUrl(R.mipmap.banner2);
            }else {
                bean.setCoverUrl(R.mipmap.banner1);
            }
            beanList.add(bean);
        }

        mAdapter.clear(beanList);

    }
}
