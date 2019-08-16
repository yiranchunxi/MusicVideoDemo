package com.siasun.musicvideo.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.siasun.musicvideo.R;
import com.siasun.musicvideo.base.view.BaseActivity;
import com.siasun.musicvideo.utils.GlideRoundTransform;
import com.siasun.musicvideo.utils.RoundImageView;

import butterknife.BindView;

public class GuideActivity extends BaseActivity {

    @BindView(R.id.btn_music)
    Button mButtonMusic;

    @BindView(R.id.btn_video)
    Button mButtonVideo;

    @BindView(R.id.iv_test)
    RoundImageView iv_test;

    @Override
    public int getContentView() {
        return R.layout.activity_guide;
    }

    @Override
    public void initView() {
        mButtonMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GuideActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


        mButtonVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GuideActivity.this,LiveActivity.class);
                startActivity(intent);
            }
        });

        RequestOptions options = new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                //.transforms(new GlideRoundTransform(this,10));
                .dontTransform();
        Glide.with(this)
                .load(R.mipmap.sanjiu)
                .apply(options)
                .into(iv_test);

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }
}
