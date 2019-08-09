package com.siasun.musicvideo.executor.online;

import android.app.Activity;
import android.text.TextUtils;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.siasun.musicvideo.api.http.OnLineMusicModel;
import com.siasun.musicvideo.model.bean.AudioBean;
import com.siasun.musicvideo.model.bean.DownloadInfo;
import com.siasun.musicvideo.model.bean.OnlineMusicList;
import com.siasun.musicvideo.utils.musicUtils.FileMusicUtils;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public  abstract  class AbsPlayOnlineMusic extends AbsPlayMusic{


    private OnlineMusicList.SongListBean mOnlineMusic;
    private Activity mActivity;
    private AudioBean music;

    public AbsPlayOnlineMusic(Activity activity, OnlineMusicList.SongListBean onlineMusic) {
        super(activity);
        this.mActivity = activity;
        mOnlineMusic = onlineMusic;

    }
    @Override
    public void onPrepare() {

    }



    @Override
    void getPlayInfo() {
        String artist = mOnlineMusic.getArtist_name();
        String title = mOnlineMusic.getTitle();

        music = new AudioBean();
        music.setType(AudioBean.Type.ONLINE);
        music.setTitle(title);
        music.setArtist(artist);
        music.setAlbum(mOnlineMusic.getAlbum_title());

        // 下载歌词
        /*String lrcFileName = FileMusicUtils.getLrcFileName(artist, title);
        File lrcFile = new File(FileMusicUtils.getLrcDir() + lrcFileName);
        if (!lrcFile.exists() && !TextUtils.isEmpty(mOnlineMusic.getLrclink())) {
            downloadLrc(mOnlineMusic.getLrclink(), FileMusicUtils.getLrcDir(), lrcFileName);
        }*/

        // 下载封面
        /*String albumFileName = FileMusicUtils.getAlbumFileName(artist, title);
        File albumFile = new File(FileMusicUtils.getAlbumDir(), albumFileName);
        String picUrl = mOnlineMusic.getPic_big();
        if (TextUtils.isEmpty(picUrl)) {
            picUrl = mOnlineMusic.getPic_small();
        }
        if (!albumFile.exists() && !TextUtils.isEmpty(picUrl)) {
            downloadFile(picUrl, FileMusicUtils.getAlbumDir(), albumFileName);
        } else {
            mCounter++;
        }
        music.setCoverPath(albumFile.getPath());*/

        // 获取歌曲播放链接
        getMusicInfo(mOnlineMusic.getSong_id());
    }

    private void getMusicInfo(String songId) {
        OnLineMusicModel model = OnLineMusicModel.getInstance();
        model.getMusicDownloadInfo(OnLineMusicModel.METHOD_DOWNLOAD_MUSIC,songId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DownloadInfo>() {
                    @Override
                    public void accept(DownloadInfo downloadInfo) throws Exception {
                        if (downloadInfo == null || downloadInfo.getBitrate() == null) {
                            onExecuteFail(null);
                            return;
                        }
                        music.setPath(downloadInfo.getBitrate().getFile_link());
                        music.setDuration(downloadInfo.getBitrate().getFile_duration() * 1000);
                        checkCounter(music);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (throwable instanceof RuntimeException) {
                            onExecuteFail(null);
                        }
                    }
                },new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                });
    }

















    private void downloadLrc(String lrclink, String lrcDir, String lrcFileName) {
        FileDownloader.getImpl()
                .create(lrclink)
                .setPath(lrcDir)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        //ToastUtils.showRoundRectToast("下载完成");
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                }).start();
    }



    private void downloadFile(String lrclink, String lrcDir, String lrcFileName) {
        FileDownloader.getImpl()
                .create(lrclink)
                .setPath(lrcDir)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        //ToastUtils.showToast("下载完成");
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                }).start();
    }
}
