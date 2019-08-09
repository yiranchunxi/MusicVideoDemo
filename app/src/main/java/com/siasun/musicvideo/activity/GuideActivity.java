package com.siasun.musicvideo.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.siasun.musicvideo.R;
import com.siasun.musicvideo.base.view.BaseActivity;

import butterknife.BindView;

public class GuideActivity extends BaseActivity {

    @BindView(R.id.btn_music)
    Button mButtonMusic;

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
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }
}
