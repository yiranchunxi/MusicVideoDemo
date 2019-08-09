package com.siasun.musicvideo.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.support.v4.util.LongSparseArray;

import com.siasun.musicvideo.model.bean.AudioBean;
import com.siasun.musicvideo.model.bean.DownloadMusicInfo;
import com.siasun.musicvideo.model.bean.OnLineSongListInfo;
import com.siasun.musicvideo.service.PlayService;

import java.util.ArrayList;
import java.util.List;






public class BaseAppHelper {

    private Context mContext;
    /**
     * 播放音乐service
     */
    private PlayService mPlayService;
    /**
     * 本地歌曲列表
     */
    private final List<AudioBean> mMusicList = new ArrayList<>();
    /**
     * 歌单列表
     */
    private final List<OnLineSongListInfo> mSongListInfo = new ArrayList<>();

    private final LongSparseArray<DownloadMusicInfo> mDownloadList = new LongSparseArray<>();

    private BaseAppHelper() {}

    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private final static BaseAppHelper INSTANCE = new BaseAppHelper();
    }

    public static BaseAppHelper get() {
        return SingletonHolder.INSTANCE;
    }

    public void init(Application application) {
        mContext = application.getApplicationContext();
        //这里可以做一些初始化的逻辑
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 获取PlayService对象
     * @return              返回PlayService对象
     */
    public PlayService getPlayService() {
        return mPlayService;
    }

    /**
     * 设置PlayService服务
     */
    public void setPlayService(PlayService service) {
        mPlayService = service;
    }

    /**
     * 获取扫描到的音乐数据集合
     * @return              返回list集合
     */
    public List<AudioBean> getMusicList() {
        return mMusicList;
    }


    /**
     * 设置音频结合
     * @param list              音频集合
     */
    public void setMusicList(List<AudioBean> list) {
        mMusicList.clear();
        mMusicList.addAll(list);
    }

    /**
     * 获取扫描到的音乐信息数据集合
     * @return              返回list集合
     */
    public List<OnLineSongListInfo> getSongListInfos() {
        return mSongListInfo;
    }

    /**
     * 获取扫描到的音乐下载数据集合
     * @return              返回list集合
     */
    public LongSparseArray<DownloadMusicInfo> getDownloadList() {
        return mDownloadList;
    }

}
