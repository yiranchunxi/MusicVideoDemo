package com.siasun.musicvideo.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.siasun.musicvideo.R;
import com.siasun.musicvideo.activity.MainActivity;
import com.siasun.musicvideo.app.BaseAppHelper;
import com.siasun.musicvideo.app.BaseApplication;
import com.siasun.musicvideo.base.view.BaseFragment;
import com.siasun.musicvideo.executor.online.AbsPlayOnlineMusic;
import com.siasun.musicvideo.inter.callback.OnPlayerEventListener;
import com.siasun.musicvideo.model.bean.AudioBean;
import com.siasun.musicvideo.utils.ExpandTextView;
import com.siasun.musicvideo.utils.VideoPlayerUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import cn.ycbjie.ycthreadpoollib.PoolThread;

public class PlayMusicFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.tv_title)
    TextView tv_title;


    @BindView(R.id.iv_play_pause)
    ImageView ivPlay;


    @BindView(R.id.tv_current_time)
    TextView tvCurrentTime;
    @BindView(R.id.sb_progress)
    SeekBar sbProgress;
    @BindView(R.id.tv_total_time)
    TextView tvTotalTime;



    @BindView(R.id.iv_next)
    ImageView iv_next;

    @BindView(R.id.iv_prev)
    ImageView iv_prev;


    @BindView(R.id.tv_content)
    ExpandTextView tv_content;

    private static final String TAG = "DetailAudioFragment";
    private int mLastProgress;

    /**
     * 是否拖进度，默认是false
     */
    private boolean isDraggingProgress;
    private AudioManager mAudioManager;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_play_pause:
                play();
                break;
            case R.id.iv_next:
                next();
                break;
            case R.id.iv_prev:
                prev();
                break;

            default:
                break;
        }
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_music;
    }

    @Override
    public void initView(View view) {
        getFragmentManager();

        getChildFragmentManager();




        initExpandTextView();


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
    public void initListener() {
        ivBack.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        iv_prev.setOnClickListener(this);
        iv_next.setOnClickListener(this);
        initSeekBarListener();
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

                ivPlay.setSelected(true);

            }

            /**
             * 暂停播放
             * 主要是切换歌曲的时候需要及时刷新界面信息，比如播放暂停按钮
             */
            @Override
            public void onPlayerPause() {
                ivPlay.setSelected(false);
            }

            /**
             * 更新进度
             * 主要是播放音乐或者拖动进度条时，需要更新进度
             */
            @Override
            public void onUpdateProgress(int progress) {
                if(progress>0){
                    //如果没有拖动进度，则开始更新进度条进度
                    if (!isDraggingProgress) {
                        sbProgress.setProgress(progress);
                    }
                   // lrcView.updateTime(progress);
                }
            }

            @Override
            public void onBufferingUpdate(int percent) {
                if(sbProgress.getMax()>0 && percent>0){
                    //AppLogUtils.e("setOnPlayEventListener---percent---"+ sbProgress.getMax() + "-----" +percent);
                    sbProgress.setSecondaryProgress(sbProgress.getMax() * 100 / percent);
                }
            }

            /**
             * 更新定时停止播放时间
             */
            @Override
            public void onTimer(long remain) {

            }

            @Override
            public void onPlayPathNull(final int position) {

                new AbsPlayOnlineMusic(getActivity(), position) {
                    @Override
                    public void onPrepare() {

                    }

                    @Override
                    public void onExecuteSuccess() {
                        getPlayService().play(position);
                        Toast.makeText(getActivity(),"正在播放" + BaseAppHelper.get().getMusicList().get(position).getTitle(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onExecuteFail(Exception e) {
                        Toast.makeText(getActivity(),BaseApplication.getInstance().getResources().getString(R.string.unable_to_play),Toast.LENGTH_SHORT).show();

                        next();
                    }
                }.execute();



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
        mLastProgress = 0;

        sbProgress.setProgress((int) getPlayService().getCurrentPosition());
        sbProgress.setSecondaryProgress(0);
        sbProgress.setMax((int) playingMusic.getDuration());
        tvCurrentTime.setText("00:00");
        tvTotalTime.setText(VideoPlayerUtils.formatTime(playingMusic.getDuration()));
         /*tvArtist.setText(playingMusic.getArtist());
        AppLogUtils.e("-----------------------"+(int) playingMusic.getDuration());
        setCoverAndBg(playingMusic);
        setLrc(playingMusic);
        */
        if (getPlayService().isPlaying() || getPlayService().isPreparing()) {
            ivPlay.setSelected(true);
            //mAlbumCoverView.start();
        } else {
            ivPlay.setSelected(false);
            //mAlbumCoverView.pause();
        }
    }
    /**
     * 返回监听
     */
    private void onBackPressed() {
        getActivity().onBackPressed();
        ivBack.setEnabled(false);
        PoolThread executor = BaseApplication.getInstance().getExecutor();
        executor.setDelay(300, TimeUnit.MILLISECONDS);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ivBack.setEnabled(true);
            }
        });

    }

    private void prev() {
        if (getPlayService() != null) {
            Toast.makeText(getActivity(),R.string.state_prev,Toast.LENGTH_SHORT).show();
            getPlayService().prev();
        }
    }

    private void next() {
        if (getPlayService() != null) {
            Toast.makeText(getActivity(),R.string.state_next,Toast.LENGTH_SHORT).show();
            getPlayService().next();
        }
    }

    private void play() {
        if (getPlayService() != null) {
            getPlayService().playPause();
        }
    }

    private void initSeekBarListener() {
        SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar == sbProgress) {
                    if (Math.abs(progress - mLastProgress) >= DateUtils.SECOND_IN_MILLIS) {
                        tvCurrentTime.setText(VideoPlayerUtils.formatTime(progress));
                        mLastProgress = progress;
                    }
                }
            }

            /**
             * 通知用户已启动触摸手势,开始触摸时调用
             * @param seekBar               seekBar
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (seekBar == sbProgress) {
                    isDraggingProgress = true;
                }
            }


            /**
             * 通知用户已结束触摸手势,触摸结束时调用
             * @param seekBar               seekBar
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar == sbProgress) {
                    isDraggingProgress = false;
                    //如果是正在播放，或者暂停，那么直接拖动进度
                    if (getPlayService().isPlaying() || getPlayService().isPausing()) {
                        //获取进度
                        int progress = seekBar.getProgress();
                        //直接移动进度
                        getPlayService().seekTo(progress);
                       // lrcView.updateTime(progress);
                    } else {
                        //其他情况，直接设置进度为0
                        seekBar.setProgress(0);
                    }
                }/* else if (seekBar == sbVolume) {
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, seekBar.getProgress(),
                            AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                }*/
            }
        };
        sbProgress.setOnSeekBarChangeListener(onSeekBarChangeListener);
      //  sbVolume.setOnSeekBarChangeListener(onSeekBarChangeListener);
    }
}
