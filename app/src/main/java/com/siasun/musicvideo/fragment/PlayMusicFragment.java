package com.siasun.musicvideo.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.siasun.musicvideo.R;
import com.siasun.musicvideo.base.view.BaseFragment;
import com.siasun.musicvideo.inter.callback.OnPlayerEventListener;
import com.siasun.musicvideo.model.bean.AudioBean;

import butterknife.BindView;

public class PlayMusicFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.tv_title)
    TextView tv_title;


    private static final String TAG = "DetailAudioFragment";


    @Override
    public void onClick(View v) {

    }

    @Override
    public int getContentView() {
        return R.layout.fragment_music;
    }

    @Override
    public void initView(View view) {
        getFragmentManager();

        getChildFragmentManager();
    }

    @Override
    public void initListener() {




        getPlayService().setOnPlayEventListener(new OnPlayerEventListener() {
            /**
             * 切换歌曲
             * 主要是切换歌曲的时候需要及时刷新界面信息
             */
            @Override
            public void onChange(AudioBean music) {

                setViewData(music);

            }

            /**
             * 继续播放
             * 主要是切换歌曲的时候需要及时刷新界面信息，比如播放暂停按钮
             */
            @Override
            public void onPlayerStart() {

                //ivPlay.setSelected(true);

            }

            /**
             * 暂停播放
             * 主要是切换歌曲的时候需要及时刷新界面信息，比如播放暂停按钮
             */
            @Override
            public void onPlayerPause() {
               // ivPlay.setSelected(false);
            }

            /**
             * 更新进度
             * 主要是播放音乐或者拖动进度条时，需要更新进度
             */
            @Override
            public void onUpdateProgress(int progress) {
                if(progress>0){
                    //如果没有拖动进度，则开始更新进度条进度
                 /*   if (!isDraggingProgress) {
                        sbProgress.setProgress(progress);
                    }
                    lrcView.updateTime(progress);*/
                }
            }

            @Override
            public void onBufferingUpdate(int percent) {
               /* if(sbProgress.getMax()>0 && percent>0){
                    AppLogUtils.e("setOnPlayEventListener---percent---"+ sbProgress.getMax() + "-----" +percent);
                    sbProgress.setSecondaryProgress(sbProgress.getMax() * 100 / percent);
                }*/
            }

            /**
             * 更新定时停止播放时间
             */
            @Override
            public void onTimer(long remain) {

            }
        });
    }

    @Override
    public void initData() {
        setViewData(getPlayService().getPlayingMusic());
    }


    /**
     * 对Fragment传递数据，建议使用setArguments(Bundle args)，而后在onCreate中使用getArguments()取出，
     * 在 “内存重启”前，系统会帮你保存数据，不会造成数据的丢失。和Activity的Intent恢复机制类似。
     * @param type                          type
     * @return                              PlayMusicFragment实例对象
     */
    public static PlayMusicFragment newInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(TAG, type);
        PlayMusicFragment fragment = new PlayMusicFragment();
        fragment.setArguments(bundle);
        return fragment;
    }



    /**
     * 填充页面数据
     *
     * @param playingMusic 正在播放的音乐
     */
    @SuppressLint("SetTextI18n")
    private void setViewData(AudioBean playingMusic) {
        if (playingMusic == null) {
            return;
        }
        tv_title.setText(playingMusic.getTitle());
        /*tvArtist.setText(playingMusic.getArtist());
        sbProgress.setProgress((int) getPlayService().getCurrentPosition());
        sbProgress.setSecondaryProgress(0);
        sbProgress.setMax((int) playingMusic.getDuration());
        AppLogUtils.e("-----------------------"+(int) playingMusic.getDuration());
        mLastProgress = 0;
        tvCurrentTime.setText("00:00");
        tvTotalTime.setText(VideoPlayerUtils.formatTime(playingMusic.getDuration()));
        setCoverAndBg(playingMusic);
        setLrc(playingMusic);
        if (getPlayService().isPlaying() || getPlayService().isPreparing()) {
            ivPlay.setSelected(true);
            //mAlbumCoverView.start();
        } else {
            ivPlay.setSelected(false);
            //mAlbumCoverView.pause();
        }*/
    }

}
