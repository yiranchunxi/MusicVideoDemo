package com.siasun.musicvideo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videoplayer.ijk.IjkPlayerFactory;
import com.dueeeke.videoplayer.listener.OnVideoViewStateChangeListener;
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.util.L;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.siasun.musicvideo.R;
import com.siasun.musicvideo.base.view.BaseActivity;
import com.siasun.musicvideo.pop.ZhihuCommentPopup;
import com.siasun.musicvideo.utils.ExpandTextView;
import com.siasun.musicvideo.utils.ProgressManagerImpl;

import butterknife.BindView;

public class PlayerActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.player)
    VideoView mVideoView;


    @BindView(R.id.tv_content)
    ExpandTextView tv_content;


    @BindView(R.id.ll_comment)
    LinearLayout  ll_comment;

    private BasePopupView popupWindow;

    @Override
    public int getContentView() {
        return R.layout.activity_player;
    }

    @Override
    public void initView() {
        initExpandTextView();
    }

    @Override
    public void initListener() {
        ll_comment.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            StandardVideoController controller = new StandardVideoController(this);
            boolean isLive = intent.getBooleanExtra("isLive", false);
            if (isLive) {
                controller.setLive();
            }
            String title = intent.getStringExtra("title");
            controller.setTitle(title);
            mVideoView.setVideoController(controller);

            mVideoView.setUrl(intent.getStringExtra("url"));

            //保存播放进度
            mVideoView.setProgressManager(new ProgressManagerImpl());
            //播放状态监听
            mVideoView.addOnVideoViewStateChangeListener(mOnVideoViewStateChangeListener);

            //使用IjkPlayer解码
            mVideoView.setPlayerFactory(IjkPlayerFactory.create(this));
            //使用ExoPlayer解码
//            mVideoView.setPlayerFactory(ExoMediaPlayerFactory.create(this));
            //使用MediaPlayer解码
//            mVideoView.setPlayerFactory(AndroidMediaPlayerFactory.create(this));

            mVideoView.start();
        }
    }

    private OnVideoViewStateChangeListener mOnVideoViewStateChangeListener = new OnVideoViewStateChangeListener() {
        @Override
        public void onPlayerStateChanged(int playerState) {
            switch (playerState) {
                case VideoView.PLAYER_NORMAL://小屏
                    break;
                case VideoView.PLAYER_FULL_SCREEN://全屏
                    break;
            }
        }

        @Override
        public void onPlayStateChanged(int playState) {
            switch (playState) {
                case VideoView.STATE_IDLE:
                    break;
                case VideoView.STATE_PREPARING:
                    break;
                case VideoView.STATE_PREPARED:
                    //需在此时获取视频宽高
                    int[] videoSize = mVideoView.getVideoSize();
                    L.d("视频宽：" + videoSize[0]);
                    L.d("视频高：" + videoSize[1]);
                    break;
                case VideoView.STATE_PLAYING:
                    break;
                case VideoView.STATE_PAUSED:
                    break;
                case VideoView.STATE_BUFFERING:
                    break;
                case VideoView.STATE_BUFFERED:
                    break;
                case VideoView.STATE_PLAYBACK_COMPLETED:
                    break;
                case VideoView.STATE_ERROR:
                    break;
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mVideoView!=null){
            mVideoView.release();
        }

    }


    @Override
    public void onBackPressed() {

        Log.e("test","onBackPressed");
        if(popupWindow.isShow()){
            popupWindow.dismiss();
            return;
        }else{
            if (!mVideoView.onBackPressed()) {
                super.onBackPressed();
            }
        }

    }

    private void initExpandTextView() {

        tv_content.setMaxLineCount(3);
        tv_content.setCollapseText("收起");
        tv_content.setExpandText("展开全文");
        tv_content.setCollapseEnable(true);
        tv_content.setUnderlineEnable(false);
        tv_content.setCollapseTextColor(Color.parseColor("#de6500"));
        tv_content.setExpandTextColor(Color.parseColor("#de6500"));
        tv_content.setText("沈阳连续多日的高温，让人感到暑热难耐，然而在沈阳森林动物园里的大熊猫、黑猩猩、河马、松鼠猴等动物们则在动物园营造的清凉环境下,这个价钱不一定啊。那就买个油耗低那就买个油耗低的车那就买个油耗低的车那就买个油耗低的车", false, new ExpandTextView.Callback() {
            @Override
            public void onExpand() {

            }

            @Override
            public void onCollapse() {

            }

            @Override
            public void onLoss() {

            }

            @Override
            public void onExpandClick() {
                tv_content.setChanged(true);
            }

            @Override
            public void onCollapseClick() {
                tv_content.setChanged(false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.ll_comment:
                popupWindow= new XPopup.Builder(PlayerActivity.this)
                        .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
                        .asCustom(new ZhihuCommentPopup(PlayerActivity.this))/*.enableDrag(false)*/
                        .show();


                break;

        }
    }
}
